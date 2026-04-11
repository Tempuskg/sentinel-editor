package com.sentinel.editor.utils

import android.content.Context
import android.content.SharedPreferences

class GitHubAuthenticator private constructor(
    context: Context,
    token: String,
    scopes: List<String>
) {
    val token: String = token
    val scopes: List<String> = scopes
    val expiresAt: Long? = null
    val authenticated: Boolean = token.isNotEmpty()

    private val sp: SharedPreferences =
        context.getSharedPreferences("sentinel_prefs", Context.MODE_PRIVATE)

    fun validateToken(): Boolean = authenticated

    fun refreshToken(): Boolean = false

    private fun isTokenExpiringSoon(): Boolean = false

    class Builder(private val context: Context) {
        private var token: String = ""
        private var scopes: List<String> = emptyList()

        fun token(token: String) = apply { this.token = token }

        fun scopes(scopes: List<String>) = apply { this.scopes = scopes }

        fun build(): GitHubAuthenticator = GitHubAuthenticator(context, token, scopes)
    }
}
