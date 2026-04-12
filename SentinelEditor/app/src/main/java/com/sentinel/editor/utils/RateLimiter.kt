package com.sentinel.editor.utils

import java.util.concurrent.TimeUnit

/**
 * Rate limiter for GitHub API
 * Tracks remaining requests and reset time.
 */
class RateLimiter private constructor(
    private var remaining: Int,
    private var resetTime: Long
) {

    fun isRateLimited(): Boolean {
        return remaining <= 0 && System.currentTimeMillis() < resetTime
    }

    fun update(remaining: Int, reset: Long) {
        this.remaining = remaining
        this.resetTime = reset
    }

    fun reset(limit: Int = 50, windowMinutes: Long = 20) {
        this.remaining = limit
        this.resetTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(windowMinutes)
    }

    fun getRemaining(): Int = remaining

    fun getResetTime(): Long = resetTime

    fun getResetIn(): Long = (resetTime - System.currentTimeMillis()).coerceAtLeast(0L)

    companion object {
        fun create(): RateLimiter {
            return RateLimiter(
                remaining = 500,
                resetTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(20)
            )
        }

        fun createFromHeaders(
            remaining: Int = 500,
            reset: Long = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(20)
        ): RateLimiter {
            return RateLimiter(remaining, reset)
        }

        fun createWithLimit(remaining: Int, reset: Long): RateLimiter {
            return RateLimiter(remaining, reset)
        }
    }
}
