package com.example.pcremote.ui.dialog.scan

import com.example.pcremote.ui.abstraction.BaseNavigator

interface WifiScanNavigator: BaseNavigator {
    fun updateFoundDevicesList(address: String)
    fun scanCompleted()
}