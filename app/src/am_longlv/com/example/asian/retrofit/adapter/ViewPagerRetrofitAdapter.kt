package com.example.asian.retrofit.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.asian.retrofit.fragment.ImageRetrofitFragment

private const val NUM_PAGES = 3

class ViewPagerRetrofitAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return ImageRetrofitFragment.newInstance(position)
    }

    override fun getItemCount(): Int {
        return NUM_PAGES
    }
}
