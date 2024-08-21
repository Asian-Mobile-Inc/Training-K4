package com.example.asian.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.asian.R
import com.example.asian.databinding.UserItemViewBinding
import com.example.asian.diff.UserDiffUtilCallback
import com.example.asian.model.User

class UserAdapter(
    private val onUpdate: (User) -> Unit,
    private val onDelete: (User) -> Unit,
    private val onFavorite: (User) -> Unit,
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var mUsers: MutableList<User> = mutableListOf()

    fun setUsers(newUsers: MutableList<User>) {
        val diffCallback = UserDiffUtilCallback(mUsers, newUsers)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        mUsers.clear()
        mUsers.addAll(newUsers)
        diffCourses.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: UserItemViewBinding =
            UserItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mUsers[position])
    }

    inner class ViewHolder(private val binding: UserItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                tvUserItemId.text = user.userId.toString()
                tvUserItemName.text = user.userName
                tvUserItemAge.text = user.age.toString()
                if (user.favorite) {
                    btnFavorite.setImageResource(R.drawable.ic_favorite)
                } else {
                    btnFavorite.setImageResource(R.drawable.ic_un_favorite)
                }
                btnDeleteItemUser.setOnClickListener { onDelete(user) }
                btnEditItemUser.setOnClickListener { onUpdate(user) }
                btnFavorite.setOnClickListener { onFavorite(user) }
            }
        }
    }
}
