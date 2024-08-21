package com.example.asian.ui

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.asian.R
import com.example.asian.adapter.PagerAdapter
import com.example.asian.databinding.ActivityMvvmBinding
import com.example.asian.model.User
import com.example.asian.viewmodel.UserViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MvvmActivity : AppCompatActivity() {
    private val binding: ActivityMvvmBinding by lazy {
        ActivityMvvmBinding.inflate(layoutInflater)
    }

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initControl()
        initListener()
    }

    private fun initControl() {
        val pagerAdapter = PagerAdapter(this)
        binding.vpPagerUsers.adapter = pagerAdapter
        TabLayoutMediator(binding.tlTabUsers, binding.vpPagerUsers) { tab, position ->
            if (position == userViewModel.positionPageOne) {
                tab.text = resources.getString(R.string.all)
            } else {
                tab.text = resources.getString(R.string.favorite)
            }
        }.attach()
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
            val user = User(name, age.toInt(), false)
            userViewModel.insertUser(user)
            binding.edtName.text = null
            binding.edtAge.text = null
            hideKeyboard()
        }
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
