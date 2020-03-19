package com.example.pcremote.ui.fragment.power_control

import android.os.Handler
import androidx.lifecycle.ViewModel
import com.example.pcremote.ext.convertMillisToDisplayableTime
import com.example.pcremote.singleton.Preferences
import com.example.pcremote.ui.activity.base.BaseViewModel


class PowerControlViewModel: BaseViewModel<PowerControlNavigator>() {
    private val handler = Handler()
    private var currentRunnable: Runnable? = null

    fun checkScheduledShutdown() {
        val shutdownTime = prefs.getShutdownTime()
        if (shutdownTime == Preferences.UNSET_LONG) {
            navigator.hideCountdown()
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
                    navigator.displayCountdown(remainedTime.convertMillisToDisplayableTime())
                    handler.postDelayed(this,1000)
                } else {
                    prefs.clearShutdownTime()
                    navigator.toastOnShutdown()
                    navigator.hideCountdown()
                }
            }
        }.apply { run() }

        navigator.showCountdown()
    }

    fun abortShutdown() {
        currentRunnable?.let { runnable ->
            handler.removeCallbacks(runnable)
            currentRunnable = null
        }
        prefs.clearShutdownTime()
        navigator.hideCountdown()
    }
}