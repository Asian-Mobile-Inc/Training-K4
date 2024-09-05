package com.example.asian.issueeleventh.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asian.R
import com.example.asian.databinding.DialogConfirmBinding
import com.example.asian.databinding.DialogEditNameAgeBinding
import com.example.asian.databinding.FragmentUserInfoBinding
import com.example.asian.issueeleventh.adapter.UserAdapter
import com.example.asian.issueeleventh.model.UserInfo
import com.example.asian.issueeleventh.viewmodel.UserViewModel

private const val MAX_AGE = 200
private const val MAX_LENGTH_AGE = 3
private const val KEY_BUNDLE_TAB = "tab"

class UserInfoFragment : Fragment(), UserAdapter.ItemClickListener {
    private var mTab: Int = 0
    private lateinit var mUserAdapter: UserAdapter
    private val mBinding: FragmentUserInfoBinding by lazy {
        FragmentUserInfoBinding.inflate(layoutInflater)
    }
    private val mUserViewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mTab = it.getInt(KEY_BUNDLE_TAB, 0)
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
                    putInt(KEY_BUNDLE_TAB, tab)
                }
            }
    }

    private fun setupRecyclerView() {
        mUserAdapter = UserAdapter(this)
        mBinding.rvUser.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mUserAdapter
        }
    }

    private fun initData() {
        if (mTab == 0) {
            mUserViewModel.getAllData()
        } else {
            mUserViewModel.getFavouriteUsers()
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
        val userClone = UserInfo(
            user.userName,
            user.userAge,
            !user.userFavourite
        )
        userClone.userId = user.userId
        mUserViewModel.favouriteUser(userClone)

    }

    private fun showDialogDelete(user: UserInfo?) {
        activity?.let {
            val dialog = Dialog(it).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(R.layout.dialog_confirm)
                if (window != null) {
                    window!!.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            }
            val dialogBinding: DialogConfirmBinding =
                DialogConfirmBinding.inflate(dialog.layoutInflater)
            dialog.setContentView(dialogBinding.root)
            with(dialogBinding) {
                if (user == null) {
                    tvDeleteThisItem.text = getString(R.string.delete_all_item)
                }
                btnCancelDelete.setOnClickListener {
                    dialog.dismiss()
                }
                btnConfirmDelete.setOnClickListener {
                    if (user != null) {
                        mUserViewModel.deleteUser(user)
                    } else {
                        mUserViewModel.deleteAllUser()
                    }
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }

    private fun showDialogEdit(user: UserInfo) {
        activity?.let {
            val dialog = Dialog(it).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(R.layout.dialog_edit_name_age)
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            val dialogBinding: DialogEditNameAgeBinding =
                DialogEditNameAgeBinding.inflate(dialog.layoutInflater)
            dialog.setContentView(dialogBinding.root)
            with(dialogBinding) {
                edtNewAge.setText(user.userAge.toString().toInt().toString())
                edtNewName.setText(user.userName)
                btnCancel.setOnClickListener {
                    dialog.dismiss()
                }
                btnConfirm.setOnClickListener {
                    if (edtNewName.text.isEmpty()) {
                        edtNewName.error = getString(R.string.name_invalid)
                    }
                    if (edtNewAge.text.toString().isEmpty()) {
                        edtNewAge.error = getString(R.string.age_invalid)
                    } else if (edtNewAge.text.toString().length > MAX_LENGTH_AGE || edtNewAge.text.toString()
                            .toInt() > MAX_AGE
                    ) {
                        edtNewAge.error = getString(R.string.age_invalid)
                    }
                    if (!(edtNewAge.text.isEmpty() ||
                                edtNewAge.text.isEmpty() ||
                                edtNewAge.text.toString().length > MAX_LENGTH_AGE ||
                                edtNewAge.text.toString().toInt() > MAX_AGE)
                    ) {
                        val userInfo = UserInfo(
                            edtNewName.text.toString(),
                            edtNewAge.text.toString().toInt(),
                            user.userFavourite
                        )
                        userInfo.userId = user.userId
                        mUserViewModel.updateUser(userInfo)
                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
        }
    }
}
