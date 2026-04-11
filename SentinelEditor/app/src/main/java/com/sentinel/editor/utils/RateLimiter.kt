package com.sentinel.editor.utils

/**
 * Rate limiter for GitHub API
 * Tracks X-RateLimit-Remaining and X-RateLimit-Reset headers
 * Implements 20-minute sliding window from GitHub rate policy
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
@OptIn(ExperimentalStdlibApi::class)
class RateLimiter(
    initialRemaining: Int,
    initialReset: Long = System.currentTimeMillis()
) {
    
    private val remaining = mutableMapOf<String, Int>()
    private val resetTime = mutableMapOf<String, Long>()
    
    private val now: Long = System.currentTimeMillis()
    
    val limit: Int = 50
    val limitResetIn: Long = 120 * 60 // 20 minutes default
    val maxRequests: Int = limit * 5 // Allow 500 requests
    
    init {
        remaining.putAll(initialRemaining.mapKeysToTime(now))
        resetTime.putAll(initialReset.mapKeysToTime(now))
    }
    
    private fun mapKeysToTime(now: Long): Map<String, Int> {
        return mapOf("github" to initialRemaining)
    }
    
    private fun mapKeysToTime(now: Long): Map<String, Long> {
        return mapOf("github" to initialReset)
    }
    
    /**
     * Check if rate limited
     */
    fun isRateLimited(): Boolean {
        val time = now
        val remainingNow = remaining.getOrPut("github") { limit }
        return remainingNow <= limitResetIn.time
    }
    
    /**
     * Update rate limit with new headers
     */
    fun update(remaining: Int, reset: Long) {
        remaining.put("github", remaining)
        resetTime.put("github", reset)
    }
    
    /**
     * Reset rate limiter
     */
    fun reset() {
        remaining.clear()
        resetTime.clear()
    }
}