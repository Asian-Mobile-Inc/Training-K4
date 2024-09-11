package com.example.asian.retrofit.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asian.databinding.ItemListRetrofitBinding
import com.example.asian.retrofit.model.ImageModel

class ImageAdapter(private var itemClickListener: ItemClickListener, private var tab: Int) :
    ListAdapter<ImageModel, ImageAdapter.ViewHolder>(ImageDiffCallback()) {
    class ImageDiffCallback : DiffUtil.ItemCallback<ImageModel>() {
        override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
            return oldItem.imageId == newItem.imageId
        }

        override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
            return oldItem == newItem
                    && oldItem.isFavourite == newItem.isFavourite
                    && oldItem.isDownloaded == newItem.isDownloaded
        }

        override fun getChangePayload(oldItem: ImageModel, newItem: ImageModel): Any? {
            return if (oldItem != newItem) {
                super.getChangePayload(oldItem, newItem)
            } else {
                1
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(imageModel: ImageModel)
        fun onItemLongClick(imageModel: ImageModel)
        fun onBtnFavouriteClick(imageModel: ImageModel)
        fun onBtnDownloadClick(imageModel: ImageModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListRetrofitBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent.context, tab
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(getItem(position), itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            return holder.initListener(getItem(position), itemClickListener)
        }
    }

    class ViewHolder(
        private val binding: ItemListRetrofitBinding,
        private val context: Context,
        private val tab: Int,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageModel: ImageModel, itemClickListener: ItemClickListener) {
            with(binding) {
                Glide
                    .with(context)
                    .load(imageModel.url)
                    .into(ivStorage)
                tvNameStorage.text = imageModel.imageId
            }
            initListener(imageModel, itemClickListener)
        }

        fun initListener(
            imageModel: ImageModel,
            itemClickListener: ItemClickListener,
        ) {
            with(binding) {
                if (tab != TabName.RETROFIT.position) {
                    btnDownload.visibility = View.GONE
                } else {
                    ivStorage.setOnLongClickListener {
                        itemClickListener.onItemLongClick(imageModel)
                        return@setOnLongClickListener true
                    }
                }
                ivStorage.setOnClickListener {
                    itemClickListener.onItemClick(imageModel)
                }
                if (imageModel.isFavourite) {
                    btnFavourite.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
                } else {
                    btnFavourite.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY)
                }
                if (imageModel.isDownloaded) {
                    btnDownload.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
                } else {
                    btnDownload.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY)
                }
                btnFavourite.setOnClickListener {
                    itemClickListener.onBtnFavouriteClick(imageModel)
                }
                btnDownload.setOnClickListener {
                    itemClickListener.onBtnDownloadClick(imageModel)
                }
            }
        }
    }
}
