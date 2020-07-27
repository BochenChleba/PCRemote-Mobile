package com.example.pcremote.view.touchpad

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.pcremote.data.dto.Offset
import kotlin.math.abs

private const val UNSET_FLOAT = -1f
private const val LONG_CLICK_TIME = 500L
private const val MIN_OFFSET = 0.0018

class TouchpadView(context: Context, attributeSet: AttributeSet)
    : View(context, attributeSet) {

    var listener: ITouchpadView? = null
    var isDisabled = false
    private var prevX = UNSET_FLOAT
    private var prevY = UNSET_FLOAT
    private var viableForClickEvent = true
    private var longClickRunnable = Runnable {
        listener?.onTouchpadLongClick()
        viableForClickEvent = false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        if (isDisabled) {
            return true
        }
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
        prevX = event.x
        prevY = event.y
        handler.postDelayed(longClickRunnable, LONG_CLICK_TIME)
    }

    private fun actionMove(event: MotionEvent) {
        handler.removeCallbacks(longClickRunnable)
        viableForClickEvent = false
        calculateSwipeOffset(event)
    }

    private fun actionUp() {
        if (viableForClickEvent) {
            handler.removeCallbacks(longClickRunnable)
            listener?.onTouchpadClick()
        } else {
            viableForClickEvent = true
        }
    }

    private fun calculateSwipeOffset(event: MotionEvent) {
        var xOffset = (event.x - prevX) / width
        var yOffset = (event.y - prevY) / height
        if (abs(xOffset) < MIN_OFFSET) {
            xOffset = 0f
        }
        if (abs(yOffset) < MIN_OFFSET) {
            yOffset = 0f
        }
        if (xOffset != 0f || yOffset != 0f) {
            listener?.onTouchpadMove(
                Offset(
                    xOffset,
                    yOffset
                )
            )
        }
        prevX = event.x
        prevY = event.y
    }

}
