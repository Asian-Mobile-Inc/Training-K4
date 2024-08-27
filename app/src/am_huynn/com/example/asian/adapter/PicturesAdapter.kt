package com.example.asian.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asian.databinding.ItemGridImageBinding
import com.example.asian.model.Picture

class PicturesAdapter() : RecyclerView.Adapter<PicturesAdapter.PictureViewHolder>() {
    private val pictures: MutableList<Picture> = mutableListOf()

    inner class PictureViewHolder(
        private val binding: ItemGridImageBinding, private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(picture: Picture) {
            Glide.with(context).load(picture.uri).into(binding.ivPicture)
            binding.tvNamePicture.text = picture.name
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
