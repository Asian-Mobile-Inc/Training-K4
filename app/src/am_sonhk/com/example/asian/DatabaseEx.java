package com.example.asian;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DatabaseEx extends AppCompatActivity {

    private EditText edtUserName;
    private EditText edtUserAge;
    private Button mBtnAddUser;
    private Button mBtnDeleteAllUsers;
    private Button mBtnShowAllUsers;
    private RecyclerView mRecyclerViewUsers;
    private UserAdapter mUserAdapter;
    private List<User> mUserList;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_mana_layout);

        edtUserName = findViewById(R.id.edtUserName);
        edtUserAge = findViewById(R.id.edtUserAge);
        mBtnAddUser = findViewById(R.id.btnAddUser);
        mBtnDeleteAllUsers = findViewById(R.id.btnDeleteAllUsers);
        mBtnShowAllUsers = findViewById(R.id.btnShowAllUsers);
        mRecyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        mDatabaseHelper = new DatabaseHelper(this);
        mUserList = new ArrayList<>();
        mUserAdapter = new UserAdapter(mUserList, mDatabaseHelper);

        mRecyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewUsers.setAdapter(mUserAdapter);

        mBtnAddUser.setOnClickListener(this::addUser);
        mBtnDeleteAllUsers.setOnClickListener(this::deleteAllUsers);
        mBtnShowAllUsers.setOnClickListener(this::loadUsers);

        loadUsers();
    }

    private void addUser(View view) {
        String name = edtUserName.getText().toString();
        String ageStr = edtUserAge.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ageStr)) {
            Toast.makeText(this, getString(R.string.pls_enter_the_data), Toast.LENGTH_SHORT).show();
            return;
        }
        int age = Integer.parseInt(ageStr);
        mDatabaseHelper.addUser(name, age);
        edtUserName.setText("");
        edtUserAge.setText("");
        Toast.makeText(this, getString(R.string.input_dataa_complete), Toast.LENGTH_SHORT).show();
        loadUsers();
    }

    private void deleteAllUsers(View view) {
        mDatabaseHelper.deleteAllUsers();
        loadUsers();
        Toast.makeText(this, getString(R.string.delete_all_data_done), Toast.LENGTH_SHORT).show();
    }

    private void loadUsers(View view) {
        loadUsers();
    }

        private void loadUsers() {
            mUserList.clear();
            mUserList.addAll(mDatabaseHelper.getAllUsers());
            mUserAdapter.notifyDataSetChanged();
        }
}

// TODO : use DiffUtil replace for notifyDataChanged