package ir.sharif.simplenote.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.sharif.simplenote.data.model.TokenStore
import ir.sharif.simplenote.ui.components.AppBar
import ir.sharif.simplenote.ui.components.ConfirmationDialog
import ir.sharif.simplenote.ui.navigation.LocalNavController

@Composable
fun SettingsScreen() {
    val navController = LocalNavController.current

    var showDialog by remember { mutableStateOf(false) }

    AppBar("Settings") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Profile()

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(thickness = 0.8.dp)

            Spacer(modifier = Modifier.height(18.dp))
            Text(
                "APP SETTINGS",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("changePassword")
                    }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Outlined.Lock, contentDescription = "Lock")
                Spacer(modifier = Modifier.width(12.dp))
                Text("Change Password", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = "Right Arrow"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(thickness = 0.8.dp)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDialog = true }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Log Out",
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Log Out",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                "Taha Notes v1.1",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (showDialog) {
                ConfirmationDialog(
                    title = "Log Out",
                    text = "Are you sure you want to log out from the application?",
                    onDismiss = { showDialog = false },
                    onConfirm = {
                        showDialog = false
                        TokenStore.clear()
                        navController.navigate("login") {
                            popUpTo("settings") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
