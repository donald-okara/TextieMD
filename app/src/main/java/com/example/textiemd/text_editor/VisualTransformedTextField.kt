package com.example.textiemd.text_editor

import TextEditorVisualTransformer
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.rounded.FormatBold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VisualTransformedTextField(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var headline by remember { mutableStateOf(TextFieldValue("")) }
    val visualTransformation = remember { TextEditorVisualTransformer() }
    var isFabExpanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = BringIntoViewRequester()

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Box {
                if (headline.text.isEmpty()) {
                    Text(
                        text = "Enter Headline here...",
                        style = MaterialTheme.typography.headlineMedium.copy(color = Color.Gray)
                            .copy(
                                fontWeight = FontWeight.Bold,
                            ),
                        modifier = Modifier.drawBehind {
                            val lineThickness = 4.dp.toPx() // Adjust thickness as needed
                            val yPosition = size.height - lineThickness / 2
                            drawLine(
                                color = Color.Black, // Or any color you prefer
                                start = Offset(0f, yPosition), end = Offset(size.width, yPosition),
                                strokeWidth = lineThickness
                            )
                        }
                    )
                }

                BasicTextField(
                    value = headline,
                    onValueChange = { headline = it },
                    visualTransformation = visualTransformation,
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.drawBehind {
                        val lineThickness = 4.dp.toPx() // Adjust thickness as needed
                        val yPosition = size.height - lineThickness / 2
                        drawLine(
                            color = Color.Black, // Or any color you prefer
                            start = Offset(0f, yPosition), end = Offset(size.width, yPosition),
                            strokeWidth = lineThickness
                        )
                    }
                )
            }

            Spacer(modifier = modifier.padding(16.dp))

            PhotoPicker()

            Spacer(modifier = modifier.padding(16.dp))

            Box {
                if (text.text.isEmpty()) {
                    Text(
                        text = "Hint: Text is in markdown format...",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }

                BasicTextField(
                    modifier = modifier
                        .fillMaxSize()
                        .onFocusEvent {
                            if (it.isFocused || it.hasFocus) {
                                coroutineScope.launch {
                                    bringIntoViewRequester.bringIntoView()
                                }
                            }
                        },
                    value = text,
                    onValueChange = { text = it },
                    visualTransformation = visualTransformation,
                    textStyle = MaterialTheme.typography.bodyLarge
                )


            }

            //Spacer(modifier = modifier.padding(16.dp))

        }

        // Format Controls
        // Floating Action Button with formatting controls
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .bringIntoViewRequester(bringIntoViewRequester)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Animated Visibility for Formatting Controls
                AnimatedVisibility(
                    visible = isFabExpanded,
                    enter = fadeIn(animationSpec = tween(durationMillis = 600)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 600))
                ) {
                    FormatControls(
                        text = text,
                        onTextChange = { text = it }
                    )
                }

                // Floating Action Button
                // Rotation animation
                val rotationAngle by animateFloatAsState(
                    targetValue = if (isFabExpanded) 180f else 0f,
                    animationSpec = tween(durationMillis = 300), label = ""
                )

                // Scaling animation
                val scale by animateFloatAsState(
                    targetValue = if (isFabExpanded) 1.2f else 1f,
                    animationSpec = tween(durationMillis = 300), label = ""
                )

                FloatingActionButton(
                    onClick = { isFabExpanded = !isFabExpanded },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .graphicsLayer(
                            rotationZ = rotationAngle,
                            scaleX = scale,
                            scaleY = scale
                        )
                ) {
                    Icon(
                        imageVector = if (isFabExpanded) Icons.Default.Close else Icons.Default.FormatSize,
                        contentDescription = "Toggle Formatting Controls",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

            }
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
        ControlWrapper(
            isSelected = false,
            onClick = { multiplePhotoPickerLauncher.launch("image/*") }
        ) {
            Icon(
                modifier = modifier
                    .fillMaxSize()
                    .alpha(0.5f),
                imageVector = Icons.Default.Image,
                contentDescription = "Pick an image",
                tint = LocalContentColor.current,

            )
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormatControls(modifier: Modifier = Modifier, text: TextFieldValue, onTextChange: (TextFieldValue) -> Unit) {
    val annotationsManager = remember { AnnotationsManager() }
    val scope = rememberCoroutineScope()
    val selection = text.selection
    val isBold = annotationsManager.isBold(text.text, selection.start, selection.end)
    val isItalics = annotationsManager.isItalics(text.text, selection.start, selection.end)


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )  {
        ControlWrapper(
            isSelected = isBold,
            onClick = {
                scope.launch(Dispatchers.Default) {
                    val updatedText = annotationsManager.applyBold(
                        text = text.text,
                        selectionStart = selection.start,
                        selectionEnd = selection.end
                    )
                    scope.launch(Dispatchers.Main) {
                        onTextChange(text.copy(text = updatedText))
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.FormatBold,
                contentDescription = "Bold",
                tint = if (isBold) MaterialTheme.colorScheme.onTertiaryContainer else LocalContentColor.current
            )
        }


        ControlWrapper(
            isSelected = isItalics,
            onClick = {
                scope.launch(Dispatchers.Default) {
                    val updatedText = annotationsManager.applyItalics(
                        text = text.text,
                        selectionStart = selection.start,
                        selectionEnd = selection.end
                    )
                    scope.launch(Dispatchers.Main) {
                        onTextChange(text.copy(text = updatedText))
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.FormatItalic,
                contentDescription = "Italics",
                tint = if (isBold) MaterialTheme.colorScheme.onTertiaryContainer else LocalContentColor.current
            )
        }
    }


}


@Composable
fun ControlWrapper(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(48.dp)
            .clickable { onClick() }
            .background(
                if (isSelected) MaterialTheme.colorScheme.tertiaryContainer else Color.Transparent,
            )
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = CircleShape
            )
            .padding(all = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}