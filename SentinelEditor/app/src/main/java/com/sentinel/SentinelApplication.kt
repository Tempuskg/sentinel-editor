package com.sentinel

import android.app.Application
import android.util.Log

/**
 * Application entry point.
 */
class SentinelApplication : Application() {

    companion object {
        const val APP_TAG = "SentinelApp"
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(APP_TAG, "SentinelApplication initialized")
    }
}
