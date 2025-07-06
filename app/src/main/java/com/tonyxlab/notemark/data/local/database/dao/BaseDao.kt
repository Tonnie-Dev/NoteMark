package com.tonyxlab.notemark.data.local.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Upsert

interface BaseDao<T> {
    @Upsert
    suspend fun upsert(value:T)

    @Delete
    suspend fun delete(value: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: T)
}