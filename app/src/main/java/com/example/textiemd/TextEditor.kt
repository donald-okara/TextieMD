import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor


/**
Be sure that you have those two dependencies:
// Rich Text Editor
implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-beta03")
// Extension Icons
implementation("androidx.compose.material:material-icons-extended:1.5.3")
 */
@Composable
fun MainScreen() {
    val state = rememberRichTextState()
    val titleSize = MaterialTheme.typography.displaySmall.fontSize
    val subtitleSize = MaterialTheme.typography.titleLarge.fontSize
    var expanded by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeadLine()

                RichTextEditor(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .background(Color.Transparent) // Make the background transparent
                        .border(0.dp, Color.Transparent) // Remove any border
                        .padding(bottom = 50.dp),
                    state = state,
                    placeholder = { Text(text = "Tell your story") }
                )
            }

            EditorControls(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                state = state,
                onBoldClick = {
                    state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                },
                onItalicClick = {
                    state.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
                },
                onUnderlineClick = {
                    state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                },
                onTitleClick = {
                    state.toggleSpanStyle(SpanStyle(fontSize = titleSize))
                },
                onSubtitleClick = {
                    state.toggleSpanStyle(SpanStyle(fontSize = subtitleSize))
                },
                onTextColorClick = {
                    state.toggleSpanStyle(SpanStyle(color = Color.Red))
                },
                onStartAlignClick = {
                    state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Start))
                },
                onEndAlignClick = {
                    state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.End))
                },
                onCenterAlignClick = {
                    state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center))
                },
                onExportClick = {
                    Log.d("Editor", state.toHtml())
                }
            )


        }
    }
}


@Composable
fun HeadLine(
    modifier: Modifier = Modifier,
    placeholder: String = "Enter headline here..."
) {
    var textState by remember { mutableStateOf(TextFieldValue()) }

    Box(modifier = modifier) {
        // Placeholder text
        if (textState.text.isEmpty()) {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.headlineLarge.copy(
                    textAlign = TextAlign.Start,
                    color = Color.Gray
                ),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp) // Same padding as BasicTextField
            )
        }

        BasicTextField(
            value = textState,
            onValueChange = { newValue -> textState = newValue },
            textStyle = MaterialTheme.typography.headlineLarge.copy(
                textAlign = TextAlign.Start
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Same padding as placeholder
        )
    }
}


@Composable
fun LinkDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String) -> Unit
) {
    var linkText by remember { mutableStateOf(TextFieldValue()) }
    var linkUrl by remember { mutableStateOf(TextFieldValue()) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Add Link") },
        text = {
            Column {
                Text("Link Text")
                OutlinedTextField(
                    value = linkText,
                    onValueChange = { linkText = it },
                    placeholder = { Text("Enter display text") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Link URL")
                OutlinedTextField(
                    value = linkUrl,
                    onValueChange = { linkUrl = it },
                    placeholder = { Text("Enter URL") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (linkText.text.isNotBlank() && linkUrl.text.isNotBlank()) {
                        onConfirmation(linkText.text, linkUrl.text)
                    }
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditorControls(
    modifier: Modifier = Modifier,
    state: RichTextState,
    onBoldClick: () -> Unit,
    onItalicClick: () -> Unit,
    onUnderlineClick: () -> Unit,
    onTitleClick: () -> Unit,
    onSubtitleClick: () -> Unit,
    onTextColorClick: () -> Unit,
    onStartAlignClick: () -> Unit,
    onEndAlignClick: () -> Unit,
    onCenterAlignClick: () -> Unit,
    onExportClick: () -> Unit,
) {
    var boldSelected by rememberSaveable { mutableStateOf(false) }
    var italicSelected by rememberSaveable { mutableStateOf(false) }
    var underlineSelected by rememberSaveable { mutableStateOf(false) }
    var titleSelected by rememberSaveable { mutableStateOf(false) }
    var subtitleSelected by rememberSaveable { mutableStateOf(false) }
    var textColorSelected by rememberSaveable { mutableStateOf(false) }
    var linkSelected by rememberSaveable { mutableStateOf(false) }
    var alignmentSelected by rememberSaveable { mutableIntStateOf(0) }

    var showLinkDialog by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }

    AnimatedVisibility(visible = showLinkDialog) {
        LinkDialog(
            onDismissRequest = {
                showLinkDialog = false
                linkSelected = false
            },
            onConfirmation = { linkText, link ->
                state.addLink(
                    text = linkText,
                    url = link
                )
                showLinkDialog = false
                linkSelected = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ){
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp)
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ControlWrapper(
                    selected = boldSelected,
                    onChangeClick = { boldSelected = it },
                    onClick = onBoldClick
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatBold,
                        contentDescription = "Bold Control",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                ControlWrapper(
                    selected = italicSelected,
                    onChangeClick = { italicSelected = it },
                    onClick = onItalicClick
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatItalic,
                        contentDescription = "Italic Control",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                ControlWrapper(
                    selected = underlineSelected,
                    onChangeClick = { underlineSelected = it },
                    onClick = onUnderlineClick
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatUnderlined,
                        contentDescription = "Underline Control",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                ControlWrapper(
                    selected = titleSelected,
                    onChangeClick = { titleSelected = it },
                    onClick = onTitleClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Title,
                        contentDescription = "Title Control",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                ControlWrapper(
                    selected = subtitleSelected,
                    onChangeClick = { subtitleSelected = it },
                    onClick = onSubtitleClick
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatSize,
                        contentDescription = "Subtitle Control",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                ControlWrapper(
                    selected = textColorSelected,
                    onChangeClick = { textColorSelected = it },
                    onClick = onTextColorClick
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatColorText,
                        contentDescription = "Text Color Control",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                ControlWrapper(
                    selected = linkSelected,
                    onChangeClick = { linkSelected = it },
                    onClick = { showLinkDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddLink,
                        contentDescription = "Link Control",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                ControlWrapper(
                    selected = alignmentSelected == 0,
                    onChangeClick = { alignmentSelected = 0 },
                    onClick = onStartAlignClick
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatAlignLeft,
                        contentDescription = "Start Align Control",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                ControlWrapper(
                    selected = alignmentSelected == 1,
                    onChangeClick = { alignmentSelected = 1 },
                    onClick = onCenterAlignClick
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatAlignCenter,
                        contentDescription = "Center Align Control",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                ControlWrapper(
                    selected = alignmentSelected == 2,
                    onChangeClick = { alignmentSelected = 2 },
                    onClick = onEndAlignClick
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatAlignRight,
                        contentDescription = "End Align Control",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                ControlWrapper(
                    selected = true,
                    selectedColor = MaterialTheme.colorScheme.tertiary,
                    onChangeClick = { },
                    onClick = onExportClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Export Control",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.size(56.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ){
            Icon(
                imageVector = if (expanded) Icons.Default.Close else Icons.Default.Edit,
                contentDescription = if (expanded) "Collapse Controls" else "Expand Controls"
            )
        }
    }

}

@Composable
fun ControlWrapper(
    selected: Boolean,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = MaterialTheme.colorScheme.inversePrimary,
    onChangeClick: (Boolean) -> Unit,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(size = 6.dp))
            .clickable {
                onClick()
                onChangeClick(!selected)
            }
            .background(
                if (selected) selectedColor
                else unselectedColor
            )
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(size = 6.dp)
            )
            .padding(all = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}