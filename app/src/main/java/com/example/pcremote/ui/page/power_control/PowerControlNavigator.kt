package com.example.pcremote.ui.page.power_control

interface PowerControlNavigator {
    fun displayCountdown(remainedTime: String)
    fun hideCountdown()
    fun showCountdown()
    fun toastOnShutdown()
}