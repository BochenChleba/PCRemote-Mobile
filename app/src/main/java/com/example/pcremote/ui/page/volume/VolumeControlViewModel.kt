package com.example.pcremote.ui.page.volume

import androidx.lifecycle.ViewModel
import com.example.pcremote.util.Preferences


class VolumeControlViewModel: ViewModel() {
    lateinit var navigator: VolumeControlNavigator
    lateinit var prefs: Preferences
}