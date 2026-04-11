package com.sentinel.editor.ui.theme

/**
 * Material3 Theme for SentinelEditor
 * Dark theme by default for professional editor experience
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF3B82F6),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBBDEFB),
    onPrimaryContainer = Color(0xFF003058),
    secondary = Color(0xFF187AFF),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF001D41),
    onSecondaryContainer = Color(0xFFB3E5FC),
    tertiary = Color(0xFFFFB63D),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDDB2),
    onTertiaryContainer = Color(0xFF422101),
    background = Color(0xFF212026),
    onBackground = Color.White,
    surface = Color(0xFF494558),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF494558),
    onSurfaceVariant = Color(0xFFB1A7C7),
    outline = Color(0xFF918EA8),
    error = Color(0xFFF2B8B5),
    onError = Color.White,
    errorContainer = Color(0xFFCA6457),
    onErrorContainer = Color.White,
    inversePrimary = Color(0xFF3B82F6)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF187AFF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF5D93FF),
    onPrimaryContainer = Color(0xFF00234A),
    secondary = Color(0xFF187AFF),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4689FF),
    onSecondaryContainer = Color(0xFF001D41),
    tertiary = Color(0xFFFFAA35),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFCC80),
    onTertiaryContainer = Color(0xFF422101),
    background = Color(0xFFFFFAE3),
    onBackground = Color(0xFF424252),
    surface = Color(0xFFFFF9E0),
    onSurface = Color(0xFF424252),
    surfaceVariant = Color(0xFFE4E0F0),
    onSurfaceVariant = Color(0xFF494558),
    outline = Color(0xFF918EA8),
    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFFFDCC4),
    onErrorContainer = Color(0xFF831C14)
)

@Composable
fun SentinelEditorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@Composable
fun AppTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    // Use default Material3 theme with custom color scheme if needed
    MaterialTheme(
        content = content
    )
}
