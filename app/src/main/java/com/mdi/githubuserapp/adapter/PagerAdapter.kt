package com.mdi.githubuserapp.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mdi.githubuserapp.fragments.FollowerFragment
import com.mdi.githubuserapp.fragments.FollowingFragment

class PagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var fragment: Fragment? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> fragment = FollowerFragment()
            1 -> fragment = FollowingFragment()
        }
        return fragment as Fragment
    }
}