@file: RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.data.local.database.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.notemark.data.local.database.entity.NoteEntity
import com.tonyxlab.notemark.domain.model.NoteItem
import java.time.Instant
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun NoteItem.toEntity(): NoteEntity {


    return NoteEntity(
            id = id,
            title = title,
            content = content,
            createdOn = createdOn.atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
    )
}


fun NoteEntity.toModel(): NoteItem {

    return NoteItem(

            id = id,
            title = title,
            content = content,
            createdOn = Instant.ofEpochMilli(createdOn)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
    )
}