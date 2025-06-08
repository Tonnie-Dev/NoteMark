package com.tonyxlab.notemark.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.tonyxlab.notemark.presentation.core.utils.AppTextFieldStyle
import com.tonyxlab.notemark.presentation.core.utils.spacing


@Composable
private fun TextFieldDecorator(
    placeholderString: String,
    isTextEmpty: Boolean,
    style: AppTextFieldStyle,
    innerTextField: @Composable () -> Unit,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {

    Row(
            modifier = modifier
                    .fillMaxSize()
                    .background(
                            color = style.backgroundColor(),
                            shape = MaterialTheme.shapes.medium
                    )
                    .border(
                            width = style.borderWidth(),
                            color = style.borderColor(),
                            shape = MaterialTheme.shapes.medium
                    )
                    .padding(vertical = MaterialTheme.spacing.spaceMedium)
                    .padding(end = MaterialTheme.spacing.spaceMedium)
                    .padding(start = MaterialTheme.spacing.spaceMedium)
    ) {


        Box {
            if (isTextEmpty) {


                Text(
                        text = placeholderString,
                        style = AppTextFieldStyle.PlaceHolderStyle.textStyle()
                )
            }
            innerTextField
        }

        icon?.let {

            Icon(
                    modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) { onIconClick() },
                    imageVector = icon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}