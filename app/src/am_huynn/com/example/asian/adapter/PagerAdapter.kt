package com.example.asian.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.asian.fragment.UsersFragment

class PagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val itemCount = 2

    override fun getItemCount(): Int {
        return itemCount
    }

    override fun createFragment(position: Int): Fragment {
        return UsersFragment(position)
    }
}
