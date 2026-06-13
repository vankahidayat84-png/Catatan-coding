package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = ColorAccent,
    background = BgMain,
    surface = BgCard,
    onPrimary = TextMain,
    onBackground = TextMain,
    onSurface = TextMain,
    surfaceVariant = BgCard,
    onSurfaceVariant = TextSecondary
  )

private val LightColorScheme =
  lightColorScheme(
    primary = ColorAccent,
    background = BgMain,
    surface = BgCard,
    onPrimary = TextMain,
    onBackground = TextMain,
    onSurface = TextMain,
    surfaceVariant = BgCard,
    onSurfaceVariant = TextSecondary
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> DarkColorScheme // Force dark theme according to requirement
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
