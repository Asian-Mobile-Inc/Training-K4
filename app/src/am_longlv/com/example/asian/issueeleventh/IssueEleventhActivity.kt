package com.example.asian.issueeleventh

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asian.R
import com.example.asian.databinding.ActivityIssueEleventhBinding
import com.example.asian.databinding.DialogConfirmBinding
import com.example.asian.databinding.DialogEditNameAgeBinding
import com.example.asian.issueeleventh.adapter.UserAdapter
import com.example.asian.issueeleventh.model.UserInfo
import com.example.asian.issueeleventh.viewmodel.UserViewModel

class IssueEleventhActivity : AppCompatActivity(), UserAdapter.ItemClickListener {
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(
            this,
            UserViewModel.UserViewModelFactory(this.application)
        )[UserViewModel::class.java]
    }
    private lateinit var mBinding: ActivityIssueEleventhBinding
    private lateinit var mUserAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingData()
        setupRecyclerView()
        initListener()
    }

    private fun initListener() {
        mBinding.btnAdd.setOnClickListener {
            if (isValidate()) {
                userViewModel.insertUser(
                    UserInfo(
                        mBinding.edtName.text.toString(),
                        mBinding.edtAge.text.toString().toInt()
                    )
                )
            }
        }
        mBinding.btnDeleteAll.setOnClickListener {
            userViewModel.deleteAllUser()
        }
        mBinding.btnShowAll.setOnClickListener {
//            userViewModel.getAllData()
        }
    }

    private fun bindingData() {
        mBinding = ActivityIssueEleventhBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        userViewModel.allUsers.observe(this) {
            mUserAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        mBinding.rvUser.layoutManager = LinearLayoutManager(this)
        mUserAdapter = UserAdapter(this)
        mBinding.rvUser.adapter = mUserAdapter
    }

    private fun isValidate(): Boolean {
        if (mBinding.edtName.text.isEmpty()) {
            mBinding.edtName.error = getString(R.string.name_invalid)
        }
        if (mBinding.edtAge.text.isEmpty()) {
            mBinding.edtAge.error = getString(R.string.age_invalid)
        }
        return !(mBinding.edtName.text.isEmpty() || mBinding.edtAge.text.isEmpty())
    }

    override fun onDeleteClick(user: UserInfo) {
        showDialogDelete(user)
    }

    override fun onEditClick(user: UserInfo) {
        showDialogEdit(user)
    }

    private fun showDialogDelete(user: UserInfo) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_confirm)
        if (dialog.window != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        val dialogBinding: DialogConfirmBinding =
            DialogConfirmBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.btnCancelDelete.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnConfirmDelete.setOnClickListener {
            userViewModel.deleteUser(user)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showDialogEdit(user: UserInfo) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_edit_name_age)
        if (dialog.window != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        val dialogBinding: DialogEditNameAgeBinding =
            DialogEditNameAgeBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnConfirm.setOnClickListener {
            if (dialogBinding.edtNewName.text.isEmpty()) {
                dialogBinding.edtNewName.error = getString(R.string.name_invalid)
            }
            if (dialogBinding.edtNewAge.text.toString().isEmpty()) {
                dialogBinding.edtNewAge.error = getString(R.string.age_invalid)
            }
            if (!(dialogBinding.edtNewAge.text.isEmpty() || dialogBinding.edtNewAge.text.isEmpty())) {
                user.userName = dialogBinding.edtNewName.text.toString()
                user.userAge = dialogBinding.edtNewAge.text.toString().toInt()
                userViewModel.updateUser(user)
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}
