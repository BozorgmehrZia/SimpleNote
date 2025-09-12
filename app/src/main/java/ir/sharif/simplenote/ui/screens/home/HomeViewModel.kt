package ir.sharif.simplenote.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sharif.simplenote.data.repository.NoteRepository
import ir.sharif.simplenote.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {

    val notes: StateFlow<List<Note>> = noteRepository.notes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val pageSize: Int = 6 // 6 notes per page with bigger cards

    private val filteredNotesFlow: StateFlow<List<Note>> = combine(notes, _searchQuery) { allNotes, query ->
        val q = query.trim().lowercase()
        if (q.isEmpty()) allNotes
        else allNotes.filter { note ->
            note.title.lowercase().contains(q) || note.content.lowercase().contains(q)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    val totalPages: StateFlow<Int> = filteredNotesFlow
        .map { list ->
            val pages = (list.size + pageSize - 1) / pageSize
            if (pages <= 0) 1 else pages
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 1
        )

    val paginatedNotes: StateFlow<List<Note>> = combine(filteredNotesFlow, _currentPage) { list, page ->
        val safePage = page.coerceAtLeast(1)
        val fromIndex = (safePage - 1) * pageSize
        if (fromIndex >= list.size) emptyList()
        else list.subList(fromIndex, minOf(fromIndex + pageSize, list.size))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    init {
        // Load notes (triggers sync if online)
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            noteRepository.loadNotes()
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        // Reset to first page when query changes
        _currentPage.value = 1
    }

    fun nextPage() {
        val maxPage = totalPages.value
        if (_currentPage.value < maxPage) {
            _currentPage.value = _currentPage.value + 1
        }
    }

    fun prevPage() {
        if (_currentPage.value > 1) {
            _currentPage.value = _currentPage.value - 1
        }
    }

    fun goToPage(page: Int) {
        val maxPage = totalPages.value
        _currentPage.value = page.coerceIn(1, maxPage)
    }
}