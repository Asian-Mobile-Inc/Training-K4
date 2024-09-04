package com.example.asian.kotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.asian.databinding.UserItemBinding
import com.example.asian.kotlin.diff.UserDiffUtilCallback
import com.example.asian.kotlin.model.User

class UserAdapter(
    private val context: Context,
    private val onClick: (User) -> Unit,
    private val onDelete: (User) -> Unit,
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var mUserList = ArrayList<User>()

    inner class UserViewHolder(val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = mUserList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        with(holder) {
            with(mUserList[position]) {
                binding.tvUserId.text = this.mUserId.toString()
                binding.tvUserName.text = this.mUserName
                binding.tvUserAge.text = this.mAge
            }
        }
    }

    private fun updateData(newList: List<User>) {
        val diffCallback = UserDiffUtilCallback(mUserList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        mUserList.clear()
        mUserList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

}

