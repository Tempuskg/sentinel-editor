package com.sentinel.editor

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.DisplayCutoutMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sentinel.editor.ui.theme.SentinelEditorTheme
import com.sentinel.ui.markdown.ComposeMarkdownEditor

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        // App Theme composable wrapper
        setContent {
            SentinelEditorTheme { // AppTheme -> SentinelEditorTheme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ComposeMarkdownEditor()
                }
            }
        }
        // Handle display cutouts for Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window?.layoutInDisplayCutoutMode = DisplayCutoutMode.NAVIGATION_BAR
        }
    }
    
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Handle rotation
    }
}
