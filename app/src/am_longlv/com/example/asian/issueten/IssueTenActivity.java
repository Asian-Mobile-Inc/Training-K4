package com.example.asian.issueten;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.asian.R;
import com.example.asian.issueten.customview.MyChartView;
import com.example.asian.issueten.model.SellExpense;

import java.util.List;

public class IssueTenActivity extends AppCompatActivity {
    private MyChartView mMcvSellExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_ten);
        initUI();
        initData();
    }

    private void initUI() {
        mMcvSellExpenses = findViewById(R.id.mcvSellExpenses);
    }

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundleSellExpenses");
        if (bundle != null) {
            List<SellExpense> sellExpenses = bundle.getParcelableArrayList("sellExpenses");
            mMcvSellExpenses.setupDataChart(sellExpenses);
        }
    }
}
