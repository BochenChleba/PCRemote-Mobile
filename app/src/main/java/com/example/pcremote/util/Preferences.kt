package com.example.pcremote.util

import android.content.Context

class Preferences(context: Context) {

    companion object {
        const val UNSET_INT = -1
        const val UNSET_FLOAT = -1f
        const val UNSET_LONG = -1L

        private const val NAME = "PC_REMOTE_PREFS"
        private const val KEY_SHUTDOWN_TIME = "shutdownTime"

        var instance: Preferences? = null

        fun getInstance(context: Context): Preferences {
            if (instance == null) {
                instance = Preferences(context)
            }
            return instance!!
        }
    }

    private val sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun setShutdownTime(value: Long) {
        sharedPreferences.edit().also { editor ->
            editor.putLong(KEY_SHUTDOWN_TIME, value)
            editor.apply()
        }
    }

    fun clearShutdownTime() {
        sharedPreferences.edit().also { editor ->
            editor.remove(KEY_SHUTDOWN_TIME)
            editor.apply()
        }
    }

    fun getShutdownTime(): Long = sharedPreferences.getLong(KEY_SHUTDOWN_TIME, UNSET_LONG)

}