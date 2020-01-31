package com.example.pcremote.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.pcremote.ui.fragment.base.BaseFragment
import com.example.pcremote.ui.fragment.power_control.PowerControlFragment
import com.example.pcremote.ui.fragment.touchpad.TouchpadFragment
import com.example.pcremote.ui.fragment.volume.VolumeControlFragment

class MainViewPagerAdapter(fragmentManager: FragmentManager)
    : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val fragments = listOf(
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

    inline fun <reified T: BaseFragment> getFragmentInstance()
            = fragments.filterIsInstance<T>().firstOrNull()

    override fun getItem(position: Int) = fragments[position]
    override fun getCount() = fragments.size
    override fun getPageTitle(position: Int) = titles[position]
}