package com.example.asian.issuethirteen.adapter

private const val NAME_MY_FAVOURITE = "My favourite"
private const val NAME_GALLERY = "Gallery"

enum class TabName(val realName: String) {
    MY_GALLERY(NAME_MY_FAVOURITE),
    GALLERY(NAME_GALLERY);

    companion object {
        fun getName(position: Int): String {
            return when (position) {
                0 -> MY_GALLERY.realName
                else -> GALLERY.realName
            }
        }
    }
}
