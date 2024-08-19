package com.example.asian.issueeleventh

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.asian.R
import com.example.asian.databinding.ActivityIssueEleventhBinding
import com.example.asian.databinding.DialogConfirmBinding
import com.example.asian.issueeleventh.adapter.ViewPagerUserAdapter
import com.example.asian.issueeleventh.model.UserInfo
import com.example.asian.issueeleventh.viewmodel.UserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class IssueEleventhActivity : AppCompatActivity() {
    private val mUserViewModel: UserViewModel by viewModels()
    private val mBinding: ActivityIssueEleventhBinding by lazy {
        ActivityIssueEleventhBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initListener()
        setUpTabLayout()
    }

    private fun initListener() {
        mBinding.btnAdd.setOnClickListener {
            if (isValidate()) {
                mUserViewModel.insertUser(
                    UserInfo(
                        mBinding.edtName.text.toString(),
                        mBinding.edtAge.text.toString().toInt(),
                        false
                    )
                )
                mBinding.edtAge.text = null
                mBinding.edtName.text = null
            }
        }
        mBinding.btnDeleteAll.setOnClickListener {
            showDialogDeleteAll()
        }
        mBinding.btnShowAll.setOnClickListener {
            mUserViewModel.getAllData()
            mUserViewModel.getFavouriteUsers()
        }
    }

    private fun isValidate(): Boolean {
        if (mBinding.edtName.text.isEmpty()) {
            mBinding.edtName.error = getString(R.string.name_invalid)
        }
        if (mBinding.edtAge.text.isEmpty()) {
            mBinding.edtAge.error = getString(R.string.age_invalid)
        } else if (mBinding.edtAge.text.toString().length > 3 || mBinding.edtAge.text.toString()
                .toInt() > 200
        ) {
            mBinding.edtAge.error = getString(R.string.age_invalid)
        }
        return !(mBinding.edtName.text.isEmpty() ||
                mBinding.edtAge.text.isEmpty() ||
                mBinding.edtAge.text.toString().length > 3 ||
                mBinding.edtAge.text.toString().toInt() > 200)
    }

    private fun showDialogDeleteAll() {
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
        dialogBinding.tvDeleteThisItem.text = getString(R.string.delete_all_item)
        dialogBinding.btnCancelDelete.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnConfirmDelete.setOnClickListener {
            mUserViewModel.deleteAllUser()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setUpTabLayout() {
        val viewPagerAdapter = ViewPagerUserAdapter(this)
        mBinding.vpUserInfo.offscreenPageLimit = 1
        mBinding.vpUserInfo.adapter = viewPagerAdapter
        TabLayoutMediator(
            mBinding.tlUser, mBinding.vpUserInfo
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = (
                    when (position) {
                        0 -> "ALL"
                        else -> "Favourite"
                    })
        }.attach()
    }
}
