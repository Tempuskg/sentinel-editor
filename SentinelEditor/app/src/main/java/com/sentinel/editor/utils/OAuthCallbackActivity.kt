package com.sentinel.editor.utils

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberCarouselUiState
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.IconTypography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.sentinel.database.*
import com.sentinel.model.GitHubAuth
import com.sentinel.ui.theme.SentinelEditorTheme
import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.net.InetAddress
import java.security.MessageDigest
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * OAuth Callback Activity
 * Handles GitHub OAuth flow completion with PKCE
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
@OptIn(ExperimentalMaterial3Api::class)
class OAuthCallbackActivity : ComponentActivity() {
    
    companion object {
        const val SCHEME = "com.sentinel.editor.github.auth"
        const val STATE_PARAM = "state"
        const val CODE_PARAM = "code"
        const val TOKEN_PARAM = "token"
        const val USER_PARAM = "user"
        const val EXPIRES_AT_PARAM = "expiresAt"
        const val CLIENT_ID_PARAM = "clientId"
    }
    
    private var authCode: String? = null
    private var authState: String? = null
    private var authToken: String? = null
    private var authUser: String? = null
    private var authExpiresAt: Long? = null
    private var authClientId: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        // Extract OAuth parameters from intent
        authCode = intent.getStringExtra(CODE_PARAM)
        authState = intent.getStringExtra(STATE_PARAM)
        authToken = intent.getStringExtra(TOKEN_PARAM)
        authUser = intent.getStringExtra(USER_PARAM)
        authExpiresAt = intent.getLongExtra(EXPIRES_AT_PARAM, 0)
        authClientId = intent.getStringExtra(CLIENT_ID_PARAM)
        
        setContent {
            SentinelEditorTheme {
                OAuthCallbackContent(
                    code = authCode,
                    state = authState,
                    token = authToken,
                    user = authUser,
                    expiresAt = authExpiresAt,
                    clientId = authClientId
                ) { token, user, expiresAt ->
                    saveAuthToken(token!!, user, expiresAt)
                }
            }
        }
    }
    
    /**
     * OAuth Callback Content Composable
     */
    @Composable
    fun OAuthCallbackContent(
        code: String?,
        state: String?,
        token: String?,
        user: String?,
        expiresAt: Long?,
        clientId: String?,
        onAuthenticated: (String, String?, Long?) -> Unit
    ) {
        val context = LocalContext.current
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("GitHub Authentication", style = MaterialTheme.typography.titleLarge) },
                    navigationIcon = {
                        IconButton(onClick = { 
                            // Cancel and return to app
                            val cancelIntent = Intent(context, MainActivity::class.java)
                            cancelIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(cancelIntent)
                            finish()
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Cancel")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    actions = {
                        IconButton(onClick = {
                            val githubIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/login"))
                            githubIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(githubIntent)
                        }) {
                            Icon(Icons.Default.OpenInNew, contentDescription = "Open GitHub")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Loading indicator if code is provided
                if (code != null) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        strokeWidth = 3.dp
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "Authenticating with GitHub...",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Please wait while we verify your credentials",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else if (token != null) {
                    // Already authenticated
                    Text(
                        text = "✅ Authentication Successful!",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                cornerRadius = 8.dp
                            )
                            .padding(16.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    SuccessScreen(token, user, expiresAt)
                } else {
                    // Empty state
                    EmptyStateScreen {
                        val githubIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/login"))
                        githubIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(githubIntent)
                    }
                }
            }
        }
    }
    
    /**
     * Success screen showing authentication result
     */
    @Composable
    fun SuccessScreen(
        token: String,
        user: String?,
        expiresAt: Long?
    ) {
        Surface {
            Column (
                modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            ) {
                Text(
                    text = "User Profile",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Card {
                    AsyncImage(
                        model = AsyncImageLoader(user?.take(50) ?: "https://github.com.png"),
                        contentDescription = Avatar content description
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = user ?: "Unknown user",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Access Token (first 16 chars): ${token.take(16)}...",
                    style = MaterialTheme.typography.bodySmall
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Expires: ${if (expiresAt != 0L) {
                        val now = System.currentTimeMillis()
                        expireAtSeconds(now, expiresAt).let { if (it != null) "$it seconds left" else "Never" }
                    } else "Never" }",
                    style = MaterialTheme.typography.bodySmall
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        // Navigate to main app
                        val mainIntent = Intent(this@OAuthCallbackActivity, MainActivity::class.java)
                        mainIntent.addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                        startActivity(mainIntent)
                        finish()
                    }
                ) {
                    Icon(Icons.Default.Check, contentDescription = "")
                    Text ("Continue")
                }
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Refresh state when returning from background
    }
}