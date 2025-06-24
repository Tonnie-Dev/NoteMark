package com.tonyxlab.notemark.presentation.core.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.presentation.theme.OnSurface12
import com.tonyxlab.notemark.util.ifThen


@Composable
fun AppButton(
    buttonText: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    isEnabled: Boolean = true,
    isOutlined: Boolean = false,
    isLoading: Boolean = false,
    enabledContainerColor: Color = MaterialTheme.colorScheme.primary,
    disabledContainerColor: Color = OnSurface12,
    outlineColor: Color = MaterialTheme.colorScheme.primary,
    buttonHeight: Dp = MaterialTheme.spacing.spaceTwelve * 4,
    shape: RoundedCornerShape = RoundedCornerShape(MaterialTheme.spacing.spaceTwelve),
    onClick: () -> Unit,
) {

    Box(
            modifier = modifier
                    .clip(shape)
                    .fillMaxWidth()
                    .height(buttonHeight)
                    .clickable { onClick() }
                    .ifThen(isOutlined) {

                        border(
                                width = MaterialTheme.spacing.spaceSingleDp,
                                color = outlineColor,
                                shape = shape
                        )
                    }
                    .ifThen(isEnabled && !isOutlined) { background(enabledContainerColor) }
                    .ifThen(isEnabled.not()) { background(disabledContainerColor) },

            contentAlignment = Alignment.Center

    ) {

        val textColor = when {
            isEnabled and !isOutlined -> MaterialTheme.colorScheme.onPrimary
            isOutlined && isEnabled -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.onSurface
        }

        if (isLoading.not()) {


            Text(text = buttonText, style = textStyle.copy(color = textColor))

        } else {

            CircularProgressIndicator(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceTwelve * 2)
                            .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onPrimary
            )
        }


    }

}


@PreviewLightDark
@Composable
private fun AppButtonPreview() {


    NoteMarkTheme {

        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {


            AppButton(buttonText = "Get Started", onClick = {})
            AppButton(buttonText = "Log in", onClick = {}, isOutlined = true)
            AppButton(buttonText = "Log in", onClick = {}, isEnabled = false)
            AppButton(buttonText = "Log In", isLoading = true) { }

        }
    }
}