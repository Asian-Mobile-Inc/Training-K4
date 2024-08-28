package com.example.asian.retrofit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.asian.databinding.ItemListIssue5Binding
import com.example.asian.retrofit.model.ImageModel

class ImageAdapter : ListAdapter<ImageModel, ImageAdapter.ViewHolder>(ImageDiffCallback()) {
    class ImageDiffCallback : DiffUtil.ItemCallback<ImageModel>() {
        override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean =
            oldItem.imageId == newItem.imageId

        override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ViewHolder {
        return ViewHolder(
            ItemListIssue5Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    class ViewHolder(private var binding: ItemListIssue5Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageModel: ImageModel) {

        }
    }
}