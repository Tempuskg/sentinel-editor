package com.sentinel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

/**
 * Main App Launcher Activity - Direct launcher that starts the real editor MainActivity
 */
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Launch the actual editor MainActivity
        val intent = Intent(this, com.sentinel.editor.MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}