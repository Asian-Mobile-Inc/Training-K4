package com.example.asian.retrofit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.asian.databinding.ItemListRetrofitBinding
import com.example.asian.retrofit.model.ImageModel

class ImageAdapter(private var mItemClickListener: ItemClickListener) :
    ListAdapter<ImageModel, ImageAdapter.ViewHolder>(ImageDiffCallback()) {
    class ImageDiffCallback : DiffUtil.ItemCallback<ImageModel>() {
        override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean =
            oldItem.imageId == newItem.imageId

        override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean =
            oldItem == newItem
    }

    interface ItemClickListener {
        fun onItemClick(imageModel: ImageModel)
        fun onItemLongClick(imageModel: ImageModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListRetrofitBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(getItem(position), mItemClickListener)
    }

    class ViewHolder(private var binding: ItemListRetrofitBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageModel: ImageModel, itemClickListener: ItemClickListener) {
            with(binding) {
                Glide
                    .with(context)
                    .load(imageModel.url)
                    .signature(ObjectKey(System.currentTimeMillis()))
                    .into(ivStorage)
                tvNameStorage.text = imageModel.url
                ivStorage.setOnClickListener {
                    itemClickListener.onItemClick(imageModel)
                }
            }
        }
    }
}