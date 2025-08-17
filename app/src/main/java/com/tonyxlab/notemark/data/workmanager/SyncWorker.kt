

package com.tonyxlab.notemark.data.workmanager

import android.content.Context




import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tonyxlab.notemark.domain.repository.NoteRepository
import io.ktor.client.plugins.HttpRequestTimeoutException
import java.io.IOException


class SyncWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val repository: NoteRepository
) : CoroutineWorker(
        context, workerParams
) {
    override suspend fun doWork(): Result =
        runCatching {

            Result.success()

        }.onFailure {  }.getOrElse { e ->

            if (e is HttpRequestTimeoutException  || e is IOException)
                Result.retry()
            else
                Result.failure()

        }
}