package com.example.textiemd

import TextEditorVisualTransformer
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun rememberPhotoPicker(
    maxSelectionCount: Int = 1
): Pair<() -> Unit, List<Uri?>> {
    var selectedImages by remember { mutableStateOf<List<Uri?>>(emptyList()) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImages = listOf(uri) }
    )

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = if (maxSelectionCount > 1) maxSelectionCount else 2),
        onResult = { uris -> selectedImages = uris }
    )

    fun launchPhotoPicker() {
        if (maxSelectionCount > 1) {
            multiplePhotoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            singlePhotoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    return Pair(::launchPhotoPicker, selectedImages)
}

@Composable
fun ImageLayoutView(selectedImages: List<Uri?>) {
    LazyRow {
        items(selectedImages) { uri ->
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier.size(500.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}


@Composable
fun PhotoSelectorWithTextField() {
    val (launchPhotoPicker, selectedImages) = rememberPhotoPicker(maxSelectionCount = 3)
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = launchPhotoPicker) {
            Text("Select Photos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                decorationBox = { innerTextField ->
                    Column {
                        if (selectedImages.isNotEmpty()) {
                            ImageLayoutView(selectedImages)
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoSelectorWithTextFieldPreview() {
    PhotoSelectorWithTextField()
}