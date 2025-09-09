package com.tonyxlab.notemark.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.tonyxlab.notemark.data.local.database.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao : BaseDao<NoteEntity> {

    @Query("SELECT * FROM notes_table WHERE id =:id")
    suspend fun         getNoteById(id: Long): NoteEntity?
    @Query("SELECT * FROM notes_table WHERE remote_id = :remoteId")
    suspend fun getByRemoteId(remoteId: String): NoteEntity?
    @Query("SELECT id FROM notes_table WHERE remote_id = :remoteId LIMIT 1")
    suspend fun findIdByRemoteId(remoteId: String): Long?
    @Query("SELECT * FROM notes_table ORDER BY created_on DESC")
    fun getAllNotes(): Flow<List<NoteEntity>?>

    @Query("DELETE FROM notes_table")
    suspend fun clearAllNotes()

    @Query("DELETE FROM notes_table WHERE id = :id")
    suspend fun deleteById(id: Long):Int

    @Query(
            """
        DELETE FROM notes_table
        WHERE remote_id IS NOT NULL
          AND remote_id NOT IN (:serverIds)
          AND id NOT IN (SELECT CAST(noteId AS INTEGER) FROM sync_record)
    """
    )
    suspend fun deleteMissingRemoteIds(serverIds: Set<String>)

   @Query("""
       UPDATE notes_table
       SET is_deleted = :isDeleted,
       last_edited_on = :lastEditedOn
       WHERE id = :id
   """
   )
    suspend fun markDeleted(id: Long, isDeleted: Boolean, lastEditedOn: Long)

    @Query("SELECT * FROM notes_table WHERE is_deleted = 0 ORDER BY last_edited_on DESC")
    fun getActiveNotes(): Flow<List<NoteEntity>>
}