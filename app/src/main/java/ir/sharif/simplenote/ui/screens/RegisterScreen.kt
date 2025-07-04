package ir.sharif.simplenote.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.sharif.simplenote.ui.components.BackButton
import ir.sharif.simplenote.ui.components.ForwardButton
import ir.sharif.simplenote.ui.components.LabeledTextField
import ir.sharif.simplenote.ui.theme.Purple

@Composable
fun RegisterScreen(onBackToLogin: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        val firstName = remember { mutableStateOf("") }
        val lastName = remember { mutableStateOf("") }
        val username = remember { mutableStateOf("") }
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val passwordRetype = remember { mutableStateOf("") }

        BackButton("Back to Login") { }

        Text(
            text = "Register",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        Text(
            text = "And start taking notes",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        listOf(
            Triple("First Name", "Example: Taha", firstName),
            Triple("Last Name", "Example: Hamifar", lastName),
            Triple("Username", "Example: @HamifarTaha", username),
            Triple("Email Address", "Example: hamifar.taha@gmail.com", email)
        ).forEach { (label, placeholder, state) ->
            LabeledTextField(
                value = state.value,
                onValueChange = { state.value = it },
                label = label,
                placeholder = placeholder,
            )
        }

        listOf(
            "Password" to password,
            "Retype Password" to passwordRetype
        ).forEach { (label, state) ->
            LabeledTextField(
                value = state.value,
                onValueChange = { state.value = it },
                label = label,
                placeholder = "********",
                visualTransformation = PasswordVisualTransformation()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ForwardButton(
            text = "Register",
            containerColor = Purple,
            contentColor = Color.White
        ) {}

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBackToLogin) {
            Text("Already have an account? Login here", color = Purple)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen {}
}
