package com.example.pcremote.ui.page.touchpad

import androidx.lifecycle.ViewModel
import com.example.pcremote.util.Preferences

class TouchpadViewModel: ViewModel() {
    lateinit var navigator: TouchpadNavigator
    lateinit var prefs: Preferences
}