package com.example.pcremote.view

import android.graphics.Point

interface ITouchpadView {
    fun onTouchpadMove(offset: Offset)
    fun onTouchpadClick()
    fun onTouchpadLongClick()
}