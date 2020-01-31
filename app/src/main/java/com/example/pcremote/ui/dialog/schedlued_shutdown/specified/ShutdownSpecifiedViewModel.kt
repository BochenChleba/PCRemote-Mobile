package com.example.pcremote.ui.dialog.schedlued_shutdown.specified

import androidx.lifecycle.ViewModel
import com.example.pcremote.data.constants.TimeConstants
import com.example.pcremote.ext.toIntOrZero
import java.util.*

class ShutdownSpecifiedViewModel: ViewModel() {

    fun calculateTimeoutInSeconds(hours: String, minutes: String, seconds: String): Int? {
        val hoursInt = hours.toIntOrZero()
        val minutesInt = minutes.toIntOrZero()
        val secondsInt = seconds.toIntOrZero()

        if (!timeIsValid(hoursInt, minutesInt, secondsInt)) {
            return null
        }

        val calendar = Calendar.getInstance()
        val currentTimeInSeconds =
            calendar.get(Calendar.HOUR_OF_DAY) * TimeConstants.SECONDS_IN_HOUR +
                    calendar.get(Calendar.MINUTE) * TimeConstants.SECONDS_IN_MINUTE +
                    calendar.get(Calendar.SECOND)
        val desiredTimeInSeconds = hoursInt * TimeConstants.SECONDS_IN_HOUR +
                minutesInt * TimeConstants.SECONDS_IN_MINUTE +
                secondsInt
        val diff = desiredTimeInSeconds - currentTimeInSeconds

        return if (diff >= 0) {
            diff
        } else {
            TimeConstants.SECONDS_IN_DAY + diff
        }
    }

    private fun timeIsValid(hours: Int, minutes: Int, seconds: Int) =
            hours < TimeConstants.HOURS_IN_DAY && minutes < TimeConstants.MINUTES_IN_HOUR && seconds < TimeConstants.SECONDS_IN_MINUTE
}