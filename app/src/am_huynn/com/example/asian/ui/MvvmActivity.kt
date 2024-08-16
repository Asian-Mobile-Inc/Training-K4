package com.example.asian.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asian.R
import com.example.asian.adapter.UserAdapter
import com.example.asian.databinding.ActivityMvvmBinding
import com.example.asian.databinding.DialogLayoutBinding
import com.example.asian.model.User
import com.example.asian.viewmodel.UserViewModel

class MvvmActivity : AppCompatActivity() {
    private val binding: ActivityMvvmBinding by lazy {
        ActivityMvvmBinding.inflate(layoutInflater)
    }

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(
            this, UserViewModel.UserViewModelFactory(this.application)
        )[UserViewModel::class.java]
    }

    private val userAdapter: UserAdapter by lazy {
        UserAdapter(this, onUpdateUser, onDeleteUser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initAdapter()
        initListener()
    }

    private fun initAdapter() {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = userAdapter
        userViewModel.getIsLoadingObserver().observe(this) {
            if (it == true) {
                binding.pbProgress.visibility = View.VISIBLE
            } else {
                binding.pbProgress.visibility = View.INVISIBLE
            }
        }
        userViewModel.getAllUserObserver().observe(this) {
            userAdapter.setUsers(it)
        }
    }

    private fun initListener() {
        binding.btnAdd.setOnClickListener {
            onClickAddUser()
        }

        binding.btnDeleteAll.setOnClickListener {
            userViewModel.deleteAllUsers()
        }
    }

    private fun onClickAddUser() {
        val name = binding.edtName.text.toString()
        val age = binding.edtAge.text.toString()
        val errorName: String? = userViewModel.validatorName(name)
        val errorAge: String? = userViewModel.validatorAge(age)
        if (errorName != null) {
            binding.edtName.error = errorName
            return
        } else if (errorAge != null) {
            binding.edtAge.error = errorAge
            return
        } else {
            val user = User(name, age.toInt())
            userViewModel.insertUser(user)
            binding.edtName.text = null
            binding.edtAge.text = null
            hideKeyboard()
        }
    }

    private val onDeleteUser: (User) -> Unit = {
        userViewModel.deleteUser(it)
    }

    private val onUpdateUser: (User) -> Unit = {
        showEditDialog(it)
    }

    private fun showEditDialog(user: User) {
        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = DialogLayoutBinding.inflate(LayoutInflater.from(this))
        val dialogLayout = dialogBinding.root

        dialog.apply {
            setTitle(resources.getText(R.string.this_is_dialog))
            setView(dialogLayout)
            with(dialogBinding) {
                edtEditName.setText(user.userName)
                edtEditName.setSelection(edtEditName.length())
                btnCancel.setOnClickListener {
                    dismiss()
                }
                btnConfirmEdit.setOnClickListener {
                    val errorName: String? =
                        userViewModel.validatorName(edtEditName.text.toString())
                    if (errorName != null) {
                        edtEditName.error = errorName
                    } else {
                        user.userName = edtEditName.text.toString()
                        userViewModel.updateUser(user)
                        dismiss()
                    }
                }
            }
        }.show()
    }

    private fun hideKeyboard() {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            with(binding) {
                edtName.clearFocus()
                edtAge.clearFocus()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard()
        return super.dispatchTouchEvent(ev)
    }
}
