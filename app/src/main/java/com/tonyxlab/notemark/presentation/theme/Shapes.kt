package com.tonyxlab.notemark.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val customShapes = Shapes(
        // Pre-Defined M3 Shapes
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp),

       // Extra-Large Override
        extraLarge = RoundedCornerShape(20.dp)

)