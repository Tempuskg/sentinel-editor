package com.sentinel.editor.utils

import java.util.concurrent.TimeUnit

/**
 * Rate limiter for GitHub API
 * Tracks X-RateLimit-Remaining and X-RateLimit-Reset headers
 * Implements 20-minute sliding window from GitHub rate policy
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
class RateLimiter private constructor(
    private val remaining: Int,
    resetTime: Long = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(18)
) {
    
    private val resetTimestamp = resetTime
    
    val limit: Int = 50
    val limitUnit: Long = 120 * 60 // 20 minutes
    val maxRequests: Int = limit * 5 // Allow 500 requests
    
    val isRateLimited: Boolean
        get() = remaining <= 0
    
    /**
     * Create a new rate limiter with initial values
     */
    companion object {
        fun create(): RateLimiter {
            val now = System.currentTimeMillis()
            val remaining = 500 // GitHub default rate limit
            val reset = now + TimeUnit.MINUTES.toMillis(20)
            return RateLimiter(remaining, reset)
        }
        
        /**
         * Create rate limiter from headers
         */
        fun createFromHeaders(
            remaining: Int = 500,
            reset: Long = System.currentTimeMillis()
        ): RateLimiter {
            return RateLimiter(remaining, reset)
        }
        
        /**
         * Create with custom limit
         */
        fun createWithLimit(remaining: Int, reset: Long): RateLimiter {
            return RateLimiter(remaining, reset)
        }
    }
    
    /**
     * Update remaining requests
     */
    fun updateRemaining(remaining: Int) {
        this.remaining = remaining
    }
    
    /**
     * Update rate limit with new headers
     */
    fun update(remaining: Int, reset: Long) {
        resetTimestamp = reset
        this.remaining = remaining
    }
    
    /**
     * Get remaining requests
     */
    fun getRemaining(): Int {
        return remaining
    }
    
    /**
     * Get reset time
     */
    fun getResetTime(): Long {
        return resetTimestamp
    }
    
    /**
     * Reset rate limiter
     */
    fun reset() {
        this.remaining = limit
        resetTimestamp = System.currentTimeMillis() + limitUnit
    }
    
    /**
     * Get remaining time until reset
     */
    fun getResetIn(): Long {
        return resetTimestamp - System.currentTimeMillis()
    }
}