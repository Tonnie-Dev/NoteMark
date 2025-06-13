package com.tonyxlab.notemark.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.presentation.core.utils.AppTextFieldStyle
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme


@Composable
fun AppTextField(
    label: String,
    placeholderString: String,
    modifier: Modifier = Modifier,
    supportingText: String = "",
    icon: ImageVector? = null,
    isError: Boolean = false,
    textFieldState: TextFieldState,
    onIconClick: () -> Unit = {},
    isSecureText: Boolean = false,
    textFieldStyle: AppTextFieldStyle = AppTextFieldStyle.PlaceHolderStyle,
    labelTextStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {

    var isFocussed by remember { mutableStateOf(false) }

    val currentStyle = when {
        isError -> AppTextFieldStyle.ErrorTextStyle
        isFocussed -> AppTextFieldStyle.FocusedTextStyle
        else -> textFieldStyle
    }

    Column(modifier = modifier) {

        Text(
                text = label,
                style = labelTextStyle.copy(color = MaterialTheme.colorScheme.onSurface)
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))

        BasicTextField(
                modifier = Modifier.onFocusChanged { isFocussed = it.isFocused },
                state = textFieldState,
                textStyle = currentStyle.textStyle(),
                cursorBrush = SolidColor(currentStyle.cursorColor()),
                outputTransformation = if (isSecureText) {
                    OutputTransformation {
                        replace(start = 0, end = length, text = "*".repeat(length))
                    }
                } else null,
                decorator = { innerTextField ->
                    TextFieldDecorator(
                            innerTextField = innerTextField,
                            placeholderString = placeholderString,
                            isTextEmpty = textFieldState.text.isEmpty(),
                            icon = icon,
                            style = currentStyle,
                            onIconClick = onIconClick
                    )
                }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))

        Text(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = MaterialTheme.spacing.spaceMedium)
                        .padding(end = MaterialTheme.spacing.spaceMedium),
                text = supportingText,
                style = MaterialTheme.typography.bodySmall,
                color = currentStyle.supportingTextColor()
        )

    }
}


@Composable
private fun TextFieldDecorator(
    innerTextField: @Composable () -> Unit,
    placeholderString: String,
    isTextEmpty: Boolean,
    style: AppTextFieldStyle,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {

    Row(
            modifier = modifier
                    .fillMaxWidth()
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
                    .padding(start = MaterialTheme.spacing.spaceMedium),
            horizontalArrangement = Arrangement.SpaceBetween
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


@PreviewLightDark
@Composable
private fun AppTextFieldPreview() {


    NoteMarkTheme {

        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {

            val textFieldState = remember { TextFieldState() }
            AppTextField(
                    label = "Username",
                    placeholderString = "John.doe",
                    supportingText = "Use 8 + Characters",
                    textFieldState = textFieldState
            )


            AppTextField(
                    label = "Username",
                    placeholderString = "John.doe",
                    supportingText = "Use 8 + Characters",
                    textFieldState = textFieldState,
                    isError = true

            )


            AppTextField(
                    label = "Username",
                    placeholderString = "John.doe",
                    supportingText = "Use 8 + Characters",
                    textFieldState = textFieldState,
                    isSecureText = true,
                    icon = Icons.Default.Visibility

            )

        }
    }
}


