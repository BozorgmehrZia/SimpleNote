package ir.sharif.simplenote.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.sharif.simplenote.R
import ir.sharif.simplenote.ui.components.ForwardButton
import ir.sharif.simplenote.ui.theme.Purple

@Composable
fun OnboardingScreen(onStartClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboarding_image),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Jot Down anything you want to achieve, today or in the future",
                color = Color.White,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(200.dp))
            ForwardButton(
                text = "Let’s Get Started",
                containerColor = Color.White,
                contentColor = Purple
            ) {}
        }
    }
}


@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen {}
}