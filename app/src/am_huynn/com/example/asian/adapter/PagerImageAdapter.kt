package com.example.asian.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.asian.fragment.ImagesFragment
import com.example.asian.model.Picture

class PagerImageAdapter(
    fragmentActivity: FragmentActivity, private val onDownLoad: (Picture) -> Unit
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return ImagesFragment(position, onDownLoad)
    }
}