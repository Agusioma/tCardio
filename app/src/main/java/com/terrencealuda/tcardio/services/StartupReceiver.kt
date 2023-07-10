package com.terrencealuda.tcardio.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.terrencealuda.tcardio.daggerhilt.TAG
import com.terrencealuda.tcardio.health.HealthServicesManager
import com.terrencealuda.tcardio.storage.repositories.PassiveDataRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Background data subscriptions are not persisted across device restarts. This receiver checks if
 * we enabled background data and, if so, registers again.
 */
@AndroidEntryPoint
class StartupReceiver : BroadcastReceiver() {

    @Inject lateinit var repository: PassiveDataRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        runBlocking {
            if (repository.passiveDataEnabled.first()) {
                // Make sure we have permission.
                val result = context.checkSelfPermission(android.Manifest.permission.BODY_SENSORS)
                if (result == PackageManager.PERMISSION_GRANTED) {
                    scheduleWorker(context)
                } else {
                    // We may have lost the permission somehow. Mark that background data is
                    // disabled so the state is consistent the next time the user opens the app UI.
                    repository.setPassiveDataEnabled(false)
                }
            }
        }
    }

    private fun scheduleWorker(context: Context) {
        // BroadcastReceiver's onReceive must complete within 10 seconds. During device startup,
        // sometimes the call to register for background data takes longer than that and our
        // BroadcastReceiver gets destroyed before it completes. Instead we schedule a WorkManager
        // job to perform the registration.
        Log.i(TAG, "Enqueuing worker")
        WorkManager.getInstance(context).enqueue(
            OneTimeWorkRequestBuilder<RegisterForBackgroundDataWorker>().build()
        )
    }
}

@HiltWorker
class RegisterForBackgroundDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val healthServicesManager: HealthServicesManager,
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        Log.i(TAG, "Worker running")
        runBlocking {
            healthServicesManager.registerForHeartRateData()
        }
        return Result.success()
    }
}
