package com.example.asian.kotlin.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asian.R
import com.example.asian.databinding.DialogUpdateBinding
import com.example.asian.databinding.UserManaLayoutBinding
import com.example.asian.kotlin.adapter.UserAdapter
import com.example.asian.kotlin.model.User
import com.example.asian.kotlin.viewmodel.UserViewModel


class MainKotlinActivity : AppCompatActivity() {
    private lateinit var binding: UserManaLayoutBinding
    private lateinit var bindingDLG: DialogUpdateBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: UserAdapter

    private fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserManaLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        initControls()
        observation()
        userViewModel.getAllUsers()
    }

    private fun observation() {
        userViewModel.allUsers.observe(this) { list ->
            adapter.submitList(list)
        }
    }

    private fun initControls() {
        adapter = UserAdapter(onItemDelete, onItemUpdate)
        binding.rvUsers.adapter = adapter
        binding.rvUsers.layoutManager = LinearLayoutManager(this)

        binding.btnDeleteAllUsers.setOnClickListener {
            showDialog()
        }

        binding.btnShowAllUsers.setOnClickListener {
            userViewModel.getAllUsers()
        }

        binding.btnAddUser.setOnClickListener {
            addUser()
            it.hideKeyboard()
        }
    }

    private fun addUser() {
        val userName = binding.edtUserName.text.toString()
        val userAge = binding.edtUserAge.text.toString()
        when {
            userName.isEmpty() || userAge.isEmpty() -> {
                toast(getString(R.string.pls_enter_the_data))
            }

            !validateName(userName) || !validateAge(userAge) -> {
            }

            else -> {
                val user = User(userName = userName, userAge = userAge.toInt())
                userViewModel.addUser(user)
                toast(getString(R.string.user) + user.userName + getString(R.string.notiaddsucces))
                clearEdt()

            }
        }
    }

    private fun showUpdateDialog(user: User) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        bindingDLG = DialogUpdateBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDLG.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialog.apply {
            with(bindingDLG) {
                edtDLGUserName.setText(user.userName)
                edtDLGUserName.setSelection(edtDLGUserName.text.length)
                edtDLGUserAge.setText(user.userAge.toString())
                btnEditUser.setOnClickListener {
                    val name = edtDLGUserName.text.toString().trim()
                    val age = edtDLGUserAge.text.toString().trim()
                    when {
                        name.isEmpty() || age.isEmpty() -> {
                            toast(getString(R.string.pls_enter_the_data))
                        }

                        !validateName(name) || !validateAge(age) -> {
                        }

                        else -> {
                            user.userName = name
                            user.userAge = age.toInt()
                            userViewModel.updateUser(user)
                            toast(getString(R.string.update_success))
                            dialog.dismiss() // Close dialog
                        }
                    }
                }
                btnCancel.setOnClickListener {
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }


    private val onItemDelete: (User) -> Unit = { user ->
        val dialogClickListener =
            DialogInterface.OnClickListener { _: DialogInterface?, which: Int ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        userViewModel.deleteUser(user.userId)
                        toast(getString(R.string.delete_user_succes))
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.message_dialog_delete_item))
            .setPositiveButton(getString(R.string.yes), dialogClickListener)
            .setNegativeButton(getString(R.string.no), dialogClickListener).show()
    }


    private val onItemUpdate: (User) -> Unit = { user ->
        showUpdateDialog(user)
    }

    private fun showDialog() {
        val dialogClickListener =
            DialogInterface.OnClickListener { _: DialogInterface?, which: Int ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        userViewModel.deleteAllUsers()
                        toast(getString(R.string.deleteallsucces))
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.message_dialog_delete_all_item))
            .setPositiveButton(getString(R.string.yes), dialogClickListener)
            .setNegativeButton(getString(R.string.no), dialogClickListener).show()
    }

    private fun validateName(username: String) = if (username.length > MAX_NAME_LENGTH) {
        toast(getString(R.string.max_name_length))
        false
    } else {
        true
    }

    private fun validateAge(userAge: String) = if (userAge.length > MAX_AGE_LENGTH) {
        toast(getString(R.string.max_age_length))
        false
    } else {
        true
    }

    private fun clearEdt() {
        binding.edtUserName.text.clear()
        binding.edtUserAge.text.clear()
    }

    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    companion object {
        private const val MAX_AGE_LENGTH = 3
        private const val MAX_NAME_LENGTH = 50
    }

}



