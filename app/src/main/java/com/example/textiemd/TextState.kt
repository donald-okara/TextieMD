package com.example.textiemd

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue

data class TextState(
    var textFieldValue: TextFieldValue = TextFieldValue(),
    var bold: Boolean = false,
    var italic: Boolean = false,
    var underline: Boolean = false,
    var header: Boolean = false,
    var color: Color = Color.Black
)
