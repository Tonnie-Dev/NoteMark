package com.tonyxlab.notemark.presentation.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector

val Boolean.eyeIcon: ImageVector
    get() = if (this) Icons.Default.Visibility else Icons.Default.VisibilityOff
