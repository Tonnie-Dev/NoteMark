@file: RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.data.local.database.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.notemark.data.local.database.entity.NoteEntity
import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.util.localDateTimeToMillis
import com.tonyxlab.notemark.util.toLocalDateTime


fun NoteItem.toEntity(): NoteEntity {
    return NoteEntity(
            id = id,
            title = title,
            content = content,
                    createdOn = createdOn.localDateTimeToMillis()
    )
}


fun NoteEntity.toModel(): NoteItem {

    return NoteItem(

            id = id,
            title = title,
            content = content,
            createdOn = createdOn.toLocalDateTime()
    )
}