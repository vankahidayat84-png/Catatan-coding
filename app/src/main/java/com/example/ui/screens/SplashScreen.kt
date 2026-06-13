package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BgMain
import com.example.ui.theme.ColorAccent
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        delay(1000) // 3 seconds total with animation (1s scale + 1s alpha + 1s delay)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgMain),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = "Logo Background",
                    tint = ColorAccent,
                    modifier = Modifier.size(120.dp).scale(scale.value)
                )
                Text(
                    text = "S",
                    color = TextMain,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.scale(scale.value)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Developer Maskaav",
                color = TextSecondary,
                fontSize = 16.sp,
                modifier = Modifier.alpha(alpha.value)
            )
        }
    }
}
