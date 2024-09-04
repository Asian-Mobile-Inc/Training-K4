package com.example.asian.kotlin.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.asian.R
import com.example.asian.kotlin.adapter.UserAdapter
import com.example.asian.kotlin.model.User
import com.example.asian.kotlin.viewmodel.UserViewModel

class MainActivityKotlin : AppCompatActivity() {

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(
            this,
            UserViewModel.UserViewModelFactory(this.application)
        )[UserViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_mana_layout)
        initControl()
        initEvent()
    }

    private fun initEvent() {

    }

    private fun initControl() {
        val adapter:UserAdapter = UserAdapter(this@MainActivityKotlin,onItemClick,onItemDelete)

    }

    private val onItemClick:(User)->Unit = {}

    private val onItemDelete:(User)->Unit= {}
}