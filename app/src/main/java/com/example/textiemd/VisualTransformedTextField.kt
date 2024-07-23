package com.example.textiemd

import TextEditorVisualTransformer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle

@Composable
fun VisualTransformedTextField() {
    var text by remember { mutableStateOf("") }
    val visualTransformation = remember { TextEditorVisualTransformer() }

    Box {
        if (text.isEmpty()) {
            Text(
                text = "Enter text here...",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
            )
        }

        Column {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                visualTransformation = visualTransformation
            )
        }



    }
}
