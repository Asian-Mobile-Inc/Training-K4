package com.example.asian

import androidx.recyclerview.widget.DiffUtil
import com.example.asian.model.User

class MyDiffUtilCallback(oldUsers: List<User>, newUsers: List<User>) : DiffUtil.Callback() {
    private val mOldUsers: List<User>
    private val mNewUsers: List<User>

    init {
        mOldUsers = oldUsers
        mNewUsers = newUsers
    }

    override fun getOldListSize(): Int {
        return mOldUsers.size
    }

    override fun getNewListSize(): Int {
        return mNewUsers.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUsers[oldItemPosition].userId
            .equals(mNewUsers[newItemPosition].userId)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser: User = mOldUsers[oldItemPosition]
        val newUser: User = mNewUsers[newItemPosition]
        return oldUser.equals(newUser)
    }
}