@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.login.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.util.DeviceType
import com.tonyxlab.notemark.util.generateLoremIpsum
import com.tonyxlab.notemark.util.toNoteDate
import com.tonyxlab.notemark.util.trimContentText
import java.time.LocalDateTime

@Composable
fun NotePreview(
    noteItem: NoteItem,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    deviceType: DeviceType = DeviceType.MOBILE_PORTRAIT
) {
    Box(
            modifier = modifier
                    .clip(MaterialTheme.shapes.medium)
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                                onTap = { onEvent(HomeUiEvent.ClickNote(noteItem.id)) },
                                onLongPress = { onEvent(HomeUiEvent.LongPressNote(noteItem.id)) }
                        )
                    }
    ) {

        Surface(color = MaterialTheme.colorScheme.background) {

            Column(modifier = Modifier.padding(MaterialTheme.spacing.spaceMedium)) {

                Text(
                        modifier = Modifier.padding(bottom = MaterialTheme.spacing.spaceSmall),
                        text = noteItem.createdOn.toNoteDate(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                )

                Text(
                        modifier = Modifier.padding(bottom = MaterialTheme.spacing.spaceExtraSmall),
                        text = noteItem.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                )

                Text(
                        modifier = Modifier.padding(bottom = MaterialTheme.spacing.spaceExtraSmall),
                        text = trimContentText(deviceType, noteItem.content),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        overflow = TextOverflow.Ellipsis
                )

            }
        }
    }

}

@PreviewLightDark
@Composable
private fun NotePreview_Preview() {

    val now = LocalDateTime.now()
    val pastYearDate = now.minusYears(1)

    val note = NoteItem(
            content = generateLoremIpsum(20),
            createdOn = pastYearDate,
            lastEditedOn = now
    )

    NoteMarkTheme {

        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)) {

                NotePreview(
                        modifier = Modifier.weight(1f),
                        noteItem = getNote(2),
                        onEvent = {}
                )

                NotePreview(
                        modifier = Modifier.weight(1f),
                        noteItem = note,
                        onEvent = {}
                )
            }
        }
    }
}


fun getNote(id: Long): NoteItem {
    val now = LocalDateTime.now()
    return NoteItem(
            id = id,
            content = generateLoremIpsum(10),
            createdOn = now,
            lastEditedOn = now
    )
}

fun getNotes(count: Int): List<NoteItem> {

    return buildList {

        repeat(count) { i ->
            add(getNote(i.toLong()))
        }
    }
}
