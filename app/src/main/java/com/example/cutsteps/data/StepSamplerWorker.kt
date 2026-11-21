package com.example.cutsteps.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.work.Worker
import androidx.work.WorkerParameters

class StepSamplerWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            applicationContext.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return Result.success()
        }
        val repository = StepRepository(applicationContext)
        if (!repository.hasSensor()) return Result.success()
        return if (repository.sampleOnce()) Result.success() else Result.retry()
    }
}
