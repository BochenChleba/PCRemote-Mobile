package com.example.pcremote.util

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewpager.widget.ViewPager
import com.example.pcremote.R
import com.google.android.material.tabs.TabLayout
import org.jetbrains.anko.allCaps
import org.jetbrains.anko.padding
import org.jetbrains.anko.textColor

private const val TAB_TEXT_SIZE = 14f
private const val TAB_SCALE_FACTOR = 1.2f
private const val TAB_ALPHA_FACTOR = 0.5f
private const val TAB_PADDING = 20
private const val DEFAULT_SCALE = 1f
private const val DEFAULT_ALPHA = 1f

class CustomizableTabLayout : TabLayout {
    private lateinit var titles: Array<String>
    private var unselectedTypeFace: Typeface? = null

    constructor(context: Context?) : super(context) {
        init(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        addOnTabSelectedListener()
    }

        private fun addOnTabSelectedListener() {
        addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: Tab?) {
            }

            override fun onTabUnselected(tab: Tab?) {
                val view = tab?.customView
                if (view is AppCompatTextView) {
                    view.typeface = unselectedTypeFace
                    view.textColor = context.getColor(R.color.almostWhite)
                    view.animateOnUnselection()
                }
            }

            override fun onTabSelected(tab: Tab?) {
                val view = tab?.customView
                if (view is AppCompatTextView) {
                    view.setTypeface(view.typeface, Typeface.BOLD)
                    view.textColor = context.getColor(R.color.orange)
                    view.animateOnSelection()
                }
            }
        })
    }

    private fun View.animateOnUnselection() =
        this.animate().scaleX(DEFAULT_SCALE).scaleY(DEFAULT_SCALE).alpha(TAB_ALPHA_FACTOR).start()

    private fun View.animateOnSelection() =
        this.animate().scaleX(TAB_SCALE_FACTOR).scaleY(TAB_SCALE_FACTOR).alpha(DEFAULT_ALPHA).start()

    override fun setupWithViewPager(viewPager: ViewPager?, autoRefresh: Boolean) {
        super.setupWithViewPager(viewPager, autoRefresh)
        addViews()
        setTitlesAtTabs()
    }

    private fun addViews() {
        for (i in 0 until tabCount) {
            val tab = getTabAt(i)
            if (tab != null) {
                val textView = createCustomTextView()
                if (i == 0) {
                    if (unselectedTypeFace == null) {
                        unselectedTypeFace = textView.typeface
                    }
                    textView.setTypeface(textView.typeface, Typeface.BOLD)
                    textView.textColor = context.getColor(R.color.orange)
                    textView.animateOnSelection()
                }
                tab.customView = textView
            }
        }
    }

    private fun createCustomTextView(): AppCompatTextView {
        val textView = AppCompatTextView(context)
        textView.gravity = Gravity.CENTER
        textView.allCaps = true
        textView.textSize = TAB_TEXT_SIZE
        textView.textColor = context.getColor(R.color.almostWhite)
        textView.alpha = TAB_ALPHA_FACTOR
        textView.padding = TAB_PADDING
        return textView
    }

    private fun setTitlesAtTabs() {
        titles = context?.resources?.getStringArray(R.array.tabs) ?: emptyArray()
        if (titles.size < tabCount) {
            return
        }
        for (i in 0 until tabCount) {
            val tab = getTabAt(i)
            if (tab != null) {
                val view = tab.customView
                if (view is AppCompatTextView) {
                    view.text = titles[i]
                }
            }
        }
    }
}