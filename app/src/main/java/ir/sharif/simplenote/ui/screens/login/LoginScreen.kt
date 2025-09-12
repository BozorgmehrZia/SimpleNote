package ir.sharif.simplenote.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.sharif.simplenote.ui.components.ForwardButton
import ir.sharif.simplenote.ui.components.LabeledTextField
import ir.sharif.simplenote.ui.navigation.LocalNavController
import ir.sharif.simplenote.ui.theme.Purple

@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel()) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Let’s Login",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        Text(
            text = "And notes your idea",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        LabeledTextField(
            value = username,
            onValueChange = { username = it },
            label = "Username",
            placeholder = "Example: @HamifarTaha"
        )

        LabeledTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            placeholder = "********",
            visualTransformation = PasswordVisualTransformation(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        ForwardButton(
            text = "Login",
            containerColor = Purple,
            contentColor = Color.White,
            enabled = !uiState.isLoading,
            isLoading = uiState.isLoading
        ) {
            viewModel.login(username, password)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Divider with OR
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
            Text(
                "  Or  ",
                color = Color.Gray,
                fontSize = 12.sp
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Register link
        TextButton(
            onClick = {
                navController.navigate("register")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                "Don’t have any account? Register here",
                color = Purple,
                fontSize = 17.sp
            )
        }

        if (uiState.errorMessage != null) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.dismissError()
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.dismissError() }
                    ) {
                        Text("OK")
                    }
                },
                title = { Text("Error") },
                text = { Text(uiState.errorMessage ?: "") }
            )
        }

    }
}
