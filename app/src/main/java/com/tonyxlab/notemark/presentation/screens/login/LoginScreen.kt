package com.tonyxlab.notemark.presentation.screens.login



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tonyxlab.notemark.navigation.NavOperations

@Composable
fun LoginScreen(modifier: Modifier = Modifier, navOperations: NavOperations) {


    Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {


        Text(text = "Login Screen", style = MaterialTheme.typography.titleLarge)
    }
}