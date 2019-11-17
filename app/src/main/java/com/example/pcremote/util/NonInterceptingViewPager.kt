package com.example.pcremote.util

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NonInterceptingViewPager(context: Context, attributeSet: AttributeSet): ViewPager(context, attributeSet) {
    override fun onInterceptTouchEvent(ev: MotionEvent?) = false
}