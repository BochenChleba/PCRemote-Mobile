package com.example.pcremote.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.pcremote.ui.page.power_control.PowerControlFragment

class MainViewPagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = listOf(
        PowerControlFragment.newInstance()
       // PowerControlFragment.newInstance()
    )

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size
}