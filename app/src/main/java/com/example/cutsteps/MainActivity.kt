package com.example.cutsteps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.cutsteps.data.StepSamplerWorker
import com.example.cutsteps.ui.theme.CuteStepsTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        scheduleStepSampling()
        setContent {
            CuteStepsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CuteStepsApp()
                }
            }
        }
    }

    private fun scheduleStepSampling() {
        val request = PeriodicWorkRequestBuilder<StepSamplerWorker>(
            15,
            TimeUnit.MINUTES
        ).setConstraints(Constraints.Builder().build()).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            STEP_SAMPLING_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    companion object {
        private const val STEP_SAMPLING_WORK = "cute_steps_sampling"
    }
}
