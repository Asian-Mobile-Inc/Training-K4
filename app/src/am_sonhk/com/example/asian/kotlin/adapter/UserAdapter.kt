package com.example.asian.kotlin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.asian.databinding.UserItemBinding
import com.example.asian.kotlin.model.User

class UserAdapter(
    private val deleteUser: (User) -> Unit,
    private val updateUser: (User) -> Unit
) : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user, deleteUser, updateUser)

    }

    class UserViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, deleteUser: (User) -> Unit, updateUser: (User) -> Unit) {
            binding.tvUserId.text = user.userId.toString()
            binding.tvUserName.text = user.userName
            binding.tvUserAge.text = user.userAge.toString()
            binding.btnDeleteUser.setOnClickListener {
                deleteUser(user)
            }
            binding.btnEditUser.setOnClickListener {
                updateUser(user.copy())
            }
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}

