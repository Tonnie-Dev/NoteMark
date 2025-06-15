package com.tonyxlab.notemark.presentation.core.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tonyxlab.notemark.presentation.core.utils.spacing


@Composable
fun Header(@StringRes title: Int, @StringRes subTitle: Int, modifier: Modifier = Modifier) {
    Column (modifier = modifier){
        AppTitleText(text = stringResource(id = title))
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSingleDp * 6))
        AppCaptionText(text = stringResource(id = subTitle))
    }
}

