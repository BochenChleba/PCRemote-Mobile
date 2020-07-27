package com.example.pcremote.ui.fragment.power_control

import android.os.Handler
import com.example.pcremote.ext.convertMillisToDisplayableTime
import com.example.pcremote.persistance.Preferences
import com.example.pcremote.ui.abstraction.BaseViewModel


class PowerControlViewModel: BaseViewModel<PowerControlNavigator>() {
    private val handler = Handler()
    private var currentRunnable: Runnable? = null

    fun checkScheduledShutdown() {
        val shutdownTime = prefs.getShutdownTime()
        if (shutdownTime == Preferences.UNSET_LONG) {
            getNavigator().hideCountdown()
            return
        }
        currentRunnable?.let { runnable ->
            handler.removeCallbacks(runnable)
            currentRunnable = null
        }

        currentRunnable = object: Runnable {
            override fun run() {
                val remainedTime = shutdownTime - System.currentTimeMillis()
                if (remainedTime > 0) {
                    getNavigator().displayCountdown(remainedTime.convertMillisToDisplayableTime())
                    handler.postDelayed(this, 1000)
                } else {
                    prefs.clearShutdownTime()
                    getNavigator().toastOnShutdown()
                    getNavigator().hideCountdown()
                }
            }
        }.apply { run() }

        getNavigator().showCountdown()
    }

    fun abortShutdown() {
        currentRunnable?.let { runnable ->
            handler.removeCallbacks(runnable)
            currentRunnable = null
        }
        prefs.clearShutdownTime()
        getNavigator().hideCountdown()
    }
}