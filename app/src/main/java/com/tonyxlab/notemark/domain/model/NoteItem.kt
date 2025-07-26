package com.tonyxlab.notemark.domain.model

import com.tonyxlab.notemark.util.Constants
import com.tonyxlab.notemark.util.Constants.DEFAULT_TITLE_VALUE
import java.time.LocalDateTime

data class NoteItem(
    val id: Long = Constants.INITIAL_DATABASE_ID,
    val title: String = DEFAULT_TITLE_VALUE,
    val content: String = "",
    val createdOn: LocalDateTime,
        val lastEditedOn: LocalDateTime
)

fun NoteItem.isBlankNote(): Boolean = title == DEFAULT_TITLE_VALUE && content.isBlank()