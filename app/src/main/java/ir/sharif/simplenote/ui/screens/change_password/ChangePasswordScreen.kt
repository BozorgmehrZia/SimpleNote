package ir.sharif.simplenote.ui.screens.change_password

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(navController: NavHostController) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    val viewModel: ChangePasswordViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Handle success state
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Change Password", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            label = { Text("Current Password") },
            enabled = !uiState.isLoading
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            enabled = !uiState.isLoading
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm New Password") },
            enabled = !uiState.isLoading
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { 
                viewModel.changePassword(currentPassword, newPassword, confirmPassword)
            },
            enabled = !uiState.isLoading && currentPassword.isNotEmpty() && 
                     newPassword.isNotEmpty() && confirmPassword.isNotEmpty()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(if (uiState.isLoading) "Changing..." else "Save Changes")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        TextButton(
            onClick = { navController.popBackStack() },
            enabled = !uiState.isLoading
        ) {
            Text("Cancel")
        }
        
        // Show error message if any
        uiState.errorMessage?.let { errorMessage ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
