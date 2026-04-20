package com.sentinel.editor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.sentinel.editor.navigation.NavigationGraph
import com.sentinel.editor.ui.theme.SentinelEditorTheme
import com.sentinel.editor.utils.ThemeMode
import com.sentinel.editor.utils.ThemePreferenceManager
import kotlinx.coroutines.launch

/**
 * Main Android Activity for Sentinel Editor - GitHub Repository Editor App
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val themeMode by ThemePreferenceManager.themeModeFlow(context)
                .collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)
            val systemDarkTheme = isSystemInDarkTheme()
            val useDarkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> systemDarkTheme
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            SentinelEditorTheme(
                darkTheme = useDarkTheme,
                dynamicColor = themeMode == ThemeMode.SYSTEM
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavigationGraph(
                        navController = navController,
                        themeMode = themeMode,
                        onThemeModeChange = { selectedThemeMode ->
                            coroutineScope.launch {
                                ThemePreferenceManager.setThemeMode(context, selectedThemeMode)
                            }
                        }
                    )
                }
            }
        }
    }
}