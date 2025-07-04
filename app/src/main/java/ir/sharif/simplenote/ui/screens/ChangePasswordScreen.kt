package ir.sharif.simplenote.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.sharif.simplenote.ui.components.AppBar
import ir.sharif.simplenote.ui.components.BackButton
import ir.sharif.simplenote.ui.components.ForwardButton
import ir.sharif.simplenote.ui.components.LabeledTextField
import ir.sharif.simplenote.ui.theme.Purple

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreen() {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var retypePassword by remember { mutableStateOf("") }
    AppBar("Change Password") { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // Current password section
            Text("Please input your current password first", color = Purple, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            LabeledTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = "Current Password",
                placeholder = "********",
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // New password section
            Text("Now, create your new password", color = Purple, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            LabeledTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "New Password",
                placeholder = "********",
                visualTransformation = PasswordVisualTransformation()
            )
            Text("Password should contain a-z, A-Z, 0-9", fontSize = 12.sp, color = Color.LightGray)

            Spacer(modifier = Modifier.height(20.dp))
            LabeledTextField(
                value = retypePassword,
                onValueChange = { retypePassword = it },
                label = "Retype New Password",
                placeholder = "********",
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.weight(1f))

            ForwardButton(
                text = "Submit New Password",
                containerColor = Purple,
                contentColor = Color.White
            ) { }
        }
    }
}
