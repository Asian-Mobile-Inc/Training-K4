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
    private val onLongClick: (Picture) -> Unit,
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
            Glide.with(context).load(picture.path).into(binding.ivPicture)
            with(binding) {
                tvNamePicture.text = picture.name
                root.setOnClickListener { onClick(picture) }
                ivSelected.isVisible = picture.isSelected
                root.setOnLongClickListener {
                    onLongClick(picture)
                    return@setOnLongClickListener true
                }
                if (picture.favorite) {
                    ivFavorite.setImageResource(R.drawable.ic_favorite)
                } else {
                    ivFavorite.setImageResource(R.drawable.ic_un_favorite)
                }
                root.isSelected = picture.isSelected
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
