package com.example.asian.kotlin.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asian.MyDiffUtilsCallback
import com.example.asian.R
import com.example.asian.kotlin.model.User

class MainActivityIssueNine : AppCompatActivity() {
    private var mEdtUserName: EditText? = null
    private var mEdtUserAge: EditText? = null
    private var mBtnAddUser: Button? = null
    private var mBtnDeleteAllUsers: Button? = null
    private var mBtnShowAllUsers: Button? = null
    private var mRecyclerViewUsers: RecyclerView? = null
    private var mUserAdapter: UserAdapter? = null
    private var mUserList: MutableList<User>? = null
    private var mDatabaseHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_mana_layout)
        initUI()
        initListener()

        mDatabaseHelper = DatabaseHelper(this)
        mUserList = ArrayList()
        mUserAdapter = UserAdapter(mUserList, mDatabaseHelper)

        mRecyclerViewUsers!!.layoutManager = LinearLayoutManager(this)
        mRecyclerViewUsers!!.adapter = mUserAdapter

        loadUsers()
    }

    private fun initListener() {
        mBtnAddUser!!.setOnClickListener { view: View -> this.addUser(view) }
        mBtnDeleteAllUsers!!.setOnClickListener { view: View -> this.deleteAllUsers(view) }
        mBtnShowAllUsers!!.setOnClickListener { view: View -> this.loadUsers(view) }
    }

    private fun initUI() {
        mEdtUserName = findViewById(R.id.edtUserName)
        mEdtUserAge = findViewById(R.id.edtUserAge)
        mBtnAddUser = findViewById(R.id.btnAddUser)
        mBtnDeleteAllUsers = findViewById(R.id.btnDeleteAllUsers)
        mBtnShowAllUsers = findViewById(R.id.btnShowAllUsers)
        mRecyclerViewUsers = findViewById(R.id.recyclerViewUsers)
    }

    private fun addUser(view: View) {
        val name = mEdtUserName!!.text.toString()
        val ageStr = mEdtUserAge!!.text.toString()
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ageStr)) {
            Toast.makeText(this, getString(R.string.pls_enter_the_data), Toast.LENGTH_SHORT).show()
            return
        } else if (name.length > MAX_NAME_LENGTH) {
            Toast.makeText(this, getString(R.string.max_name_length), Toast.LENGTH_SHORT).show()
            return
        } else if (ageStr.length > MAX_AGE_LENGTH) {
            Toast.makeText(this, getString(R.string.max_age_length), Toast.LENGTH_SHORT).show()
            return
        }

        val age = ageStr.toInt()
        mDatabaseHelper!!.addUser(name, age)
        mEdtUserName!!.setText("")
        mEdtUserAge!!.setText("")
        Toast.makeText(this, getString(R.string.input_dataa_complete), Toast.LENGTH_SHORT).show()
        loadUsers()
    }

    private fun deleteAllUsers(view: View) {
        mDatabaseHelper!!.deleteAllUsers()
        val newListUser: List<User> = ArrayList()
        updateList(newListUser)
        Toast.makeText(this, getString(R.string.delete_all_data_done), Toast.LENGTH_SHORT).show()
    }

    private fun loadUsers(view: View) {
        loadUsers()
    }

    private fun loadUsers() {
        val newList = mDatabaseHelper!!.allUsers
        updateList(newList)
    }

    fun updateList(newList: List<User>?) {
        val diffCallback = MyDiffUtilsCallback(this.mUserList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        mUserList!!.clear()
        mUserList!!.addAll(newList!!)
        diffResult.dispatchUpdatesTo(mUserAdapter!!)
    }

    companion object {
        private const val MAX_AGE_LENGTH = 3
        private const val MAX_NAME_LENGTH = 50
    }
}
