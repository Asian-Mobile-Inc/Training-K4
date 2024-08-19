package com.example.asian.issueeleventh.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asian.R
import com.example.asian.databinding.DialogConfirmBinding
import com.example.asian.databinding.DialogEditNameAgeBinding
import com.example.asian.databinding.FragmentUserInfoBinding
import com.example.asian.issueeleventh.adapter.UserAdapter
import com.example.asian.issueeleventh.model.UserInfo
import com.example.asian.issueeleventh.viewmodel.UserViewModel

class UserInfoFragment : Fragment(), UserAdapter.ItemClickListener {
    private var mTab: Int = 0
    private lateinit var mUserAdapter: UserAdapter
    private val mBinding: FragmentUserInfoBinding by lazy {
        FragmentUserInfoBinding.inflate(layoutInflater)
    }
    private val mUserViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mTab = it.getInt("tab", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        initObserver()
        setupRecyclerView()
        initData()
        return mBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(tab: Int) =
            UserInfoFragment().apply {
                arguments = Bundle().apply {
                    putInt("tab", tab)
                }
            }
    }

    private fun setupRecyclerView() {
        mBinding.rvUser.layoutManager = LinearLayoutManager(context)
        mUserAdapter = UserAdapter(this)
        mBinding.rvUser.adapter = mUserAdapter
    }

    private fun initData() {
        if (mTab == 0) {
            mUserViewModel.getAllData()
        } else {
            mUserViewModel.getFavouriteUser()
        }
    }

    private fun initObserver() {
        if (mTab == 0) {
            mUserViewModel.allUsers.observe(this) {
                mUserAdapter.submitList(it.toMutableList())
            }
        } else {
            mUserViewModel.favouriteUsers.observe(this) {
                mUserAdapter.submitList(it.toMutableList())
            }
        }
    }


    override fun onDeleteClick(user: UserInfo) {
        showDialogDelete(user)
    }

    override fun onEditClick(user: UserInfo) {
        showDialogEdit(user)
    }

    override fun onFavouriteClick(user: UserInfo) {
        val userClone:UserInfo = user.copy()
        userClone.userFavourite = !user.userFavourite
        mUserViewModel.favouriteUser(userClone)

    }

    private fun showDialogDelete(user: UserInfo?) {
        activity?.let {
            val dialog = Dialog(it)
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
    }

    private fun showDialogEdit(user: UserInfo) {
        activity?.let {
            val dialog = Dialog(it)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_edit_name_age)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
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
                        dialogBinding.edtNewAge.text.toString().toInt(),
                        false
                    )
                    userInfo.userId = user.userId
                    mUserViewModel.updateUser(userInfo)
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }
}