package com.example.asian.retrofit.adapter

private const val NAME_RETROFIT = "RETROFIT"
private const val NAME_LOCAL = "LOCAL"
private const val NAME_FAVOURITE = "FAVOURITE"

enum class TabName(val realName: String, val position: Int) {
    RETROFIT(NAME_RETROFIT, 0),
    LOCAL(NAME_LOCAL, 1),
    FAVOURITE(NAME_FAVOURITE, 2);

    companion object {
        fun getName(position: Int): String {
            return when (position) {
                0 -> RETROFIT.realName
                1 -> LOCAL.realName
                else -> FAVOURITE.realName
            }
        }
    }
}
