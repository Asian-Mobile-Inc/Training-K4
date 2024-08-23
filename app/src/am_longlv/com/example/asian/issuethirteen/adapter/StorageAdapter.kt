package com.example.asian.issuethirteen.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asian.R
import com.example.asian.databinding.ItemListStorageBinding
import com.example.asian.issuethirteen.model.StorageModel

class StorageAdapter(private var mItemClickListener: ItemClickListener) :
    ListAdapter<StorageModel, StorageAdapter.ViewHolder>(UserDiffCallback()) {
    class UserDiffCallback : DiffUtil.ItemCallback<StorageModel>() {
        override fun areItemsTheSame(oldItem: StorageModel, newItem: StorageModel): Boolean =
            oldItem.storageUri == newItem.storageUri

        override fun areContentsTheSame(oldItem: StorageModel, newItem: StorageModel): Boolean =
            oldItem == newItem && oldItem.isSelected == newItem.isSelected

        override fun getChangePayload(oldItem: StorageModel, newItem: StorageModel): Any? {
            return if (oldItem != newItem) {
                super.getChangePayload(oldItem, newItem)
            } else {
                1
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(storage: StorageModel)
        fun onItemLongClick(storage: StorageModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemListStorageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent.context
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), mItemClickListener)

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            Log.d("androidruntime", "sjss")
            holder.bindBackground(getItem(position), mItemClickListener)
        }

    }

    class ViewHolder(
        private var binding: ItemListStorageBinding,
        private val context: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storage: StorageModel, itemClickListener: ItemClickListener) {
            Glide
                .with(context)
                .load(storage.storageUri)
                .into(binding.ivStorage)
            binding.tvNameStorage.text = storage.storageName
            if (storage.isSelected) {
                binding.ivChecked.visibility = View.VISIBLE
                binding.clItemStorage.setBackgroundResource(R.drawable.bg_item_storage_selected)
            } else {
                binding.ivChecked.visibility = View.GONE
                binding.clItemStorage.setBackgroundResource(0)
            }
            binding.clItemStorage.setOnClickListener {
                itemClickListener.onItemClick(storage)
            }
            binding.clItemStorage.setOnLongClickListener {
                itemClickListener.onItemLongClick(storage)
                return@setOnLongClickListener true
            }
        }

        fun bindBackground(storage: StorageModel, itemClickListener: ItemClickListener) {
            if (storage.isSelected) {
                binding.ivChecked.visibility = View.VISIBLE
                binding.clItemStorage.setBackgroundResource(R.drawable.bg_item_storage_selected)
            } else {
                binding.ivChecked.visibility = View.GONE
                binding.clItemStorage.setBackgroundResource(0)
            }
            binding.clItemStorage.setOnClickListener {
                itemClickListener.onItemClick(storage)
            }
            binding.clItemStorage.setOnLongClickListener {
                itemClickListener.onItemLongClick(storage)
                return@setOnLongClickListener true
            }
        }
    }
}
