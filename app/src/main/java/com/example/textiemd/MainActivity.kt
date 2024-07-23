package com.example.textiemd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.textiemd.text_editor.VisualTransformedTextField
import com.example.textiemd.ui.theme.TextieMDTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextieMDTheme {
                VisualTransformedTextField()
            }
        }
    }
}