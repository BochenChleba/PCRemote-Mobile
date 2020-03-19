package com.example.pcremote.ui.dialog.scan

import com.example.pcremote.ui.activity.base.BaseNavigator

interface WifiScanNavigator: BaseNavigator {
    fun updateFoundDevicesList(address: String)
    fun scanCompleted()
}