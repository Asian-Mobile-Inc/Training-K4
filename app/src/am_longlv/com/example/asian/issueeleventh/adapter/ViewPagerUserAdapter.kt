package com.example.asian.issueeleventh.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.asian.issueeleventh.fragment.UserInfoFragment

private const val NUM_PAGES = 2

class ViewPagerUserAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return UserInfoFragment.newInstance(position)
    }

    override fun getItemCount(): Int {
        return NUM_PAGES
    }
}