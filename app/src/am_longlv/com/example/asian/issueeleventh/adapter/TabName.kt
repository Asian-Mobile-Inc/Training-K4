package com.example.asian.issueeleventh.adapter

enum class TabName {
    ALL,
    FAVOURITE;

    companion object {
        fun getName(position: Int): String {
            return when (position) {
                0 -> ALL.name
                else -> FAVOURITE.name
            }
        }
    }
}

val TabName.id: Int
    get() {
        return when (this.name) {
            TabName.ALL.name -> 0
            TabName.FAVOURITE.name -> 1
            else -> 3
        }
    }