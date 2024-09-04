package com.example.asian;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivityIssueNine extends AppCompatActivity {

    private static final int MAX_AGE_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 50;
    private EditText mEdtUserName;
    private EditText mEdtUserAge;
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
        initUI();
        initListener();

        mDatabaseHelper = new DatabaseHelper(this);
        mUserList = new ArrayList<>();
        mUserAdapter = new UserAdapter(mUserList, mDatabaseHelper);

        mRecyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewUsers.setAdapter(mUserAdapter);

        loadUsers();
    }

    private void initListener() {
        mBtnAddUser.setOnClickListener(this::addUser);
        mBtnDeleteAllUsers.setOnClickListener(this::deleteAllUsers);
        mBtnShowAllUsers.setOnClickListener(this::loadUsers);
    }

    private void initUI() {
        mEdtUserName = findViewById(R.id.edtUserName);
        mEdtUserAge = findViewById(R.id.edtUserAge);
        mBtnAddUser = findViewById(R.id.btnAddUser);
        mBtnDeleteAllUsers = findViewById(R.id.btnDeleteAllUsers);
        mBtnShowAllUsers = findViewById(R.id.btnShowAllUsers);
        mRecyclerViewUsers = findViewById(R.id.rvUsers);
    }

    private void addUser(View view) {
        String name = mEdtUserName.getText().toString();
        String ageStr = mEdtUserAge.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ageStr)) {
            Toast.makeText(this, getString(R.string.pls_enter_the_data), Toast.LENGTH_SHORT).show();
            return;
        } else if (name.length() > MAX_NAME_LENGTH) {
            Toast.makeText(this, getString(R.string.max_name_length), Toast.LENGTH_SHORT).show();
            return;
        } else if (ageStr.length() > MAX_AGE_LENGTH) {
            Toast.makeText(this, getString(R.string.max_age_length), Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        mDatabaseHelper.addUser(name, age);
        mEdtUserName.setText("");
        mEdtUserAge.setText("");
        Toast.makeText(this, getString(R.string.input_dataa_complete), Toast.LENGTH_SHORT).show();
        loadUsers();
    }

    private void deleteAllUsers(View view) {
        mDatabaseHelper.deleteAllUsers();
        List<User> newListUser = new ArrayList<>();
        updateList(newListUser);
        Toast.makeText(this, getString(R.string.delete_all_data_done), Toast.LENGTH_SHORT).show();
    }

    private void loadUsers(View view) {
        loadUsers();
    }

    private void loadUsers() {
        List<User> newList = mDatabaseHelper.getAllUsers();
        updateList(newList);
    }

    public void updateList(List<User> newList) {
        MyDiffUtilsCallback diffCallback = new MyDiffUtilsCallback(this.mUserList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        mUserList.clear();
        mUserList.addAll(newList);
        diffResult.dispatchUpdatesTo(mUserAdapter);
    }
}
