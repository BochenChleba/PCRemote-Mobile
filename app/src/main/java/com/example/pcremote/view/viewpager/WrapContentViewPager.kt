package com.example.pcremote.view.viewpager

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager


class WrapContentViewPager(context: Context, attrs: AttributeSet): ViewPager(context, attrs) {

    init {
        addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                requestLayout()
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        adapter?.let { offscreenPageLimit = it.count }
        var actualHeight = heightMeasureSpec

        getChildAt(currentItem)?.let { view ->
            view.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            actualHeight = MeasureSpec.makeMeasureSpec(view.measuredHeight, MeasureSpec.EXACTLY)
        }

        super.onMeasure(widthMeasureSpec, actualHeight)
    }

}