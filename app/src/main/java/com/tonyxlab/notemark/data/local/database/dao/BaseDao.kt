package com.tonyxlab.notemark.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface BaseDao<T> {

    @Delete
    suspend fun delete(value: T):Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndReturnId(value: T): Long

    @Upsert
    suspend fun upsert(value: T):Long

    @Upsert
    suspend fun upsertAll(values:List<T>)

    @Query("""
        
        DELETE FROM notes_table
        WHERE remote_id IS NOT NULL
        AND
        remote_id NOT IN (:serverIds)
        AND id NOT IN (SELECT CAST (noteId AS INTEGER) FROM sync_record)
    """)

    suspend fun deleteMissingIds(serverIds:Set<String>)

}