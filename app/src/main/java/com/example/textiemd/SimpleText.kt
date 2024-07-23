package com.example.textiemd

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import java.time.format.TextStyle

@Composable
fun SimpleTextEditor(){
// State to hold the text input
    var text by remember { mutableStateOf("") }

    Column {
        Box {
            // Display the placeholder text if the input is empty
            if (text.isEmpty()) {
                Text(
                    text = "Enter text here...",
                    color = Color.Gray
                )
            }

            BasicTextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
            )
        }

        PhotoSelectorWithTextField()
    }
}