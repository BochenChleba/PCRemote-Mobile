package com.example.pcremote.adapter

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.pcremote.R
import com.example.pcremote.ui.dialog.schedlued_shutdown.countdown.ShutdownCountdownFragment
import com.example.pcremote.ui.dialog.schedlued_shutdown.specified.ShutdownSpecifiedFragment

class ShutdownViewPagerAdapter(fragmentManager: FragmentManager, context: Context)
    : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = listOf(
        Pair(ShutdownCountdownFragment.newInstance(), context.getString(R.string.tab_name_shutdown_countdown)),
        Pair(ShutdownSpecifiedFragment.newInstance(), context.getString(R.string.tab_name_shutdown_specified_time))
    )

    override fun getItem(position: Int) = fragments[position].first

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int): CharSequence? = fragments[position].second
}
