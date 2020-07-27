package com.example.pcremote.ui.abstraction

interface BaseNavigator {
    fun showToast(text: String)
    fun showToast(resourceId: Int)
}