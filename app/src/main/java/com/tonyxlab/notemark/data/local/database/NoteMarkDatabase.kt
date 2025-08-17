package com.tonyxlab.notemark.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tonyxlab.notemark.data.local.database.dao.NoteDao
import com.tonyxlab.notemark.data.local.database.entity.NoteEntity
import com.tonyxlab.notemark.data.local.sync.dao.SyncDao

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NoteMarkDatabase: RoomDatabase(){

    abstract val noteDao: NoteDao
    abstract val syncDao: SyncDao
}
