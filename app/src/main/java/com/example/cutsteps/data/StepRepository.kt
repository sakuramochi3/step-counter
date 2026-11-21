package com.example.cutsteps.data

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class StepRepository(context: Context) {

    private val appContext = context.applicationContext
    private val prefs: SharedPreferences =
        appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val sensorManager: SensorManager? =
        appContext.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val stepSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private val _ticks = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val ticks: SharedFlow<Unit> = _ticks.asSharedFlow()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun hasSensor(): Boolean = stepSensor != null

    fun start() {
        if (stepSensor != null) {
            sensorManager?.registerListener(listener, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stop() {
        sensorManager?.unregisterListener(listener)
    }

    @Synchronized
    fun record(cumulative: Long) {
        val today = todayKey()
        val lastDay = prefs.getString(KEY_LAST_DAY, null)
        val lastCum = prefs.getLong(KEY_LAST_CUM, -1L)
        val editor = prefs.edit()

        if (lastDay != null && lastDay != today && lastCum >= 0) {
            editor.putLong(endKey(lastDay), lastCum)
        }
        if (lastCum >= 0 && cumulative < lastCum) {
            editor.putLong(baseKey(today), cumulative)
        }
        if (!prefs.contains(baseKey(today))) {
            editor.putLong(baseKey(today), cumulative)
        }
        editor.putLong(KEY_LAST_CUM, cumulative)
        editor.putString(KEY_LAST_DAY, today)
        editor.apply()
        prune()
        _ticks.tryEmit(Unit)
    }

    fun sampleOnce(): Boolean {
        val sensor = stepSensor ?: return false
        val manager = sensorManager ?: return false
        val latch = CountDownLatch(1)
        val oneShot = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                    record(event.values[0].toLong())
                    latch.countDown()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        manager.registerListener(oneShot, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        val received = latch.await(3, TimeUnit.SECONDS)
        manager.unregisterListener(oneShot)
        return received
    }

    fun lastSevenDays(): List<DaySteps> {
        val today = todayKey()
        val lastCum = prefs.getLong(KEY_LAST_CUM, -1L)
        return (6 downTo 0).map { offset ->
            val key = dayKey(-offset)
            val base = prefs.getLong(baseKey(key), -1L)
            val end = prefs.getLong(endKey(key), -1L)
            val steps = when {
                base < 0 -> 0L
                key == today && lastCum >= base -> lastCum - base
                end >= base -> end - base
                else -> 0L
            }
            DaySteps(dayKey = key, offsetFromToday = offset, steps = steps.coerceAtLeast(0L))
        }
    }

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                record(event.values[0].toLong())
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
    }

    private fun todayKey(): String = dateFormat.format(System.currentTimeMillis())

    private fun dayKey(offsetDays: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, offsetDays)
        return dateFormat.format(cal.timeInMillis)
    }

    private fun prune() {
        val keepFrom = dayKey(-7)
        val editor = prefs.edit()
        for ((key, _) in prefs.all) {
            if ((key.startsWith("base_") || key.startsWith("end_")) && key.substringAfter('_') < keepFrom) {
                editor.remove(key)
            }
        }
        editor.apply()
    }

    private fun baseKey(day: String) = "base_$day"
    private fun endKey(day: String) = "end_$day"

    data class DaySteps(val dayKey: String, val offsetFromToday: Int, val steps: Long)

    companion object {
        private const val PREFS_NAME = "cute_steps"
        private const val KEY_LAST_CUM = "last_cum"
        private const val KEY_LAST_DAY = "last_day"
    }
}
