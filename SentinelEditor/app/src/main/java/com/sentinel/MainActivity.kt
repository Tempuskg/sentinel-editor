package com.sentinel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sentinel.editor.navigation.NavigationGraph

/**
 * Main Activity - Simple launcher with navigation container
 */
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Use Compose for content
        setContent {
            // Empty content - NavigationContainer renders navigation
            androidx.compose.ui.unit.dp
            
            // Simple placeholder - actual navigation happens in NavigationContainer
        }
    }
}