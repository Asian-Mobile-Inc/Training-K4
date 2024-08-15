package com.example.asian.diff

import androidx.recyclerview.widget.DiffUtil
import com.example.asian.model.User

class UserDiffUtilCallback(
    private val oldList: MutableList<User>, private val newList: MutableList<User>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].userId == newList[newItemPosition].userId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser.userName.equals(newUser) && oldUser.age == newUser.age
    }
}
