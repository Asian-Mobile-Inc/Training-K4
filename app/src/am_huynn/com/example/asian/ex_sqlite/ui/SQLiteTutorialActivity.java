package com.example.asian.ex_sqlite.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.R;
import com.example.asian.ex_sqlite.adapter.UserAdapter;
import com.example.asian.ex_sqlite.helper.UserSQLiteHelper;
import com.example.asian.ex_sqlite.model.User;

import java.util.ArrayList;

public class SQLiteTutorialActivity extends AppCompatActivity {
    private ArrayList<User> mUsers;
    private EditText mEdtUserName;
    private EditText mEdtUserAge;
    private Button mBtnAddUser;
    private Button mBtnDeleteAll;
    private Button mBtnShowAll;
    private RecyclerView mRvUsers;
    private UserAdapter mUserAdapter;
    private UserSQLiteHelper mSqLiteOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_tutorial);
        initView();
        initDataUsers();
        initAdapter();
        iniListener();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideKeyboard();
        return super.onTouchEvent(event);
    }

    private void initDataUsers() {
        mSqLiteOpenHelper = new UserSQLiteHelper(this);
        mUsers = mSqLiteOpenHelper.getAllUsers();
    }

    private void initView() {
        mEdtUserName = findViewById(R.id.edtNameUser);
        mEdtUserAge = findViewById(R.id.edtAgeUser);
        mRvUsers = findViewById(R.id.rvUsers);
        mBtnAddUser = findViewById(R.id.btnAddUser);
        mBtnDeleteAll = findViewById(R.id.btnDeleteAll);
        mBtnShowAll = findViewById(R.id.btnShowAll);
    }


    private void initAdapter() {
        mUserAdapter = new UserAdapter(this, mUsers, user -> deleteUser(user));
        mRvUsers.setAdapter(mUserAdapter);
        mRvUsers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void iniListener() {
        mBtnAddUser.setOnClickListener(view -> addUser());
        mBtnDeleteAll.setOnClickListener(view -> deleteAll());
        mBtnShowAll.setOnClickListener(view -> showAllUsers());
    }

    private void addUser() {
        String name = mEdtUserName.getText().toString().trim();
        String age = mEdtUserAge.getText().toString().trim();
        if (!validatorInput(name, age)) {
            return;
        }
        User user = mSqLiteOpenHelper.addUser(name, Integer.parseInt(age));
        mUsers.add(user);
        mUserAdapter.setData(mUsers);
        clearEditText();
        hideKeyboard();
    }

    private void deleteAll() {
        mSqLiteOpenHelper.deleteAllUsers();
        mUsers.clear();
        mUserAdapter.setData(mUsers);
    }

    public Void deleteUser(User user) {
        mSqLiteOpenHelper.deleteUser(user.getUserId());
        for (User u : mUsers) {
            if (u.getUserId() == user.getUserId()) {
                mUsers.remove(u);
                mUserAdapter.setData(mUsers);
                break;
            }
        }
        return null;
    }

    private void showAllUsers() {
        mUsers = mSqLiteOpenHelper.getAllUsers();
        mUserAdapter.setData(mUsers);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void clearEditText() {
        mEdtUserName.getText().clear();
        mEdtUserAge.getText().clear();
    }

    private boolean validatorInput(String name, String age) {
        boolean isValid;
        if (name.isEmpty()) {
            mEdtUserName.setError(getString(R.string.please_do_not_empty));
            isValid = false;
        } else if (age.isEmpty()) {
            mEdtUserAge.setError(getString(R.string.please_do_not_empty));
            isValid = false;
        } else if (Integer.parseInt(age) < 1) {
            mEdtUserAge.setError(getString(R.string.age_greater_than_zero));
            isValid = false;
        } else {
            isValid = true;
        }
        return isValid;
    }
}