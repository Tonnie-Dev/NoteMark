package com.tonyxlab.notemark.data.local.database.entity

import android.R.attr.data
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "notes_table")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "remote_id")
    val remoteId:String? = null,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "created_on")
    val createdOn: Long,
    @ColumnInfo(name = "last_edited_on")
    val lastEditedOn: Long
)