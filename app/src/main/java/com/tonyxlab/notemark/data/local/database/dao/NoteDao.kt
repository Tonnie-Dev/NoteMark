package com.tonyxlab.notemark.data.local.database.dao

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Dao
import androidx.room.Query
import com.tonyxlab.notemark.data.local.database.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao: BaseDao<NoteEntity> {

    @Query("SELECT * FROM notes_table WHERE id =:id")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Query("SELECT * FROM notes_table ORDER BY created_on DESC")
    fun getAllNotes(): Flow<List<NoteEntity>?>

    @Query("DELETE FROM notes_table")
    suspend fun clearAllNotes()


    @Query("""
        DELETE FROM notes_table
        WHERE remote_id IS NOT NULL
          AND remote_id NOT IN (:serverIds)
          AND id NOT IN (SELECT CAST(noteId AS INTEGER) FROM sync_record)
    """)
    suspend fun deleteMissingRemoteIds(serverIds: Set<String>)
}