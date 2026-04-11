package com.sentinel.editor.utils

class RateLimiter(
    initialRemaining: Int,
    initialReset: Long = System.currentTimeMillis()
) {
    private var remaining: Int = initialRemaining
    private var resetTime: Long = initialReset

    fun isRateLimited(): Boolean {
        return remaining <= 0 && System.currentTimeMillis() < resetTime
    }

    fun update(remaining: Int, reset: Long) {
        this.remaining = remaining
        this.resetTime = reset
    }

    fun reset() {
        remaining = 50
        resetTime = System.currentTimeMillis()
    }
}
