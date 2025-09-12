package ir.sharif.simplenote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.sharif.simplenote.data.repository.NoteRepository // Import repository
import ir.sharif.simplenote.di.NoteRepositoryInstance
import ir.sharif.simplenote.ui.AppViewModelFactory
import ir.sharif.simplenote.ui.screens.change_password.ChangePasswordScreen
import ir.sharif.simplenote.ui.screens.home.HomeScreen
import ir.sharif.simplenote.ui.screens.home.HomeViewModel
import ir.sharif.simplenote.ui.screens.login.LoginScreen
import ir.sharif.simplenote.ui.screens.notedetail.NoteDetailScreen
import ir.sharif.simplenote.ui.screens.notedetail.NoteDetailViewModel
import ir.sharif.simplenote.ui.screens.onboarding.OnboardingScreen
import ir.sharif.simplenote.ui.screens.register.RegisterScreen
import ir.sharif.simplenote.ui.screens.settings.SettingsScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    // Use the shared NoteRepository instance that connects to Django API
    val noteRepository = remember { NoteRepositoryInstance.noteRepository }

    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {
        composable("onboarding") {
            OnboardingScreen() // OnboardingScreen uses LocalNavController.current
        }

        composable("register") {
            RegisterScreen(navController = navController)
        }

        composable("login") {
            LoginScreen() // LoginScreen uses LocalNavController.current
        }

        composable("home") {
            val factory = remember { AppViewModelFactory(noteRepository) }
            val homeViewModel: HomeViewModel = viewModel(factory = factory)
            HomeScreen(navController = navController, viewModel = homeViewModel)
        }

        composable("settings") {
            SettingsScreen(navController = navController)
        }

        composable("changePassword") {
            ChangePasswordScreen()
        }

        // Route for new note
        composable("noteDetail") {
            val factory = remember { AppViewModelFactory(noteRepository, null) } // noteId is null for new note
            val noteDetailViewModel: NoteDetailViewModel = viewModel(factory = factory)
            NoteDetailScreen(
                viewModel = noteDetailViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Route for existing note (with noteId argument)
        composable(
            route = "noteDetail/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.StringType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            val factory = remember { AppViewModelFactory(noteRepository, noteId) }
            val noteDetailViewModel: NoteDetailViewModel = viewModel(factory = factory)
            NoteDetailScreen(
                viewModel = noteDetailViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
