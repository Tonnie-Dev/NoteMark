package com.tonyxlab.notemark.presentation.core.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.tonyxlab.notemark.presentation.core.utils.spacing

@Composable
fun Header(
    @StringRes title: Int,
    @StringRes subTitle: Int,
    modifier: Modifier = Modifier,
    textAlignment: TextAlign = TextAlign.Start
) {
    Column(modifier = modifier) {
        AppTitleText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = title),
                textAlign = textAlignment
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSingleDp * 6))
        AppCaptionText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = subTitle),
                textAlign = textAlignment
        )
    }
}

