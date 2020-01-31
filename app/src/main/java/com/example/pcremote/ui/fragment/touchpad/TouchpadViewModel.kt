package com.example.pcremote.ui.fragment.touchpad

import androidx.lifecycle.ViewModel
import com.example.pcremote.singleton.Preferences

class TouchpadViewModel: ViewModel() {
    lateinit var navigator: TouchpadNavigator
    lateinit var prefs: Preferences
}