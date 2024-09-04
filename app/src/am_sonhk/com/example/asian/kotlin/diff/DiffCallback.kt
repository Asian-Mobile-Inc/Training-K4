package com.example.asian.kotlin.diff

import androidx.recyclerview.widget.DiffUtil
import com.example.asian.kotlin.model.User

class UserDiffUtilCallback(
    private val oldList: List<User>,
    private val newList: List<User>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].mUserId == newList[newItemPosition].mUserId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].mUserId == newList[newItemPosition].mUserId -> true
            oldList[oldItemPosition].mUserName == newList[newItemPosition].mUserName -> true
            oldList[oldItemPosition].mAge == newList[newItemPosition].mAge -> true
            else -> false
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}

