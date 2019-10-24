package com.example.pcremote.ui.dialog.schedlued_shutdown.countdown

import androidx.lifecycle.ViewModel
import com.example.pcremote.constants.TimeConstants
import com.example.pcremote.ext.toIntOrZero

class ShutdownCountdownViewModel: ViewModel() {
    fun calculateTimeoutInSeconds(hours: String, minutes: String, seconds: String) =
        (hours.toIntOrZero() * TimeConstants.SECONDS_IN_HOUR) +
                (minutes.toIntOrZero() * TimeConstants.SECONDS_IN_MINUTE) +
                (seconds.toIntOrZero())
}