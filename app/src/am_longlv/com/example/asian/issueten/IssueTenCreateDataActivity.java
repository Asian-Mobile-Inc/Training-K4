package com.example.asian.issueten;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.R;
import com.example.asian.issueten.adapter.CreateDataAdapter;
import com.example.asian.issueten.model.SellExpense;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IssueTenCreateDataActivity extends AppCompatActivity {
    private RecyclerView mRvCreateData;
    private Button mBtnCreateData;
    private List<SellExpense> mSellExpenses;
    private CreateDataAdapter mCreateDataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_ten_create_data);
        initUI();
        initListener();
        setUpListDefault();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mCreateDataAdapter = new CreateDataAdapter(mSellExpenses, this);
        mRvCreateData.setAdapter(mCreateDataAdapter);
        mRvCreateData.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initUI() {
        mRvCreateData = findViewById(R.id.rvCreateData);
        mBtnCreateData = findViewById(R.id.btnShow);
    }

    private void initListener() {
        mBtnCreateData.setOnClickListener(v -> {
            Intent intent = new Intent(IssueTenCreateDataActivity.this, IssueTenActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("sellExpenses", (ArrayList<? extends Parcelable>) mSellExpenses);
            intent.putExtra("bundleSellExpenses", bundle);
            startActivity(intent);
        });
    }
    public void setUpListDefault() {
        mSellExpenses = new ArrayList<>();
        mSellExpenses.add(new SellExpense(1, 100, 2000));
        mSellExpenses.add(new SellExpense(2, 1500, 300));
        mSellExpenses.add(new SellExpense(3, 300, 400));
        mSellExpenses.add(new SellExpense(4, 400, 500));
        mSellExpenses.add(new SellExpense(5, 300, 200));
        mSellExpenses.add(new SellExpense(6, 600, 700));
        mSellExpenses.add(new SellExpense(7, 700, 800));
        mSellExpenses.add(new SellExpense(8, 800, 900));
        mSellExpenses.add(new SellExpense(9, 900, 1000));
        mSellExpenses.add(new SellExpense(10, 1000, 1100));
        mSellExpenses.add(new SellExpense(11, 1100, 1200));
        mSellExpenses.add(new SellExpense(12, 1200, 1300));
    }
}
