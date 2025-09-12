package ir.sharif.simplenote.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.sharif.simplenote.data.repository.NoteRepository
import ir.sharif.simplenote.di.NoteRepositoryInstance
import ir.sharif.simplenote.ui.screens.home.HomeViewModel
import ir.sharif.simplenote.ui.screens.notedetail.NoteDetailViewModel

class AppViewModelFactory(private val noteRepository: NoteRepository = NoteRepositoryInstance.noteRepository, private val noteId: String? = null) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(noteRepository) as T
            }
            modelClass.isAssignableFrom(NoteDetailViewModel::class.java) -> {
                NoteDetailViewModel(noteRepository, noteId) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}