package com.example.asian.issuethirteen.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asian.databinding.ItemListStorageBinding
import com.example.asian.issuethirteen.model.StorageModel

class StorageAdapter(private var mItemClickListener: ItemClickListener) :
    ListAdapter<StorageModel, StorageAdapter.ViewHolder>(UserDiffCallback()) {
    class UserDiffCallback : DiffUtil.ItemCallback<StorageModel>() {
        override fun areItemsTheSame(oldItem: StorageModel, newItem: StorageModel): Boolean =
            oldItem.storageId == newItem.storageId

        override fun areContentsTheSame(oldItem: StorageModel, newItem: StorageModel): Boolean =
            oldItem == newItem

    }

    interface ItemClickListener {
        fun onItemClick(storage: StorageModel)
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

    class ViewHolder(private var binding: ItemListStorageBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storage: StorageModel, itemClickListener: ItemClickListener) {
            Glide
                .with(context)
                .load(storage.storageUri)
                .into(binding.ivStorage)
            binding.tvNameStorage.text = storage.storageName
            binding.clItemStorage.setOnClickListener {
                itemClickListener.onItemClick(storage)
            }
        }
    }
}
