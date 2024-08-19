package com.example.asian.issueeleventh.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.asian.R
import com.example.asian.databinding.ItemListUserInfoBinding
import com.example.asian.issueeleventh.model.UserInfo

class UserAdapter(private var mItemClickListener: ItemClickListener) :
    ListAdapter<UserInfo, UserAdapter.ViewHolder>(UserDiffCallback()) {
    class UserDiffCallback : DiffUtil.ItemCallback<UserInfo>() {
        override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean =
            oldItem.userId == newItem.userId

        override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean =
            oldItem == newItem
    }

    interface ItemClickListener {
        fun onDeleteClick(user: UserInfo)
        fun onEditClick(user: UserInfo)
        fun onFavouriteClick(user: UserInfo)
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
        holder.bind(getItem(position), mItemClickListener)

    class ViewHolder(private var binding: ItemListUserInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserInfo, itemClickListener: ItemClickListener) {
            binding.tvTitleUID.text = user.userId.toString()
            binding.tvTitleUN.text = user.userName
            binding.tvTitleUA.text = user.userAge.toString()
            if (user.userFavourite){
                binding.btnFavourite.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
            }else{
                binding.btnFavourite.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
            }
            binding.btnDelete.setOnClickListener {
                itemClickListener.onDeleteClick(user)
            }
            binding.btnEdit.setOnClickListener {
                itemClickListener.onEditClick(user)
            }
            binding.btnFavourite.setOnClickListener {
                itemClickListener.onFavouriteClick(user)
            }
        }
    }
}
