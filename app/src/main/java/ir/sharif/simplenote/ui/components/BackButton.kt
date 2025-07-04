package ir.sharif.simplenote.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.sharif.simplenote.R
import ir.sharif.simplenote.ui.theme.Purple

@Composable
fun BackButton(text: String = "Back", onBack: () -> Unit) {
    TextButton(onClick = onBack, contentPadding = PaddingValues(horizontal = 0.dp)) {
        Icons.AutoMirrored.Outlined.KeyboardArrowLeft
        Icon(
            Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
            contentDescription = null,
            tint = Purple
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, color = Purple, fontSize = 16.sp)
    }
}