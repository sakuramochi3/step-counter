package com.example.cutsteps

import android.app.Application
import android.os.Build
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

class CuteStepsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val previous = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                val sw = StringWriter()
                throwable.printStackTrace(PrintWriter(sw))
                val dir = File(filesDir, "crash").apply { mkdirs() }
                File(dir, LAST_CRASH_FILE).writeText(
                    "Android ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})\n\n$sw"
                )
            } catch (_: Throwable) {
            }
            previous?.uncaughtException(thread, throwable)
        }
    }

    companion object {
        const val LAST_CRASH_FILE = "last_crash.txt"

        fun lastCrashFile(app: Application): File = File(app.filesDir, "crash/$LAST_CRASH_FILE")
    }
}
