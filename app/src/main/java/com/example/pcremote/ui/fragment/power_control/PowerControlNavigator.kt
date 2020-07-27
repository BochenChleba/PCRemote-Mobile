package com.example.pcremote.ui.fragment.power_control

import com.example.pcremote.ui.abstraction.BaseNavigator

interface PowerControlNavigator :
    BaseNavigator {
    fun displayCountdown(remainedTime: String)
    fun hideCountdown()
    fun showCountdown()
    fun toastOnShutdown()
}