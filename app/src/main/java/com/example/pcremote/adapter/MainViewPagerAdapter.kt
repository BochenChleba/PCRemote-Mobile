package com.example.pcremote.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.pcremote.ui.fragment.keyboard.KeyboardFragment
import com.example.pcremote.ui.fragment.power_control.PowerControlFragment
import com.example.pcremote.ui.fragment.touchpad.TouchpadFragment
import com.example.pcremote.ui.fragment.volume.VolumeControlFragment

class MainViewPagerAdapter(fragmentManager: FragmentManager)
    : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = listOf(
        PowerControlFragment.newInstance(),
        VolumeControlFragment.newInstance(),
        TouchpadFragment.newInstance(),
        KeyboardFragment.newInstance()
    )

    override fun getItem(position: Int) = fragments[position]
    override fun getCount() = fragments.size
}