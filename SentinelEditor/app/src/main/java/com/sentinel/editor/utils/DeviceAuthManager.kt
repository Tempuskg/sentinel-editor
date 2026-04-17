package com.sentinel.editor.utils

import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Manages GitHub Device Authorization Flow lifecycle:
 * 1. Request device + user codes
 * 2. Poll for access token while user authorizes in browser
 */
class DeviceAuthManager(private val clientId: String) {

    private val service: DeviceAuthService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        Retrofit.Builder()
            .baseUrl("https://github.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DeviceAuthService::class.java)
    }

    /**
     * Step 1: Request a device code from GitHub.
     * Returns the code info the UI needs to display.
     */
    suspend fun requestDeviceCode(): DeviceCodeResponse {
        val response = service.requestDeviceCode(clientId)
        if (response.isSuccessful) {
            return response.body()
                ?: throw DeviceAuthException("Empty response from GitHub")
        }
        throw DeviceAuthException("Failed to request device code: HTTP ${response.code()}")
    }

    /**
     * Step 2: Poll GitHub until the user completes authorization or the code expires.
     * Returns the access token on success.
     *
     * @throws DeviceAuthException on expiration, access denied, or network failure
     */
    suspend fun pollForToken(deviceCode: String, interval: Int, expiresIn: Int): String {
        val pollIntervalMs = (interval.coerceAtLeast(5)) * 1000L
        val deadline = System.currentTimeMillis() + expiresIn * 1000L

        while (System.currentTimeMillis() < deadline) {
            delay(pollIntervalMs)

            val response = service.pollAccessToken(
                clientId = clientId,
                deviceCode = deviceCode
            )

            if (!response.isSuccessful) {
                throw DeviceAuthException("Poll failed: HTTP ${response.code()}")
            }

            val body = response.body()
                ?: throw DeviceAuthException("Empty poll response")

            when {
                body.accessToken != null -> return body.accessToken
                body.error == "authorization_pending" -> continue
                body.error == "slow_down" -> {
                    // GitHub asked us to slow down — add 5s per spec
                    delay(5000L)
                    continue
                }
                body.error == "expired_token" ->
                    throw DeviceAuthException("Device code expired. Please try again.")
                body.error == "access_denied" ->
                    throw DeviceAuthException("Authorization was denied by the user.")
                else ->
                    throw DeviceAuthException(body.errorDescription ?: "Unknown error: ${body.error}")
            }
        }

        throw DeviceAuthException("Device code expired. Please try again.")
    }
}

class DeviceAuthException(message: String) : Exception(message)
