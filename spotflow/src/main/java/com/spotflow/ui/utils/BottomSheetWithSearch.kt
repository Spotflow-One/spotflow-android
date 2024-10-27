package com.spotflow.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.spotflow.models.BaseModel

// BaseModel equivalent in Kotlin


// Composable for BottomSheet with Search
@Composable
fun BottomSheetWithSearch(
    items: List<BaseModel>,
    onSelect: (BaseModel) -> Unit
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var filteredItems by remember { mutableStateOf(items) }

    // Filtering logic based on search query
    LaunchedEffect(searchText) {
        val query = searchText.text.lowercase()
        filteredItems = if (query.isEmpty()) {
            items
        } else {
            items.filter { it.name.lowercase().contains(query) }
        }
    }

    // Bottom Sheet UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {

            // Search TextField
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // List of filtered items
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredItems) { item ->
                    Text(
                        item.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth().clickable {
                            onSelect(item)
                        })

                }
            }
        }
    }
}


// Usage example in a dialog, which behaves similarly to a bottom sheet
@Composable
fun ShowBottomSheetDialog(
    items: List<BaseModel>,
    onItemSelected: (BaseModel) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxHeight(0.9f),
            shape = MaterialTheme.shapes.large
        ) {
            BottomSheetWithSearch(
                items = items,
                onSelect = {
                    onItemSelected(it)
                    onDismiss() // Dismiss the dialog
                }
            )
        }
    }
}
