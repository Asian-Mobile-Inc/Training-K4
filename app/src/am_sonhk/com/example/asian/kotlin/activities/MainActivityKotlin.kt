//package com.example.asian.kotlin.activities
//
//import android.os.Bundle
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.asian.databinding.UserManaLayoutBinding
//import com.example.asian.kotlin.adapter.UserAdapter
//import com.example.asian.kotlin.model.User
//import com.example.asian.kotlin.viewmodel.UserViewModel
//
//class MainActivityKotlin : AppCompatActivity() {
//    private lateinit var binding: UserManaLayoutBinding
//    private lateinit var userViewModel: UserViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = UserManaLayoutBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Initialize the ViewModel
//        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
//
//        // Set up RecyclerView
//        val adapter = UserAdapter { user -> userViewModel.deleteUser(user.userId) }
//        binding.rvUsers.adapter = adapter
//        binding.rvUsers.layoutManager = LinearLayoutManager(this)
//
////        // Observe LiveData from ViewModel
////        userViewModel.allUsers.observe(this, Observer { users ->
////            users?.let { adapter.submitList(it) }
////        })
//
//        // Set up button click listeners
//        binding.btnAddUser.setOnClickListener {
//            val userName = binding.edtUserName.text.toString()
//            val userAge = binding.edtUserAge.text.toString().toIntOrNull()
//            if (userName.isNotBlank() && userAge != null) {
//                val user = User(userName = userName, userAge = userAge)
//                userViewModel.addUser(user)
//            }
//        }
//
//        binding.btnDeleteAllUsers.setOnClickListener {
//            userViewModel.deleteAllUsers()
//        }
//
//        binding.btnShowAllUsers.setOnClickListener {
//            // This button can be used to refresh the list or perform any other action
//            userViewModel.allUsers.observe(this, Observer { users ->
//                users?.let { adapter.submitList(it) }
//            })
//        }
//    }
//}
//
//
//
