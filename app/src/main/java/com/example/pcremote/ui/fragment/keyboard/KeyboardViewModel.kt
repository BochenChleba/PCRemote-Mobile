package com.example.pcremote.ui.fragment.keyboard

import android.os.Handler
import androidx.lifecycle.ViewModel
import com.example.pcremote.ext.convertMillisToDisplayableTime
import com.example.pcremote.singleton.Preferences


class KeyboardViewModel: ViewModel() {
    lateinit var navigator: KeyboardNavigator
    lateinit var prefs: Preferences
}