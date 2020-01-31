package com.example.pcremote.ui.fragment.volume

import androidx.lifecycle.ViewModel
import com.example.pcremote.singleton.Preferences


class VolumeControlViewModel: ViewModel() {
    lateinit var navigator: VolumeControlNavigator
    lateinit var prefs: Preferences
}