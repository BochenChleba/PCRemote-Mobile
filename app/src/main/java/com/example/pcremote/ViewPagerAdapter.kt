package com.example.pcremote

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.pcremote.ui.power_control.PowerControlFragment

class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PowerControlFragment.newInstance()
            1 -> PowerControlFragment.newInstance()
            else -> throw Exception("Invalid fragment position")
        }
    }

    override fun getCount() = 2
}