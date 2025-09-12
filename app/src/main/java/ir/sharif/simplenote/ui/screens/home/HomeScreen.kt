package ir.sharif.simplenote.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ir.sharif.simplenote.R // Assuming you have an R file for resources
import ir.sharif.simplenote.model.Note
import ir.sharif.simplenote.ui.AppViewModelFactory
import ir.sharif.simplenote.ui.theme.SimpleNoteTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel // Injected via factory
) {
    val notes by viewModel.paginatedNotes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val totalPages by viewModel.totalPages.collectAsState()

    Scaffold(
        containerColor = Color(0xFFF9F9F9), // Light background color from Figma
        topBar = {
            TopAppBar(
                title = { Text("My Notes", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent, // Make TopAppBar transparent
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            Box {
                NavigationBar {
                    NavigationBarItem(
                        selected = true,
                        onClick = { /* Already on Home */ },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                        label = { Text("Home") }
                    )
                    // Empty space for the centered FAB
                    NavigationBarItem(
                        selected = false,
                        onClick = { /* Empty space for FAB */ },
                        icon = { Spacer(modifier = Modifier.size(24.dp)) },
                        label = { Spacer(modifier = Modifier.height(0.dp)) },
                        enabled = false
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { /* TODO: navigate to settings when available */ },
                        icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                        label = { Text("Setting") }
                    )
                }
                
                // Centered FloatingActionButton overlapping the navigation bar
                FloatingActionButton(
                    onClick = { navController.navigate("noteDetail") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-16).dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Note", tint = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            Column(
            modifier = Modifier
                .fillMaxSize()
            ) {
            // Search box
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search notes...") }
            )

            // Grid or empty state
            if (notes.isEmpty()) {
                val emptyMessage = if (searchQuery.isNotEmpty()) "nothing found" else "Create your first note!"
                EmptyNotesView(modifier = Modifier.weight(1f), message = emptyMessage)
            } else {
                NotesGridView(modifier = Modifier.weight(1f), notes = notes, navController = navController)
            }

            // Pagination controls
            PaginationBar(
                currentPage = currentPage,
                totalPages = totalPages,
                onPrev = { viewModel.prevPage() },
                onNext = { viewModel.nextPage() }
            )
            }
        }
    }
}

@Composable
fun EmptyNotesView(modifier: Modifier = Modifier, message: String = "Create your first note!") {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_empty_notes), // Replace with your actual image
                contentDescription = "No notes illustration",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = message,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun NotesGridView(modifier: Modifier = Modifier, notes: List<Note>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(notes, key = { it.id }) { note ->
            NoteItem(note = note, onClick = {
                navController.navigate("noteDetail/${note.id}") // Navigate to edit existing note
            })
        }
    }
}

@Composable
fun PaginationBar(
    currentPage: Int,
    totalPages: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Surface(tonalElevation = 1.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Page $currentPage of $totalPages",
                style = MaterialTheme.typography.bodyMedium
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onPrev, enabled = currentPage > 1) {
                    Text("Previous")
                }
                OutlinedButton(onClick = onNext, enabled = currentPage < totalPages) {
                    Text("Next")
                }
            }
        }
    }
}

@Composable
fun NoteItem(note: Note, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Fixed height for bigger cards
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = note.title.ifEmpty { "Untitled" },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            val formattedDate = remember(note.lastModified) {
                SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(note.lastModified))
            }
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Preview needs NavController and ViewModel, so it becomes more complex
// @Preview(showBackground = true)
// @Composable
// fun HomeScreenPreview() {
//    SimpleNoteTheme {
//        // HomeScreen() // This will no longer work directly
//    }
// }
