package com.tonyxlab.notemark.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tonyxlab.notemark.R

val SpaceGroteskFontFamily = FontFamily(Font(R.font.space_grotesk_variable))
val InterFontFamily = FontFamily(Font(R.font.inter_variable))

val Typography = Typography(
        titleLarge = TextStyle(
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                fontSize = 32.sp,
                lineHeight = 36.sp
        ),

        titleSmall = TextStyle(
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp,
                lineHeight = 24.sp
        ),
        bodyLarge = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
                lineHeight = 24.sp
        ),


        bodyMedium = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                lineHeight = 20.sp
        ),

        bodySmall = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                lineHeight = 20.sp
        )
)

object ExtendedTypography {

    val TitleXLarge = TextStyle(
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 40.sp
    )
}


val Typography.titleXLarge: TextStyle
@Composable
get() = ExtendedTypography.TitleXLarge


