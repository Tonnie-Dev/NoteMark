@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.data.workmanager

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tonyxlab.notemark.BuildConfig
import com.tonyxlab.notemark.data.local.database.dao.NoteDao
import com.tonyxlab.notemark.data.local.database.dao.SyncDao
import com.tonyxlab.notemark.data.local.database.entity.NoteEntity
import com.tonyxlab.notemark.data.local.datastore.DataStore
import com.tonyxlab.notemark.data.remote.sync.client.RemoteNoteWriter
import com.tonyxlab.notemark.data.remote.sync.dto.toEntity
import com.tonyxlab.notemark.data.remote.sync.dto.toRemoteDto
import com.tonyxlab.notemark.data.remote.sync.entity.SyncOperation
import com.tonyxlab.notemark.domain.json.JsonSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import timber.log.Timber

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val syncDao: SyncDao,
    private val noteDao: NoteDao,
    private val dataStore: DataStore,
    private val jsonSerializer: JsonSerializer,
    private val remoteNoteWriter: RemoteNoteWriter
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        val token = dataStore.getAccessToken() ?: return@withContext Result.success()

        val email = BuildConfig.USER_EMAIL

        val userId = dataStore.getOrCreateInternalUserId()

        runCatching {

            uploadQueue(token, email, userId)
            downloadQueue(token, email)
            dataStore.saveLastSyncTimeInMillis(System.currentTimeMillis())
        }.fold(
                onSuccess = { Result.success() },
                // network/timeouts/transient -> retry with backoff
                // unexpected error -> fail and surface in WorkManager
                onFailure = { e -> if (e is IOException) Result.retry() else Result.failure()
                }
        )
    }

    /*
    override suspend fun doWork(): Result = withContext(context = Dispatchers.IO) {

        val email = "vontonnie@gmail.com"

        val token = dataStore.getAccessToken()

        if (token.isNullOrEmpty()) {

            return@withContext Result.success()
        }

        val userId = dataStore.getOrCreateInternalUserId()


        try {
            // === 1) UPLOAD QUEUE
            val batch = syncDao.loadBatch(userId = userId, limit = 100)

            for (rec in batch) {

                val localEntitySnapshot: NoteEntity =
                    jsonSerializer.fromJson(NoteEntity.serializer(), rec.payload)

                when (rec.operation) {

                    SyncOperation.CREATE -> {

                        val remoteNote =
                            remoteNoteWriter.create(
                                    token,
                                    email,
                                    localEntitySnapshot.toRemoteDto()
                            )
                        // Write back server id + timestamps, keep local id for continuity
                        noteDao.upsert(
                                value = remoteNote.toEntity()
                                        .copy(id = localEntitySnapshot.id)
                        )
                        syncDao.deleteByIds(listOf(rec.id))
                    }

                    SyncOperation.UPDATE -> {
                        val remoteNote = if (localEntitySnapshot.remoteId == null) {
                            // No remoteId? Promote to create.
                            localEntitySnapshot.toRemoteDto()
                        } else {
                            localEntitySnapshot.toRemoteDto()
                                    .copy(id = localEntitySnapshot.remoteId)
                        }

                        val saved = if (localEntitySnapshot.remoteId == null) {
                            remoteNoteWriter.create(token, email, remoteNote)
                        } else {
                            remoteNoteWriter.update(token, email, remoteNote)
                        }

                        noteDao.upsert(
                                saved.toEntity()
                                        .copy(id = localEntitySnapshot.id)
                        )
                        syncDao.deleteByIds(listOf(rec.id))
                    }

                    SyncOperation.DELETE -> {

                        val remoteId = localEntitySnapshot.remoteId
                        if (remoteId != null) {

                            // delete remotely if has remoteId
                            remoteNoteWriter.delete(token, email, remoteId)
                        }

                        // remove locally now that server delete succeeded
                        noteDao.deleteById(localEntitySnapshot.id)
                        syncDao.deleteByIds(listOf(rec.id))

                    }
                }
            }

            // === 2) DOWNLOAD FULL SNAPSHOT & RECONCILE ===
            val remoteNotesList = remoteNoteWriter.getAll(token, email)

            // upsert all server items locally
            if (remoteNotesList.isNotEmpty()) {
                val toUpsert = remoteNotesList.map { remoteNote ->
                    val localId = noteDao.findIdByRemoteId(remoteNote.id) ?: 0L
                    remoteNote.toEntity()
                            .copy(id = localId)      // preserve existing local id
                }
                noteDao.upsertAll(toUpsert)

            }

            // delete locals that vanished on server (but keep local-only notes without remote_id still queued)
            val serverIds = remoteNotesList.map { it.id }
                    .toSet()
            noteDao.deleteMissingRemoteIds(serverIds)
            dataStore.saveLastSyncTimeInMillis(System.currentTimeMillis())
            Result.success()
        } catch (e: IOException) {

            Timber.tag("SyncWorker")
                    .i("IO Exception - ${e.message}")
            // network/timeouts/transient -> retry with backoff
            Result.retry()
        } catch (e: Exception) {

            Timber.tag("SyncWorker")
                    .i("Other Exception - ${e.message}")
            // unexpected error -> fail and surface in WorkManager
            Result.failure()
        }
    }
*/

    /* === 2) DOWNLOAD FULL SNAPSHOT & RECONCILE === */
    private suspend fun downloadQueue(
        token: String,
        email: String
    ) {
        val remoteNotes = remoteNoteWriter.getAll(token, email)
        Timber.tag("SyncWorker").i("Size: ${remoteNotes.size}")

        if (remoteNotes.isNotEmpty()) {
            val toUpsert = remoteNotes.map { rn ->
                val existingLocalId = noteDao.findIdByRemoteId(rn.id) ?: 0L
                rn.toEntity()
                        .copy(id = existingLocalId) // preserve local id if present
            }
            noteDao.upsertAll(toUpsert)
        }

        // delete locals that vanished remotely (keep local-only notes lacking remoteId)
        val serverIds = remoteNotes.map { it.id }
                .toSet()
        noteDao.deleteMissingRemoteIds(serverIds)
    }

    /* === 1) UPLOAD QUEUE === */
    private suspend fun uploadQueue(
        token: String,
        email: String,
        userId: String,
        limit: Int = 100
    ) {
        val batch = syncDao.loadBatch(userId = userId, limit = limit)
        for (rec in batch) {
            val localEntitySnapshot: NoteEntity =
                jsonSerializer.fromJson(NoteEntity.serializer(), rec.payload)

            when (rec.operation) {
                SyncOperation.CREATE -> handleCreate(token, email, localEntitySnapshot, rec.id)
                SyncOperation.UPDATE -> handleUpdate(token, email, localEntitySnapshot, rec.id)
                SyncOperation.DELETE -> handleDelete(token, email, localEntitySnapshot, rec.id)
            }
        }
    }

    private suspend fun handleCreate(
        token: String,
        email: String,
        local: NoteEntity,
        recordId: String
    ) {
        val remote = remoteNoteWriter.create(token, email, local.toRemoteDto())
        noteDao.upsert(
                remote.toEntity()
                        .copy(id = local.id)
        )  // keep local id
        syncDao.deleteByIds(listOf(recordId))
    }

    private suspend fun handleUpdate(
        token: String,
        email: String,
        local: NoteEntity,
        recordId: String
    ) {
        val remoteDto = if (local.remoteId == null) {
            local.toRemoteDto()
        } else {
            local.toRemoteDto()
                    .copy(id = local.remoteId)
        }

        val saved = if (local.remoteId == null) {
            remoteNoteWriter.create(token, email, remoteDto)
        } else {
            remoteNoteWriter.update(token, email, remoteDto)
        }

        noteDao.upsert(
                saved.toEntity()
                        .copy(id = local.id)
        )
        syncDao.deleteByIds(listOf(recordId))
    }

    private suspend fun handleDelete(
        token: String,
        email: String,
        local: NoteEntity,
        recordId: String
    ) {
        local.remoteId?.let { remoteNoteWriter.delete(token, email, it) }
        noteDao.deleteById(local.id)
        syncDao.deleteByIds(listOf(recordId))
    }

}


