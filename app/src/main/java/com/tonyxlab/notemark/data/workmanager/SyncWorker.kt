package com.tonyxlab.notemark.data.workmanager


import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tonyxlab.notemark.data.local.database.dao.NoteDao
import com.tonyxlab.notemark.data.local.database.dao.SyncDao
import com.tonyxlab.notemark.data.local.database.entity.NoteEntity
import com.tonyxlab.notemark.data.local.datastore.DataStore
import com.tonyxlab.notemark.data.remote.sync.client.NotesRemote
import com.tonyxlab.notemark.data.remote.sync.dto.RemoteNote
import com.tonyxlab.notemark.data.remote.sync.dto.toEntity
import com.tonyxlab.notemark.data.remote.sync.dto.toRemote
import com.tonyxlab.notemark.data.remote.sync.entity.SyncOperation
import com.tonyxlab.notemark.domain.json.JsonSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException


class SyncWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val syncDao: SyncDao,
    private val noteDao: NoteDao,
    private val dataStore: DataStore,
    private val jsonSerializer: JsonSerializer,
    private val notesRemote: NotesRemote
) : CoroutineWorker(
        context, workerParams
) {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result = withContext(context = Dispatchers.IO) {

        val email = "vontonnie@gmail.com"
        Timber.tag("SyncWorker")
                .i("do work called")
        // 0) Guard: need auth and our internal user
        val token = dataStore.getAccessToken()
        if (token.isNullOrEmpty()) {
            Timber.tag("SyncWorker")
                    .i("No access token; skipping sync.")
            return@withContext Result.success()
        }
        // token is assumed injected by an interceptor; if not, pass it to remote calls.
        val userId = dataStore.getOrCreateInternalUserId()

        Timber.tag("SyncWorker")
                .i("Step 1 - UserId is: $userId")

        /*  if (!remote.ping()) {
              Timber.tag("SyncWorker").i("Ping failed — baseUrl unreachable")
              return@withContext Result.retry()
          }*/

        try {
            // === 1) UPLOAD QUEUE (record-by-record) ===
            val batch = syncDao.loadBatch(userId = userId, limit = 100)

            Timber.tag("SyncWorker")
                    .i("Step 2 - batch size: ${batch.size}")
            for (rec in batch) {
                val localNote: NoteEntity =
                    jsonSerializer.fromJson(NoteEntity.serializer(), rec.payload)

                Timber.tag("SyncWorker")
                        .i("Step 3 - operation is: ${rec.operation.name}")
                when (rec.operation) {
                    SyncOperation.CREATE -> {
                        Timber.tag("SyncWorker")
                                .i("Step 4 - entering CREATE")
                        //  val remote =rec.payload // RemoteNote

                        val remote = notesRemote.create(token, email, localNote.toRemote())
                        Timber.tag("SyncWorker")
                                .i("Step 5 - remote note created: ${remote.id}")
                        // Write back server id + timestamps, keep local id for continuity
                        noteDao.upsert(
                                remote.toEntity()
                                        .copy(id = localNote.id)
                        )
                        syncDao.deleteByIds(listOf(rec.id))
                    }

                    SyncOperation.UPDATE -> {
                        Timber.tag("SyncWorker")
                                .i("Step 6 - entering UPDATE")
                        val body = if (localNote.remoteId == null) {
                            // No remoteId? Promote to create.
                            localNote.toRemote()
                        } else {
                            localNote.toRemote()
                                    .copy(id = localNote.remoteId)
                        }

                        with(body) {
                            Timber.tag("SyncWorker")
                                    .i(
                                            """
                            
                              Id is:$id 
                              title is:$title
                              content is: $content
                              createdAt is:$createdAt
                            lastEditedAt is: $lastEditedAt
                        """.trimIndent()
                                    )
                        }
                        val saved = if (localNote.remoteId == null) {
                            notesRemote.create(token, email, body)
                        } else {
                            notesRemote.update(token, email, body)
                        }

                        noteDao.upsert(
                                saved.toEntity()
                                        .copy(id = localNote.id)
                        )
                        Timber.tag("SyncWorker")
                                .i("Step 8 - upsert Done")
                        syncDao.deleteByIds(listOf(rec.id))
                    }

                    SyncOperation.DELETE -> {

                        Timber.tag("SyncWorker")
                                .i("Step 7 - entering DELETE")
                        val remoteId = localNote.remoteId
                        if (remoteId != null) {
                            // if server call fails, throw to let WM retry
                            notesRemote.delete(token, email, remoteId)
                        }
                        // local already deleted; drop queue entry either way
                        syncDao.deleteByIds(listOf(rec.id))
                    }
                }
            }

            // === 2) DOWNLOAD FULL SNAPSHOT & RECONCILE ===
            val remoteNotes: List<RemoteNote> = notesRemote.getAll(token, email)

            // upsert all server items locally
            if (remoteNotes.isNotEmpty()) {
                noteDao.upsertAll(remoteNotes.map { it.toEntity() })
            }

            // delete locals that vanished on server (but keep local-only notes still queued)
            val serverIds = remoteNotes.map { it.id }
                    .toSet()
            noteDao.deleteMissingRemoteIds(serverIds)

            Result.success()
        } catch (e: IOException) {
            Timber.tag("SyncWorker")
                    .i("IO Exception - ${e.message}")
            // network/timeouts/transient -> retry with backoff
            Result.retry()
        } catch (e: Exception) {

            Timber.tag("SyncWorker")
                    .i("Other Exception - ${e.message}, ${e.toString()}, ${e.cause}, ${e.stackTrace}")
            // unexpected error -> fail and surface in WorkManager
            Result.failure()
        }
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
