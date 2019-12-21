package com.example.pcremote.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class TouchpadView(context: Context, attributeSet: AttributeSet)
    : View(context, attributeSet) {

    companion object {
        private const val DELAY = 140
        private const val UNSET_FLOAT = -1f
        private const val LONG_CLICK_TIME = 500L
    }

    var listener: ITouchpadView? = null
    private var prevX = UNSET_FLOAT
    private var prevY = UNSET_FLOAT
    private var prevEventTs = 0L
    private var viableForClickEvent = true
    private var longClickRunnable = Runnable {
        listener?.onTouchpadLongClick()
        viableForClickEvent = false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                actionDown(event)
            }
            MotionEvent.ACTION_MOVE -> {
                actionMove(event)
            }
            MotionEvent.ACTION_UP -> {
                actionUp()
            }
        }
        return true
    }

    private fun actionDown(event: MotionEvent) {
        storeEventData(event)
        handler.postDelayed(longClickRunnable, LONG_CLICK_TIME)
    }

    private fun actionMove(event: MotionEvent) {
        if (System.currentTimeMillis() - prevEventTs < DELAY) {
            return
        }
        handler.removeCallbacks(longClickRunnable)
        viableForClickEvent = false
        calculateSwipeOffset(event)
        storeEventData(event)
    }

    private fun actionUp() {
        if (viableForClickEvent) {
            handler.removeCallbacks(longClickRunnable)
            listener?.onTouchpadClick()
        } else {
            viableForClickEvent = true
        }
    }

    private fun storeEventData(event: MotionEvent) {
        prevX = event.x
        prevY = event.y
        prevEventTs = System.currentTimeMillis()
    }

    private fun calculateSwipeOffset(event: MotionEvent) {
        val xOffset = (event.x - prevX) / width
        val yOffset = (event.y - prevY) / height
        listener?.onTouchpadMove(Offset(xOffset, yOffset))
    }

}
