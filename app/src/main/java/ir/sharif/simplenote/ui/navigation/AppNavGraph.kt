package ir.sharif.simplenote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ir.sharif.simplenote.ui.screens.change_password.ChangePasswordScreen
import ir.sharif.simplenote.ui.screens.home.HomeScreen
import ir.sharif.simplenote.ui.screens.onboarding.OnboardingScreen
import ir.sharif.simplenote.ui.screens.register.RegisterScreen
import ir.sharif.simplenote.ui.screens.login.LoginScreen
import ir.sharif.simplenote.ui.screens.settings.SettingsScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {
        composable("onboarding") {
            OnboardingScreen()
        }

        composable("register") {
            RegisterScreen()
        }

        composable("login") {
            LoginScreen()
        }

        composable("home") {
            HomeScreen()
        }

        composable("settings") {
            SettingsScreen()
        }

        composable("changePassword") {
            ChangePasswordScreen()
        }
    }
}
