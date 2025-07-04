package ir.sharif.simplenote.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//@Composable
//fun CenteredAppBar(
//    title: String,
//    onBackClick: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(56.dp)
//    ) {
////            modifier = Modifier.align(Alignment.CenterStart)
//        BackButton {  }
//
//        Text(
//            text = title,
//            fontSize = 18.sp,
//            fontWeight = FontWeight.SemiBold,
//            modifier = Modifier.align(Alignment.Center)
//        )
//    }
//}

//@Composable
//fun CenteredAppBar(
//    title: String,
//    onBackClick: () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(56.dp)
//            .padding(horizontal = 8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        BackButton {  }
//
//        Spacer(modifier = Modifier.weight(0.7f))
//
//        Text(
//            text = title,
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Medium
//        )
//
//        Spacer(modifier = Modifier.weight(1.3f))
//
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String, content: @Composable (innerPadding: PaddingValues) -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = Color.White,
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    ),
                    title = {
                        Text(
                            title,
                            maxLines = 1,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        BackButton { }
                    },
                    actions = {
                    },
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
                HorizontalDivider(thickness = 0.8.dp)
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}

