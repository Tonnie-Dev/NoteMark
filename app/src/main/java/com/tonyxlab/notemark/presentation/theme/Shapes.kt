package com.tonyxlab.notemark.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.tonyxlab.notemark.presentation.core.utils.spacing

val customMaterialShapes = Shapes(
        // Pre-Defined M3 Shapes
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp),

       // Extra-Large Override
        extraLarge = RoundedCornerShape(20.dp)

)

@Composable
fun getClippingShape(): RoundedCornerShape{

    return RoundedCornerShape(
            topStart = MaterialTheme.spacing.spaceTen * 2,
            topEnd = MaterialTheme.spacing.spaceTen * 2
    )
}