package com.example.pcremote.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class TouchpadView(context: Context, attributeSet: AttributeSet)
    : View(context, attributeSet) {

    companion object {
        private const val DELAY = 50
        private const val UNSET_FLOAT = -1f
    }

    var listener: ITouchpadView? = null
    private var prevX = UNSET_FLOAT
    private var prevY = UNSET_FLOAT
    private var prevEventTs = 0L
    private var viableForClickEvent = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startTracking(event)
            }
            MotionEvent.ACTION_MOVE -> {
                track(event)
            }
            MotionEvent.ACTION_UP -> {
                stopTracking()
            }
        }
        return true
    }

    private fun startTracking(event: MotionEvent) {
        prevX = event.x
        prevY = event.y
        prevEventTs = System.currentTimeMillis()
    }

    private fun track(event: MotionEvent) {
        if (System.currentTimeMillis() - prevEventTs < DELAY) {
            return
        }
        val xOffset = (event.x - prevX) / width
        val yOffset = (event.y - prevY) / height
        listener?.onTouchpadMove(Offset(xOffset, yOffset))
        prevX = event.x
        prevY = event.y
        viableForClickEvent = false
        prevEventTs = System.currentTimeMillis()
    }

    private fun stopTracking() {
        prevX = UNSET_FLOAT
        prevY = UNSET_FLOAT
        if (viableForClickEvent) {
            listener?.onTouchpadClick()
        } else {
            viableForClickEvent = true
        }
        prevEventTs = 0L
    }
}
