package ir.sharif.simplenote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import ir.sharif.simplenote.di.NoteRepositoryInstance
import ir.sharif.simplenote.di.RetrofitInstance
import ir.sharif.simplenote.ui.navigation.AppNavGraph
import ir.sharif.simplenote.ui.navigation.LocalNavController
import ir.sharif.simplenote.ui.theme.SimpleNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize RetrofitInstance with context
        RetrofitInstance.initialize(this)
        
        // Initialize NoteRepositoryInstance with offline-first functionality
        NoteRepositoryInstance.initialize(this)
        
        setContent {
            SimpleNoteTheme {
                val navController = rememberNavController()
                CompositionLocalProvider(LocalNavController provides navController) {
                    AppNavGraph(navController = navController)
                }
            }
        }
    }
}