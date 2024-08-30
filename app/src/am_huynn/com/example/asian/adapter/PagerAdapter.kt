package com.example.asian.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.asian.fragment.PicturesFragment
import com.example.asian.model.Picture

class PagerAdapter(
    fragmentActivity: FragmentActivity, private val onEdit: (Picture, String) -> Unit
) : FragmentStateAdapter(fragmentActivity) {
    private val itemCount = 2

    override fun getItemCount(): Int {
        return itemCount
    }

    override fun createFragment(position: Int): Fragment {
        return PicturesFragment(position, onEdit)
    }
}
