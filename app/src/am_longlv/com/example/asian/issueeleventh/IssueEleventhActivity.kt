package com.example.asian.issueeleventh

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asian.R
import com.example.asian.databinding.ActivityIssueEleventhBinding
import com.example.asian.databinding.DialogConfirmBinding
import com.example.asian.databinding.DialogEditNameAgeBinding
import com.example.asian.issueeleventh.adapter.UserAdapter
import com.example.asian.issueeleventh.model.UserInfo
import com.example.asian.issueeleventh.viewmodel.UserViewModel

private const val MAX_AGE = 200
private const val MAX_LENGTH_AGE = 3

class IssueEleventhActivity : AppCompatActivity(), UserAdapter.ItemClickListener {
    private val mUserViewModel: UserViewModel by viewModels()
    private val mBinding: ActivityIssueEleventhBinding by lazy {
        ActivityIssueEleventhBinding.inflate(layoutInflater)
    }
    private lateinit var mUserAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initObserver()
        setupRecyclerView()
        initListener()
        initData()
    }

    private fun initData() {
        mUserViewModel.getAllData()
    }

    private fun initListener() {
        mBinding.btnAdd.setOnClickListener {
            if (isValidate()) {
                mUserViewModel.insertUser(
                    UserInfo(
                        mBinding.edtName.text.toString(),
                        mBinding.edtAge.text.toString().toInt()
                    )
                )
                with(mBinding) {
                    edtAge.text = null
                    edtName.text = null
                    rvUser.scrollToPosition(mUserViewModel.getListSize() - 1)
                }
            }
        }
        with(mBinding) {
            btnDeleteAll.setOnClickListener {
                showDialogDelete(null)
            }
            btnShowAll.setOnClickListener {
                mUserViewModel.getAllData()
            }
        }
    }

    private fun initObserver() {
        mUserViewModel.allUsers.observe(this) {
            mUserAdapter.submitList(it.toMutableList())
        }
    }

    private fun setupRecyclerView() {
        mUserAdapter = UserAdapter(this)
        mBinding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@IssueEleventhActivity)
            adapter = mUserAdapter
        }
    }

    private fun isValidate(): Boolean {
        with(mBinding) {
            if (edtName.text.isEmpty()) {
                edtName.error = getString(R.string.name_invalid)
            }
            if (edtAge.text.isEmpty()) {
                edtAge.error = getString(R.string.age_invalid)
            }
            if (edtAge.text.toString().length > MAX_LENGTH_AGE || edtAge.text.toString()
                    .toInt() > MAX_AGE
            ) {
                edtAge.error = getString(R.string.age_invalid)
            }
            return !(edtName.text.isEmpty() ||
                    edtAge.text.isEmpty() ||
                    edtAge.text.toString().length > MAX_LENGTH_AGE ||
                    edtAge.text.toString().toInt() > MAX_AGE)
        }
    }

    override fun onDeleteClick(user: UserInfo) {
        showDialogDelete(user)
    }

    override fun onEditClick(user: UserInfo) {
        showDialogEdit(user)
    }

    private fun showDialogDelete(user: UserInfo?) {
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
        if (user == null) {
            dialogBinding.tvDeleteThisItem.text = getString(R.string.delete_all_item)
        }
        dialogBinding.btnCancelDelete.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnConfirmDelete.setOnClickListener {
            if (user != null) {
                mUserViewModel.deleteUser(user)
            } else {
                mUserViewModel.deleteAllUser()
            }
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
        dialogBinding.edtNewAge.setText(user.userAge.toString().toInt().toString())
        dialogBinding.edtNewName.setText(user.userName)
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
                val userInfo = UserInfo(
                    dialogBinding.edtNewName.text.toString(),
                    dialogBinding.edtNewAge.text.toString().toInt()
                )
                userInfo.userId = user.userId
                mUserViewModel.updateUser(userInfo)
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}
