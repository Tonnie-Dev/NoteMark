package com.tonyxlab.notemark.data.workmanager

import android.content.Context
import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tonyxlab.notemark.domain.repository.NoteRepository
import okio.IOException

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val repository: NoteRepository
) : CoroutineWorker(
        context,
        workerParams
) {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun doWork(): Result =
        runCatching { Result.success() }
                .getOrElse { e ->

                    // TODO: Add HttpException Check 
                if (e is HttpException || e is IOException)
            Result.retry()

        else
            Result.failure()

    }
}