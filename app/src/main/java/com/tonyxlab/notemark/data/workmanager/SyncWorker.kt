@file:RequiresApi(Build.VERSION_CODES.O)

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
import com.tonyxlab.notemark.data.remote.sync.client.RemoteNoteWriter
import com.tonyxlab.notemark.data.remote.sync.dto.RemoteNoteDto
import com.tonyxlab.notemark.data.remote.sync.dto.toEntity
import com.tonyxlab.notemark.data.remote.sync.dto.toRemoteDto
import com.tonyxlab.notemark.data.remote.sync.entity.SyncOperation
import com.tonyxlab.notemark.domain.json.JsonSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val syncDao: SyncDao,
    private val noteDao: NoteDao,
    private val dataStore: DataStore,
    private val jsonSerializer: JsonSerializer,
    private val remoteNoteWriter: RemoteNoteWriter
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(context = Dispatchers.IO) {

        val email = "vontonnie@gmail.com"

        val token = dataStore.getAccessToken()

        if (token.isNullOrEmpty()) {

            return@withContext Result.success()
        }

        val userId = dataStore.getOrCreateInternalUserId()



        try {
            // === 1) UPLOAD QUEUE (record-by-record) ===
            val batch = syncDao.loadBatch(userId = userId, limit = 100)


            for (rec in batch) {
                val localNote: NoteEntity =
                    jsonSerializer.fromJson(NoteEntity.serializer(), rec.payload)

                when (rec.operation) {
                    SyncOperation.CREATE -> {

                        val remote = remoteNoteWriter.create(token, email, localNote.toRemoteDto())
                        // Write back server id + timestamps, keep local id for continuity
                        noteDao.upsert(
                                remote.toEntity()
                                        .copy(id = localNote.id)
                        )
                        syncDao.deleteByIds(listOf(rec.id))
                    }

                    /*SyncOperation.UPDATE -> {
                        val body = if (localNote.remoteId == null) {
                            // No remoteId? Promote to create.
                            localNote.toRemoteDto()
                        } else {
                            localNote.toRemoteDto()
                                    .copy(id = localNote.remoteId)
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
                        syncDao.deleteByIds(listOf(rec.id))
                    }*/
                    SyncOperation.UPDATE,
                    SyncOperation.DELETE -> {

                        val localNote: NoteEntity =
                            jsonSerializer.fromJson(
                                    serializer = NoteEntity.serializer(),
                                    json = rec.payload
                            )

                        val body = localNote.toRemoteDto()
                                .copy(
                                        id = localNote.remoteId ?: localNote.toRemoteDto().id
                                )
                        val uploadedNote = if (localNote.remoteId == null){
                            remoteNoteWriter.create(token, email, body)
                        }else {
                            remoteNoteWriter.update(token,email,body)
                        }

                        noteDao.upsert(uploadedNote.toEntity().copy(id = localNote.id))
                        syncDao.deleteByIds(listOf(rec.id))
                        /*   val remoteId = localNote.remoteId
                           if (remoteId != null) {

                               // if server call fails, throw to let WM retry
                               notesRemote.delete(token, email, remoteId)
                           }

                           // local already deleted; drop queue entry either way
                           syncDao.deleteByIds(listOf(rec.id))*/

                    }
                }
            }

            // === 2) DOWNLOAD FULL SNAPSHOT & RECONCILE(LWW) ===

            val remoteItems:List<RemoteNoteDto> = remoteNoteWriter.getAll(token,email)
            remoteItems.forEach { applyRemoteNoteLww(it) }
            // === 2) DOWNLOAD FULL SNAPSHOT & RECONCILE ===
         /*   val remoteNoteDtos: List<RemoteNoteDto> = remoteNoteWriter.getAll(token, email)


            // upsert all server items locally
            if (remoteNoteDtos.isNotEmpty()) {
                val toUpsert = remoteNoteDtos.map { rn ->
                    val localId = noteDao.findIdByRemoteId(rn.id) ?: 0L
                    rn.toEntity()
                            .copy(id = localId)      // preserve existing local id
                }
                noteDao.upsertAll(toUpsert)

            }

            // delete locals that vanished on server (but keep local-only notes still queued)
            val serverIds = remoteNoteDtos.map { it.id }
                    .toSet()
            noteDao.deleteMissingRemoteIds(serverIds)
            dataStore.saveLastSyncTimeInMillis(System.currentTimeMillis())*/
            Result.success()
        } catch (e: IOException) {

            // network/timeouts/transient -> retry with backoff
            Result.retry()
        } catch (e: Exception) {

            // unexpected error -> fail and surface in WorkManager
            Result.failure()
        }
    }

    suspend fun applyRemoteNoteLww(remote: RemoteNoteDto) {
        val entity = remote.toEntity() // also maps isDeleted + timestamps
        val local = entity.remoteId?.let { noteDao.getByRemoteId(it) }

        if (local == null) {
            // New to us → save as-is (tombstone stays tombstone)
            noteDao.upsert(entity.copy(id = 0L))
            return
        }

        val serverNewer = entity.lastEditedOn > local.lastEditedOn
        if (serverNewer) {
            // Server wins → overwrite and drop any stale local queue for this note
            noteDao.upsert(entity.copy(id = local.id))
            syncDao.deleteByNoteId(local.id.toString())
        }
        // else local wins → do nothing (we’ll eventually push our newer local state)
    }

}


