package com.tonyxlab.notemark.domain.model

import com.tonyxlab.notemark.util.Constants
import java.time.LocalDateTime

data class NoteItem(
    val id: Long = Constants.INITIAL_DATABASE_ID,
    val title: String = "New Note",
    val content: String = "",
    val createdOn: LocalDateTime,
)