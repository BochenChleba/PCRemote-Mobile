package com.example.pcremote.view.touchpad

import com.example.pcremote.data.Offset

interface ITouchpadView {
    fun onTouchpadMove(offset: Offset)
    fun onTouchpadClick()
    fun onTouchpadLongClick()
    fun onTouchEventIntercepted(isEventFinished: Boolean)
}