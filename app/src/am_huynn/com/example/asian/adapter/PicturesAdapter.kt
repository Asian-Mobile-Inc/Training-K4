package com.example.asian.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asian.R
import com.example.asian.databinding.ItemGridImageBinding
import com.example.asian.diff.PictureDiffCallBack
import com.example.asian.model.Picture

class PicturesAdapter(
    private val onClick: (Picture) -> Unit,
    private val onFavorite: (Picture) -> Unit,
    private val onDownLoad: (Picture) -> Unit,
    private val canDownload: Boolean
) : RecyclerView.Adapter<PicturesAdapter.PictureViewHolder>() {
    private val pictures: MutableList<Picture> = mutableListOf()

    fun setData(newList: MutableList<Picture>) {
        val diffCallback = PictureDiffCallBack(pictures, newList)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        pictures.clear()
        pictures.addAll(newList)
        diffCourses.dispatchUpdatesTo(this)
    }

    inner class PictureViewHolder(
        private val binding: ItemGridImageBinding, private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(picture: Picture) {
            with(binding) {
                Glide.with(context).load(picture.url).placeholder(R.drawable.progress_animation)
                    .into(ivPicture)
                btnFavorite.setOnClickListener { onFavorite(picture) }
                if (picture.favorite) {
                    btnFavorite.setImageResource(R.drawable.ic_favorite)
                } else {
                    btnFavorite.setImageResource(R.drawable.ic_un_favorite)
                }
                if (picture.downloaded) {
                    btnDownload.setImageResource(R.drawable.ic_downloaded)
                } else {
                    btnDownload.setImageResource(R.drawable.ic_download)
                }
                btnDownload.isEnabled = !picture.downloaded
                btnDownload.isVisible = canDownload
                btnDownload.setOnClickListener { onDownLoad(picture) }
                root.setOnClickListener { onClick(picture) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val binding: ItemGridImageBinding =
            ItemGridImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PictureViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return pictures.size
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(pictures[position])
    }
}
