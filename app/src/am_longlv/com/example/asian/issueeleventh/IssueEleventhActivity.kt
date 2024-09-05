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
import com.example.asian.issueeleventh.adapter.TabName
import com.example.asian.issueeleventh.adapter.ViewPagerUserAdapter
import com.example.asian.issueeleventh.model.UserInfo
import com.example.asian.issueeleventh.viewmodel.UserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val MAX_AGE = 200
private const val MAX_LENGTH_AGE = 3

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
        with(mBinding) {
            btnAdd.setOnClickListener {
                if (isValidate()) {
                    mUserViewModel.insertUser(
                        UserInfo(
                            edtName.text.toString(),
                            edtAge.text.toString().toInt(),
                            false
                        )
                    )
                    edtAge.text = null
                    edtName.text = null
                }
            }
            btnDeleteAll.setOnClickListener {
                showDialogDeleteAll()
            }
            btnShowAll.setOnClickListener {
                mUserViewModel.getAllData()
                mUserViewModel.getFavouriteUsers()
            }
        }
    }

    private fun isValidate(): Boolean {
        with(mBinding) {
            if (edtName.text.isEmpty()) {
                edtName.error = getString(R.string.name_invalid)
            }
            if (edtAge.text.isEmpty()) {
                edtAge.error = getString(R.string.age_invalid)
            } else if (edtAge.text.toString().length > MAX_LENGTH_AGE || edtAge.text.toString()
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

    private fun showDialogDeleteAll() {
        val dialog = Dialog(this).apply {
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
            tvDeleteThisItem.text = getString(R.string.delete_all_item)
            btnCancelDelete.setOnClickListener {
                dialog.dismiss()
            }
            btnConfirmDelete.setOnClickListener {
                mUserViewModel.deleteAllUser()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun setUpTabLayout() {
        val viewPagerAdapter = ViewPagerUserAdapter(this)
        mBinding.vpUserInfo.apply {
            offscreenPageLimit = 1
            adapter = viewPagerAdapter
        }
        TabLayoutMediator(
            mBinding.tlUser, mBinding.vpUserInfo
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = TabName.getName(position)
        }.attach()
    }
}
