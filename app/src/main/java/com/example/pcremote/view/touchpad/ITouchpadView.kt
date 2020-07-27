package com.example.pcremote.view.touchpad

import com.example.pcremote.data.dto.Offset

interface ITouchpadView {
    fun onTouchpadMove(offset: Offset)
    fun onTouchpadClick()
    fun onTouchpadLongClick()
}