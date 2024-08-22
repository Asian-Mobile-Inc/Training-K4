package com.example.asian.issuethirteen.adapter

import com.example.asian.R

enum class TabName(val realName: String, val icon: Int) {
    MY_GALLERY("My favourite", R.drawable.ic_email),
    GALLERY("Gallery", R.drawable.ic_email);

    companion object {
        fun getName(position: Int): String {
            return when (position) {
                0 -> MY_GALLERY.realName
                else -> GALLERY.realName
            }
        }
        fun getIcon(position: Int): Int {
            return when (position) {
                0 -> MY_GALLERY.icon
                else -> GALLERY.icon
            }
        }
    }
}
