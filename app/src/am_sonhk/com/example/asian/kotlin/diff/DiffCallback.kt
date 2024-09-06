package com.example.asian.kotlin.diff

import androidx.recyclerview.widget.DiffUtil
import com.example.asian.kotlin.model.User

class UserDiffCallback(
    private val oldList: List<User>,
    private val newList: List<User>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Assuming User ID is the unique identifier for each user
        return oldList[oldItemPosition].userId == newList[newItemPosition].userId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Check if contents are the same (e.g. name and age are unchanged)
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}

