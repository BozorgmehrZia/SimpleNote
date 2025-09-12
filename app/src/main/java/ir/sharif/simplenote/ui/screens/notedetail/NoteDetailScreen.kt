package ir.sharif.simplenote.ui.screens.notedetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.sharif.simplenote.ui.AppViewModelFactory
import ir.sharif.simplenote.ui.components.ConfirmationDialog
import ir.sharif.simplenote.ui.theme.SimpleNoteTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    viewModel: NoteDetailViewModel, // Injected via factory
    onNavigateBack: () -> Unit
) {
    val title = viewModel.noteTitle
    val content = viewModel.noteContent
    val lastUpdatedTimestamp = viewModel.lastUpdatedTimestamp
    
    // State for showing delete confirmation dialog
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // Trigger save when the screen is disposed (navigated away)
    // This is a simple way to auto-save. More robust would be on every change with debounce.
    // ViewModel already handles debounce with onTitleChange/onContentChange

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (viewModel.noteId == null) "Add Note" else "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                val formattedDate = remember(lastUpdatedTimestamp) {
                    SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(Date(lastUpdatedTimestamp))
                }
                Text(
                    text = "Last updated: $formattedDate",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Only show delete button for existing notes (not new notes)
                if (viewModel.noteId != null) {
                    IconButton(
                        onClick = {
                            showDeleteConfirmation = true
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Note",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { viewModel.onContentChange(it) },
                placeholder = { Text("feel free to write here ...", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Takes up remaining space
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }
    }
    
    // Delete confirmation dialog
    if (showDeleteConfirmation) {
        ConfirmationDialog(
            title = "Delete Note",
            text = "Are you sure you want to delete this note? This action cannot be undone.",
            onDismiss = {
                showDeleteConfirmation = false
            },
            onConfirm = {
                viewModel.deleteNote()
                showDeleteConfirmation = false
                onNavigateBack() // Navigate back after delete
            }
        )
    }
}

// Preview needs to be updated or removed as ViewModel injection is complex for Previews without Hilt/DI framework
// @Preview(showBackground = true)
// @Composable
// fun NoteDetailScreenPreview() {
// SimpleNoteTheme {
// //      NoteDetailScreen(onNavigateBack = {}, onDeleteNote = {}) // This will no longer work directly
// }
// }
