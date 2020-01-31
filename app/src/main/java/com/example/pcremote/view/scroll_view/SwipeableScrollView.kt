package com.example.pcremote.view.scroll_view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView
import kotlin.math.abs

private const val MIN_DISTANCE = 125

class SwipeableScrollView(context: Context, attributeSet: AttributeSet)
    : ScrollView(context, attributeSet) {

    private var downX = 0f
    private var downY = 0f
    private var upX = 0f
    private var upY = 0f
    var swipeListener: ISwipeableViewPager? = null
    var eventIntercepted = false

    init {
        requestDisallowInterceptTouchEvent(true)
    }

    /*override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }*/

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //if (eventIntercepted) { return false }
      //  super.onTouchEvent(event)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
            }
            MotionEvent.ACTION_UP -> {
                upX = event.x
                upY = event.y

                val deltaX = downX - upX
                val deltaY = downY - upY

                if(abs(deltaX) > MIN_DISTANCE){
                    if (deltaX < 0) {
                        swipeListener?.onSwipeLeft()
                        return true
                    }
                    if (deltaX > 0) {
                        swipeListener?.onSwipeRight()
                        return true
                    }
                }
                if(abs(deltaY) > MIN_DISTANCE){
                    if(deltaY < 0) {
                       // swipeListener?.onSwipeBottom()
                        return true
                    }
                    if(deltaY > 0) {
                       // swipeListener?.onSwipeTop()
                        return true
                    }
                }
            }
        }
        return false
    }
}