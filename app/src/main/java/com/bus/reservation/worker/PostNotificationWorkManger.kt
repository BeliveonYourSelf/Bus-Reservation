package com.bus.reservation.worker

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PostNotificationWorkManger @Inject constructor(private val workManager: WorkManager) {

    fun postNotification() {
        val constraint = Constraints.Builder().setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequest.Builder(
            PeriodicNotificationWorker::class.java,
            15,
            TimeUnit.MINUTES
        )
            .setConstraints(constraint)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "BookedSeat",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    fun cancelAllWorkTask (){
        workManager.cancelAllWork()
    }
}