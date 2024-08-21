package com.example.asian.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asian.R
import com.example.asian.adapter.UserAdapter
import com.example.asian.databinding.DialogLayoutBinding
import com.example.asian.databinding.FragmentUsersBinding
import com.example.asian.model.User
import com.example.asian.viewmodel.UserViewModel

class UsersFragment(private val position: Int) : Fragment() {
    private lateinit var binding: FragmentUsersBinding

    private val userAdapter: UserAdapter by lazy {
        UserAdapter(onUpdateUser, onDeleteUser, onFavorite)
    }

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        initControl()
        initObserver()
        return binding.root
    }

    private fun initControl() {
        binding.rvRecyclerAllUsers.layoutManager = LinearLayoutManager(context)
        binding.rvRecyclerAllUsers.adapter = userAdapter
        binding.rvRecyclerAllUsers.itemAnimator = null
    }

    private fun initObserver() {
        userViewModel.isLoadingLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.pbProgressUsers.visibility = View.VISIBLE
            } else {
                binding.pbProgressUsers.visibility = View.GONE
            }
        }

        userViewModel.allUsers.observe(viewLifecycleOwner) {
            if (position == 0) {
                userAdapter.setUsers(it)
            } else {
                val favorites: MutableList<User> = mutableListOf()
                for (u in it) {
                    if (u.favorite) {
                        favorites.add(u)
                    }
                }
                userAdapter.setUsers(favorites)
            }
        }
    }

    private val onDeleteUser: (User) -> Unit = {
        userViewModel.deleteUser(it)
    }

    private val onFavorite: (User) -> Unit = {
        userViewModel.favoriteUser(it)
    }

    private val onUpdateUser: (User) -> Unit = {
        showEditDialog(it)
    }

    private fun showEditDialog(user: User) {
        val dialog = AlertDialog.Builder(context).create()
        val dialogBinding = DialogLayoutBinding.inflate(LayoutInflater.from(context))
        val dialogLayout = dialogBinding.root

        dialog.apply {
            setTitle(resources.getText(R.string.this_is_dialog))
            setView(dialogLayout)
            with(dialogBinding) {
                edtEditName.setText(user.userName)
                edtEditName.setSelection(edtEditName.length())
                edtEditAge.setText(user.age.toString())
                btnCancel.setOnClickListener {
                    dismiss()
                }
                btnConfirmEdit.setOnClickListener {
                    val errorName: String? =
                        userViewModel.validatorName(edtEditName.text.toString())
                    val errorAge: String? = userViewModel.validatorAge(edtEditAge.text.toString())
                    if (errorName != null) {
                        edtEditName.error = errorName
                    } else if (errorAge != null) {
                        edtEditAge.error = errorAge
                    } else {
                        user.userName = edtEditName.text.toString()
                        user.age = edtEditAge.text.toString().toInt()
                        userViewModel.updateUser(user)
                        dismiss()
                    }
                }
            }
        }.show()
    }
}
