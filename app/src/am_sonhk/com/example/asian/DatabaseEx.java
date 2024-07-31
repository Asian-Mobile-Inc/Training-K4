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

    private EditText edtUserName, edtUserAge;
    private Button btnAddUser;
    private Button btnDeleteAllUsers;
    private Button btnShowAllUsers;
    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private List<User> userList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_mana_layout);

        edtUserName = findViewById(R.id.edtUserName);
        edtUserAge = findViewById(R.id.edtUserAge);
        btnAddUser = findViewById(R.id.btnAddUser);
        btnDeleteAllUsers = findViewById(R.id.btnDeleteAllUsers);
        btnShowAllUsers = findViewById(R.id.btnShowAllUsers);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        databaseHelper = new DatabaseHelper(this);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, databaseHelper);

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsers.setAdapter(userAdapter);

        btnAddUser.setOnClickListener(this::addUser);
        btnDeleteAllUsers.setOnClickListener(this::deleteAllUsers);
        btnShowAllUsers.setOnClickListener(this::loadUsers);

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
        databaseHelper.addUser(name, age);
        edtUserName.setText("");
        edtUserAge.setText("");
        loadUsers();
    }

    private void deleteAllUsers(View view) {
        databaseHelper.deleteAllUsers();
        loadUsers();
    }

    private void loadUsers(View view) {
        loadUsers();
    }

    private void loadUsers() {
        userList.clear();
        userList.addAll(databaseHelper.getAllUsers());
        userAdapter.notifyDataSetChanged();
    }
}

// TODO : fix string
