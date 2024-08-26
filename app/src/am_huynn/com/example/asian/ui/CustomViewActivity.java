package com.example.asian.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;
import com.example.asian.constants.Constants;
import com.example.asian.enums.EnumMonth;
import com.example.asian.model.SellExpense;
import com.example.asian.view.MyChartView;

import java.util.ArrayList;

public class CustomViewActivity extends AppCompatActivity {
    private MyChartView mMcvMyChart;
    private ArrayList<SellExpense> mSellExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        initView();
        initListSellExpenses();
    }

    private void initView() {
        mMcvMyChart = findViewById(R.id.mcvMyChart);
    }

    private void initListSellExpenses() {
        mSellExpenses = new ArrayList<>();
        mSellExpenses.add(new SellExpense(EnumMonth.Jan.name(), 65000, 10000));
        mSellExpenses.add(new SellExpense(EnumMonth.Fed.name(), 80000, 18000));
        mSellExpenses.add(new SellExpense(EnumMonth.Mar.name(), 78000, 20000));
        mSellExpenses.add(new SellExpense(EnumMonth.Apr.name(), 78000, 20000));
        mSellExpenses.add(new SellExpense(EnumMonth.May.name(), 110000, 42000));
        mSellExpenses.add(new SellExpense(EnumMonth.Jun.name(), 130000, 80000));
        mSellExpenses.add(new SellExpense(EnumMonth.Jul.name(), 130000, 80000));
        mSellExpenses.add(new SellExpense(EnumMonth.Aug.name(), 100000, 70000));
        mMcvMyChart.setSellExpenses(mSellExpenses);
    }
}
