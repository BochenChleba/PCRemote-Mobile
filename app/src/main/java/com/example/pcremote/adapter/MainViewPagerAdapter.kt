package com.example.pcremote.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.pcremote.ui.page.power_control.PowerControlFragment
import com.example.pcremote.ui.page.touchpad.TouchpadFragment
import com.example.pcremote.ui.page.volume.VolumeControlFragment

class MainViewPagerAdapter(fragmentManager: FragmentManager)
    : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = listOf(
        TouchpadFragment.newInstance(),
        PowerControlFragment.newInstance(),
        VolumeControlFragment.newInstance(),
        TouchpadFragment.newInstance(),
        PowerControlFragment.newInstance(),
        VolumeControlFragment.newInstance()
    )

    private val titles = listOf(
        "Touch Pad",
        "Power",
        "Volume",
        "Touch Pad",
        "Power",
        "Volume"
    )

    override fun getItem(position: Int) = fragments[position]
    override fun getCount() = fragments.size
    override fun getPageTitle(position: Int) = titles[position]
}