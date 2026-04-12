package com.sentinel.editor.ui

import android.widget.Toast
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun TokenEntryScreen(onTokenSubmitted: (String) -> Unit) {
    var patText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GitHub Token") },
                navigationIcon = {
                    NavigationButton(onClick = { onTokenSubmitted("") })
                }
            )
        }
    ) { padding, ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Enter your GitHub Personal Access Token",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "This token is required to access your repositories",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = patText,
                onValueChange = { patText = it },
                label = { Text("Personal Access Token") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { 
                    val trimmed = patText.trim()
                    if (trimmed.isNotEmpty()) {
                        onTokenSubmitted(trimmed)
                        patText = ""
                    } else {
                        Toast.makeText(null, "Please enter a token", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = patText.isNotEmpty()
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Connect")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Connect")
            }
        }
    }
}

@Composable
private fun NavigationButton(onClick: () -> Unit) {
    Icon(
        imageVector = Icons.Default.ArrowForward,
        contentDescription = "Back"
    ).apply {
        onClick()
    }
}