package com.tonyxlab.notemark.data.workmanager


import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tonyxlab.notemark.data.local.database.dao.NoteDao
import com.tonyxlab.notemark.data.local.datastore.DataStore
import com.tonyxlab.notemark.data.remote.sync.dao.SyncDao
import com.tonyxlab.notemark.data.remote.sync.dto.SyncRemote
import com.tonyxlab.notemark.data.remote.sync.dto.UploadRequest
import com.tonyxlab.notemark.data.remote.sync.mapper.toUploadItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SyncWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val syncDao: SyncDao,
    private val noteDao: NoteDao,
    private val dataStore: DataStore,
    private val remote: SyncRemote
) : CoroutineWorker(
        context, workerParams
) {
    override suspend fun doWork(): Result = withContext(context = Dispatchers.IO) {

        // Need Access Token and internal user Id

        val accessToken = dataStore.getAccessToken() ?: return@withContext Result.failure()

        val internalUserId = dataStore.getOrCreateInternalUserId()

        try {
            //upload Sync Queue

            val batch = syncDao.loadBatch(userId = internalUserId, limit = 100)
            if (batch.isNotEmpty()) {

                val uploadRequestRequest = UploadRequest(
                        userId = internalUserId,
                        items = batch.map { syncRecord -> syncRecord.toUploadItem() }
                )

                val response = remote.upload(
                        accessToken = accessToken,
                        body = uploadRequestRequest
                )

                val succeded =
                    response.items.filter { response -> response.status == "OK" }
                            .map { it.localId }

                if (succeded.isNotEmpty()){
                    syncDao.deleteByIds(succeded)
                }
            }

val inToken = dataStore.getDeltaToken

        } catch ()

        Result.Success()

    }

}


/*
override suspend fun doWork(): ListenableWorker.Result =
    runCatching {
        val accessToken = dataStore.getAccessToken() ?: retu



        ListenableWorker.Result.success()

    }.onFailure {  }.getOrElse { e ->

        if (e is HttpRequestTimeoutException  || e is IOException)
            ListenableWorker.Result.retry()
        else
            ListenableWorker.Result.failure()

    }
*/
