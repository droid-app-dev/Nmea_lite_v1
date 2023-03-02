package com.accord.nmea.ui.live

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.accord.nmea.ui.log.LogFragment
import com.accord.nmea.ui.sky.SkyviewFragment

class ViewpagerAdapter(fragment: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragment, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> LiveFragment()
            1 -> SkyviewFragment()
            2 -> LogFragment()
            else -> Fragment()
        }
    }


}