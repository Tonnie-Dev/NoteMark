package com.tonyxlab.notemark.data.workmanager

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.tonyxlab.notemark.domain.model.SyncInterval
import com.tonyxlab.notemark.domain.model.toMinutes
import java.util.concurrent.TimeUnit

class SyncRequest(private val workManager: WorkManager) {

    companion object {
        private const val UNIQUE_MANUAL = "notemark:sync:manual"
        private const val UNIQUE_PERIODIC = "notemark:sync:periodic"

        val DEFAULT_CONSTRAINTS: Constraints = Constraints.Builder()
                .setRequiredNetworkType(networkType = NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
    }

    fun enqueueManualSync() {
        val request = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(DEFAULT_CONSTRAINTS)
                .setBackoffCriteria(
                        backoffPolicy = BackoffPolicy.EXPONENTIAL,
                        backoffDelay = 10,
                        timeUnit = TimeUnit.SECONDS
                )
                .build()

        workManager.enqueueUniqueWork(
                uniqueWorkName = UNIQUE_MANUAL,
                existingWorkPolicy = ExistingWorkPolicy.KEEP,
                request = request
        )
    }

    fun enqueuePeriodicSync(interval: SyncInterval) {

        if (interval == SyncInterval.MANUAL) {
            cancelPeriodic()
            return
        }

        val minutes = interval.toMinutes()
        val safeMinutes = maxOf(15,minutes)

        val request =
            PeriodicWorkRequestBuilder<SyncWorker>(
                    repeatInterval = safeMinutes.toLong(),
                    repeatIntervalTimeUnit = TimeUnit.MINUTES
            ).setConstraints(DEFAULT_CONSTRAINTS)
                    .build()

        workManager.enqueueUniquePeriodicWork(
                uniqueWorkName = UNIQUE_PERIODIC,
                existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.UPDATE,
                request = request
        )
    }

    fun cancelPeriodic() {
        workManager.cancelUniqueWork(UNIQUE_PERIODIC)
    }
}