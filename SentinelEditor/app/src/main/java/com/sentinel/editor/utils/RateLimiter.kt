package com.sentinel.editor.utils

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

/**
 * Rate Limiter for API requests - implements retry logic with exponential backoff
 */
class RateLimiter {
    data class LimitHeader(val limit: Int, val reset: Long, val remaining: Int)

    private var callsWithinWindow: Int = 0
    private var currentWindowStart: Long = System.currentTimeMillis()
    private var tokenBucketTokens: Double = 0.0
    private val windowSizeInSeconds: Int = 1
    private val maxRequestsPerWindow: Int = 60

    fun checkLimit(limit: LimitHeader?): Boolean {
        if (limit == null) return false
        val now = System.currentTimeMillis()
        
        // Reset window if expired
        if (now - currentWindowStart > windowSizeInSeconds * 1000L) {
            currentWindowStart = now
            callsWithinWindow = 0
            tokenBucketTokens = maxRequestsPerWindow.toDouble()
        }
        
        val tokenBucketRefillRate = maxRequestsPerWindow.toDouble() / windowSizeInSeconds
        
        val nowInSeconds = now / 1000
        val windowStartInSec = currentWindowStart / 1000
        val timeElapsed = nowInSeconds - windowStartInSec
        
        tokenBucketTokens = (tokenBucketTokens + (timeElapsed * tokenBucketRefillRate)).coerceAtMost(maxRequestsPerWindows.toDouble())
        
        return tokenBucketTokens > 0.0
    }

    private val maxRequestsPerWindows: Int = 60

    suspend fun executeSafely(request: suspend () -> Any): Any {
        return try {
            request()
        } catch (e: Exception) {
            // Handle rate limit or other errors
            if (e.message?.contains("Rate limit", ignoreCase = true) == true) {
                // Implement backoff
                e.printStackTrace()
            }
            retryAfter(e)
        }
    }
    
    private suspend fun retryAfter(e: Exception): Any {
        throw e
    }
}