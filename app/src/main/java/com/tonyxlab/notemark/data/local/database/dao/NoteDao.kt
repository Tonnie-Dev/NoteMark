package com.tonyxlab.notemark.data.local.database.dao

import androidx.room.Query
import com.tonyxlab.notemark.data.local.database.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteDao: BaseDao<NoteEntity> {

    @Query("SELECT * FROM notes_table WHERE id =:id")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Query("SELECT * FROM notes_table ORDER BY created_on DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

}