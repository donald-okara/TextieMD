package com.example.textiemd.text_editor

import TextEditorVisualTransformer
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun VisualTransformedTextField(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    var headline by remember { mutableStateOf("") }
    val visualTransformation = remember { TextEditorVisualTransformer() }


    Column(
        modifier = modifier
            .padding(16.dp),
    ) {
        Box {
            if (headline.isEmpty()) {
                Text(
                    text = "Enter Headline here...",
                    style = MaterialTheme.typography.headlineMedium.copy(color = Color.Gray).copy(fontWeight = FontWeight.Bold)
                )
            }

            BasicTextField(
                value = headline,
                onValueChange = { headline = it },
                visualTransformation = visualTransformation,
                textStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = modifier.padding(16.dp))

        PhotoPicker()

        Spacer(modifier = modifier.padding(16.dp))

        Box {
            if (text.isEmpty()) {
                Text(
                    text = "Hint: Text is in markdown format...",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
            }

            BasicTextField(
                value = text,
                onValueChange = { text = it },
                visualTransformation = visualTransformation,
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }
    }

}

@Composable
fun PhotoPicker(
    modifier: Modifier = Modifier,
){
    var caption by remember { mutableStateOf("") }
    val visualTransformation = remember { TextEditorVisualTransformer() }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }

    // Setup the image picker launcher for multiple images
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris: List<Uri> ->
            selectedImages = selectedImages + uris
        }
    )

    if (selectedImages.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center
        ){
            Button(
                onClick = {
                    multiplePhotoPickerLauncher.launch("image/*")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = modifier.clip(CircleShape)
            ) {
                Icon(imageVector = Icons.Outlined.Image, contentDescription = "Pick an image")
            }
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(selectedImages.size) { index ->
                    Box(
                        modifier = Modifier.size(256.dp),
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(selectedImages[index])
                                    .build()
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp))
                        )
                        IconButton(
                            onClick = {
                                selectedImages =
                                    selectedImages.toMutableList().apply { removeAt(index) }
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(16.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove Image",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
            Box {
                if (caption.isEmpty()) {
                    Text(
                        text = "caption...",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }

                BasicTextField(
                    value = caption,
                    onValueChange = { caption = it },
                    visualTransformation = visualTransformation,
                    textStyle = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
