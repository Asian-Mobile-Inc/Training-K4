package com.example.asian.diff

import androidx.recyclerview.widget.DiffUtil
import com.example.asian.model.Picture

class PictureDiffCallBack(
    private val oldList: MutableList<Picture>, private val newList: MutableList<Picture>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].imageId == newList[newItemPosition].imageId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser.permalinkUrl == newUser.permalinkUrl && oldUser.url == newUser.url && oldUser.type == newUser.type && oldUser.thumbUrl == newUser.thumbUrl && oldUser.createdAt == newUser.createdAt && oldUser.favorite == newUser.favorite&& oldUser.downloaded == newUser.downloaded
    }
}
