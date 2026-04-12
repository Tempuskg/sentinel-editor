package com.sentinel.editor.utils

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sentinel.editor.MainActivity
import com.sentinel.editor.ui.theme.SentinelEditorTheme
import com.sentinel.model.GitHubAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OAuthCallbackActivity : ComponentActivity() {

    companion object {
        const val CODE_PARAM = "code"
        const val STATE_PARAM = "state"
        const val TOKEN_PARAM = "token"
        const val USER_PARAM = "user"
        const val EXPIRES_AT_PARAM = "expiresAt"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val token = intent.getStringExtra(TOKEN_PARAM)
        val user = intent.getStringExtra(USER_PARAM) ?: ""
        val expiresAt = intent.getLongExtra(EXPIRES_AT_PARAM, 0L)

        if (!token.isNullOrBlank()) {
            saveAuthToken(token, user, if (expiresAt > 0) expiresAt else null)
        }

        setContent {
            SentinelEditorTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(if (token != null) "Authentication successful" else "Authenticating…")
                    }
                }
            }
        }
    }

    private fun saveAuthToken(token: String, user: String, expiresAt: Long?) {
        val auth = GitHubAuth(
            userId = user,
            accessToken = token,
            expiresAt = expiresAt
        )
        CoroutineScope(Dispatchers.IO).launch {
            GitHubTokenManager.storeToken(applicationContext, auth)
            Log.i("OAuthCallback", "Token saved for user=$user")
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
