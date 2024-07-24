# TextEditor and Photo Picker in Jetpack Compose with TextieMD

## Overview

This project features a `VisualTransformedTextField` composable that integrates a rich text editor with Markdown-like syntax support (headings, bold, italics, hashtags). It also includes a `PhotoPicker` component for selecting and displaying multiple images with asynchronous loading and rounded corners.

## Features

- **Text Editor**: Supports Markdown-like syntax transformation for headings, bold, italics, and hashtags using `TextEditorVisualTransformer`.
- **Photo Picker**: Allows users to select multiple images and displays them with rounded corners. Includes a button for adding images and a remove button for each image.

## Setup

1. **Text Transformation**: Uses regex patterns to style text in `TextEditorVisualTransformer`.
2. **Image Loading**: Utilizes Coil for asynchronous image loading and applies rounded corners to images.
3. **Image Picker**: Integrated with `ActivityResultContracts.GetMultipleContents` for selecting multiple images.

## Usage

- **TextEditor**: Use `VisualTransformedTextField` to integrate a rich text editor with Markdown-like syntax support.
- **PhotoPicker**: Use `PhotoPicker` to handle image selection and display.

## Credits

- The `TextEditorVisualTransformer` implementation is inspired by [Learning to Code](https://learning-to-code.hashnode.dev/richtext-editor-in-jetpack-compose-1).

For complete code, please refer to the provided implementations.

[Video](Screen_recording_20240724_113848.webm)