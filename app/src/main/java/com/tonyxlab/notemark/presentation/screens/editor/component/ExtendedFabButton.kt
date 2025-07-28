package com.tonyxlab.notemark.presentation.screens.editor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme

@Composable
fun ExtendedFabButton(
    onEditMode: () -> Unit,
    onReadMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
    ) {
        ExtendedFloatingActionButton(

                onClick = {},

        ){

                Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.cds_text_edit_mode)
                )

        }



        ExtendedFloatingActionButton(

                onClick = {},

                ){

            Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.cds_text_edit_mode)
            )

        }

    }
}


@PreviewLightDark
@Composable
private fun ExtendedFabButton_Preview() {


    NoteMarkTheme {
        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(MaterialTheme.spacing.spaceMedium)
        ) {

            ExtendedFabButton(onEditMode = {}, onReadMode = {})
        }

    }
}