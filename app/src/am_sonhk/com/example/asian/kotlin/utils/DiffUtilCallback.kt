package com.example.asian.kotlin.utils

import androidx.recyclerview.widget.DiffUtil

import com.example.asian.kotlin.model.User


class DiffUtilCallback(
    private var newList: MutableList<User>,
    private var oldList: MutableList<User>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].userId == oldList[oldItemPosition].userId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].userName == oldList[oldItemPosition].userName
    }
}