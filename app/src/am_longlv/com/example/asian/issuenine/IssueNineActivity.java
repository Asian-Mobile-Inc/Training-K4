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
    List<UserInfo> mUserInfoLists;

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
        mBtnAdd.setOnClickListener(v -> addUser());
        mBtnDeleteAll.setOnClickListener(v -> deleteAllUser());
        mBtnShowAll.setOnClickListener(v -> getAllUser());
    }

    private void setUpRecyclerView() {
//        mUserInfoLists = new ArrayList<>();
//        mUserInfoLists.add(0, new UserInfo(-1, "", ""));
//        mUserInfoLists.addAll(mDBHelper.getAllUser());
        mUserInfoAdapter = new UserInfoAdapter(mUserInfoLists, this);
        mRvUserInfo.setAdapter(mUserInfoAdapter);
        mRvUserInfo.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getAllUser() {
        mUserInfoLists.add(0, new UserInfo(-1, "", ""));
        mUserInfoLists.addAll(mDBHelper.getAllUser());
        mUserInfoAdapter.updateData(mUserInfoLists);
    }

    private void deleteAllUser() {
        mDBHelper.deleteAllUser();
        mUserInfoLists.clear();
        mUserInfoAdapter.updateData(mUserInfoLists);
    }

    private void addUser() {
        if (validate()) {
            return;
        }
        mDBHelper.addUser(new UserInfo(0, mEdtUsername.getText().toString(), mEdtAge.getText().toString()));
        mUserInfoLists.add(mDBHelper.getLastUser());
        mUserInfoAdapter.updateData(mUserInfoLists);
    }

    private boolean validate() {
        if (mEdtUsername.getText().toString().isEmpty()) {
            mEdtUsername.setError(getString(R.string.name_invalid));
        }
        if (mEdtAge.getText().toString().isEmpty()) {
            mEdtAge.setError(getString(R.string.age_invalid));
        }
        return mEdtUsername.getText().toString().isEmpty() || mEdtAge.getText().toString().isEmpty();
    }
}
