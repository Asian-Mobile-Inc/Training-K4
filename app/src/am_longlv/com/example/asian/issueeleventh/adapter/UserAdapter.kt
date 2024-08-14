package com.example.asian.issueeleventh.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.asian.databinding.ItemListUserInfoBinding
import com.example.asian.issueeleventh.model.UserInfo

class UserAdapter(private var itemClickListener: ItemClickListener) :
    ListAdapter<UserInfo, UserAdapter.ViewHolder>(UserDiffCallback()) {
    class UserDiffCallback : DiffUtil.ItemCallback<UserInfo>() {
        override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean =
            oldItem.userId == newItem.userId

        override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
            Log.d(
                "androidruntime",
                (oldItem == newItem).toString() + " " + oldItem.userName + " " + newItem.userName
            )
            return oldItem == newItem
        }

    }

    interface ItemClickListener {
        fun onDeleteClick(user: UserInfo)
        fun onEditClick(user: UserInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemListUserInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), itemClickListener)


    class ViewHolder(private var binding: ItemListUserInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserInfo, itemClickListener: ItemClickListener) {
            binding.tvTitleUID.text = user.userId.toString()
            binding.tvTitleUN.text = user.userName
            binding.tvTitleUA.text = user.userAge.toString()
            binding.btnDelete.setOnClickListener {
                itemClickListener.onDeleteClick(user)
            }
            binding.btnEdit.setOnClickListener {
                itemClickListener.onEditClick(user)
            }
        }
    }
}
