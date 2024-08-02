package com.example.asian.issuenine;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.R;
import com.example.asian.issuenine.adapter.UserInfoAdapter;
import com.example.asian.issuenine.database.DBHelper;
import com.example.asian.issuenine.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class IssueNineActivity extends AppCompatActivity {
    private EditText mEdtUsername;
    private EditText mEdtAge;
    private Button mBtnAdd;
    private Button mBtnShowAll;
    private Button mBtnDeleteAll;
    private RecyclerView mRvUserInfo;
    private final DBHelper mDBHelper = new DBHelper(this, "User.db", 1);
    private UserInfoAdapter mUserInfoAdapter;
    private List<UserInfo> mUserInfoLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_nine);
        initUI();
        initListener();
        setUpRecyclerView();
    }

    private void initUI() {
        mEdtUsername = findViewById(R.id.edtName);
        mEdtAge = findViewById(R.id.edtAge);
        mBtnAdd = findViewById(R.id.btnAdd);
        mBtnShowAll = findViewById(R.id.btnShowAll);
        mBtnDeleteAll = findViewById(R.id.btnDeleteAll);
        mRvUserInfo = findViewById(R.id.rvUserInfo);
    }

    private void initListener() {
        mBtnAdd.setOnClickListener(v -> {;
            mDBHelper.addUser(new UserInfo(0, mEdtUsername.getText().toString(), mEdtAge.getText().toString()));
            mUserInfoAdapter.addUser(mDBHelper.getLastUser());
        });
        mBtnDeleteAll.setOnClickListener(v -> {
            mDBHelper.deleteAllUser();
            getAllUser();
        });
        mBtnShowAll.setOnClickListener(v -> getAllUser());
    }

    private void setUpRecyclerView() {
        mUserInfoLists = new ArrayList<>();
        mUserInfoAdapter = new UserInfoAdapter(mUserInfoLists, this);
        getAllUser();
        mRvUserInfo.setAdapter(mUserInfoAdapter);
        mRvUserInfo.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getAllUser() {
        mUserInfoAdapter.getAllUser(mDBHelper);
    }
}
