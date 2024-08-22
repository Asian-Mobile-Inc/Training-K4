package com.example.asian.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;
import com.example.asian.constants.Constants;
import com.example.asian.enums.EnumMonth;
import com.example.asian.model.SellExpense;

import java.util.ArrayList;

public class InputSaleActivity extends AppCompatActivity {
    private EditText mEdtJanuarySales;
    private EditText mEdtFebruarySales;
    private EditText mEdtMarchSales;
    private EditText mEdtAprilSales;
    private EditText mEdtMaySales;
    private EditText mEdtJuneSales;
    private EditText mEdtJulySales;
    private EditText mEdtAugustSales;
    private EditText mEdtSeptemberSales;
    private EditText mEdtOctoberSales;
    private EditText mEdtNovemberSales;
    private EditText mEdtDecemberSales;
    private EditText mEdtJanuaryExpenses;
    private EditText mEdtFebruaryExpenses;
    private EditText mEdtMarchExpenses;
    private EditText mEdtAprilExpenses;
    private EditText mEdtMayExpenses;
    private EditText mEdtJuneExpenses;
    private EditText mEdtJulyExpenses;
    private EditText mEdtAugustExpenses;
    private EditText mEdtSeptemberExpenses;
    private EditText mEdtOctoberExpenses;
    private EditText mEdtNovemberExpenses;
    private EditText mEdtDecemberExpenses;
    private Button mBtnSend;
    private ArrayList<SellExpense> sellExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_sale);
        initView();
        initListener();
    }

    private void initView() {
        mEdtJanuarySales = findViewById(R.id.edtJanuarySales);
        mEdtFebruarySales = findViewById(R.id.edtFebruarySales);
        mEdtMarchSales = findViewById(R.id.edtMarchSales);
        mEdtAprilSales = findViewById(R.id.edtAprilSales);
        mEdtMaySales = findViewById(R.id.edtMaySales);
        mEdtJuneSales = findViewById(R.id.edtJuneSales);
        mEdtJulySales = findViewById(R.id.edtJulySales);
        mEdtAugustSales = findViewById(R.id.edtAugustSales);
        mEdtSeptemberSales = findViewById(R.id.edtSeptemberSales);
        mEdtOctoberSales = findViewById(R.id.edtOctoberSales);
        mEdtNovemberSales = findViewById(R.id.edtNovemberSales);
        mEdtDecemberSales = findViewById(R.id.edtDecemberSales);
        mEdtJanuaryExpenses = findViewById(R.id.edtJanuaryExpenses);
        mEdtFebruaryExpenses = findViewById(R.id.edtFebruaryExpenses);
        mEdtMarchExpenses = findViewById(R.id.edtMarchExpenses);
        mEdtAprilExpenses = findViewById(R.id.edtAprilExpenses);
        mEdtMayExpenses = findViewById(R.id.edtMayExpenses);
        mEdtJuneExpenses = findViewById(R.id.edtJuneExpenses);
        mEdtJulyExpenses = findViewById(R.id.edtJulyExpenses);
        mEdtAugustExpenses = findViewById(R.id.edtAugustExpenses);
        mEdtSeptemberExpenses = findViewById(R.id.edtSeptemberExpenses);
        mEdtOctoberExpenses = findViewById(R.id.edtOctoberExpenses);
        mEdtNovemberExpenses = findViewById(R.id.edtNovemberExpenses);
        mEdtDecemberExpenses = findViewById(R.id.edtDecemberExpenses);
        mBtnSend = findViewById(R.id.btnSend);
    }

    private void initListener() {
        sellExpenses = new ArrayList<>();
        mBtnSend.setOnClickListener(view -> {
            sellExpenses.clear();
            if (!mEdtJanuarySales.getText().toString().isEmpty() || !mEdtJanuaryExpenses.getText().toString().isEmpty()) {
                SellExpense s = new SellExpense(EnumMonth.Jan.name());
                if (!mEdtJanuarySales.getText().toString().isEmpty()) {
                    s.setSales(Integer.parseInt(mEdtJanuarySales.getText().toString()));
                }
                if (!mEdtJanuaryExpenses.getText().toString().isEmpty()) {
                    s.setExpenses(Integer.parseInt(mEdtJanuaryExpenses.getText().toString()));
                }
                sellExpenses.add(s);
            }

            if (!mEdtFebruarySales.getText().toString().isEmpty() || !mEdtFebruaryExpenses.getText().toString().isEmpty()) {
                SellExpense s = new SellExpense(EnumMonth.Jan.name());
                if (!mEdtFebruarySales.getText().toString().isEmpty()) {
                    s.setSales(Integer.parseInt(mEdtFebruarySales.getText().toString()));
                }
                if (!mEdtFebruaryExpenses.getText().toString().isEmpty()) {
                    s.setExpenses(Integer.parseInt(mEdtFebruaryExpenses.getText().toString()));
                }
                sellExpenses.add(s);
            }

            if (!mEdtMarchSales.getText().toString().isEmpty() || !mEdtMarchExpenses.getText().toString().isEmpty()) {
                SellExpense s = new SellExpense(EnumMonth.Jan.name());
                if (!mEdtMarchSales.getText().toString().isEmpty()) {
                    s.setSales(Integer.parseInt(mEdtMarchSales.getText().toString()));
                }
                if (!mEdtMarchExpenses.getText().toString().isEmpty()) {
                    s.setExpenses(Integer.parseInt(mEdtMarchExpenses.getText().toString()));
                }
                sellExpenses.add(s);
            }

            if (!mEdtAprilSales.getText().toString().isEmpty() || !mEdtAprilExpenses.getText().toString().isEmpty()) {
                SellExpense s = new SellExpense(EnumMonth.Jan.name());
                if (!mEdtAprilSales.getText().toString().isEmpty()) {
                    s.setSales(Integer.parseInt(mEdtAprilSales.getText().toString()));
                }
                if (!mEdtAprilExpenses.getText().toString().isEmpty()) {
                    s.setExpenses(Integer.parseInt(mEdtAprilExpenses.getText().toString()));
                }
                sellExpenses.add(s);
            }

            if (!mEdtMaySales.getText().toString().isEmpty() || !mEdtMayExpenses.getText().toString().isEmpty()) {
                SellExpense s = new SellExpense(EnumMonth.Jan.name());
                if (!mEdtMaySales.getText().toString().isEmpty()) {
                    s.setSales(Integer.parseInt(mEdtMaySales.getText().toString()));
                }
                if (!mEdtMayExpenses.getText().toString().isEmpty()) {
                    s.setExpenses(Integer.parseInt(mEdtMayExpenses.getText().toString()));
                }
                sellExpenses.add(s);
            }

            if (!mEdtJuneSales.getText().toString().isEmpty() || !mEdtJuneExpenses.getText().toString().isEmpty()) {
                SellExpense s = new SellExpense(EnumMonth.Jan.name());
                if (!mEdtJuneSales.getText().toString().isEmpty()) {
                    s.setSales(Integer.parseInt(mEdtJuneSales.getText().toString()));
                }
                if (!mEdtJuneExpenses.getText().toString().isEmpty()) {
                    s.setExpenses(Integer.parseInt(mEdtJuneExpenses.getText().toString()));
                }
                sellExpenses.add(s);
            }

            if (!mEdtJulySales.getText().toString().isEmpty() || !mEdtJulyExpenses.getText().toString().isEmpty()) {
                SellExpense s = new SellExpense(EnumMonth.Jan.name());
                if (!mEdtJulySales.getText().toString().isEmpty()) {
                    s.setSales(Integer.parseInt(mEdtJulySales.getText().toString()));
                }
                if (!mEdtJulyExpenses.getText().toString().isEmpty()) {
                    s.setExpenses(Integer.parseInt(mEdtJulyExpenses.getText().toString()));
                }
                sellExpenses.add(s);
            }

            if (!mEdtAugustSales.getText().toString().isEmpty() || !mEdtAugustExpenses.getText().toString().isEmpty()) {
                SellExpense s = new SellExpense(EnumMonth.Jan.name());
                if (!mEdtAugustSales.getText().toString().isEmpty()) {
                    s.setSales(Integer.parseInt(mEdtAugustSales.getText().toString()));
                }
                if (!mEdtAugustExpenses.getText().toString().isEmpty()) {
                    s.setExpenses(Integer.parseInt(mEdtAugustExpenses.getText().toString()));
                }
                sellExpenses.add(s);
            }

            if (!mEdtSeptemberSales.getText().toString().isEmpty() || !mEdtSeptemberExpenses.getText().toString().isEmpty()) {
                SellExpense s = new SellExpense(EnumMonth.Jan.name());
                if (!mEdtSeptemberSales.getText().toString().isEmpty()) {
                    s.setSales(Integer.parseInt(mEdtSeptemberSales.getText().toString()));
                }
                if (!mEdtSeptemberExpenses.getText().toString().isEmpty()) {
                    s.setExpenses(Integer.parseInt(mEdtSeptemberExpenses.getText().toString()));
                }
                sellExpenses.add(s);
            }

            if (!mEdtOctoberSales.getText().toString().isEmpty() || !mEdtOctoberExpenses.getText().toString().isEmpty()) {
                SellExpense s = new SellExpense(EnumMonth.Jan.name());
                if (!mEdtOctoberSales.getText().toString().isEmpty()) {
                    s.setSales(Integer.parseInt(mEdtOctoberSales.getText().toString()));
                }
                if (!mEdtOctoberExpenses.getText().toString().isEmpty()) {
                    s.setExpenses(Integer.parseInt(mEdtOctoberExpenses.getText().toString()));
                }
                sellExpenses.add(s);
            }

            if (!mEdtNovemberSales.getText().toString().isEmpty() || !mEdtNovemberExpenses.getText().toString().isEmpty()) {
                SellExpense s = new SellExpense(EnumMonth.Jan.name());
                if (!mEdtNovemberSales.getText().toString().isEmpty()) {
                    s.setSales(Integer.parseInt(mEdtNovemberSales.getText().toString()));
                }
                if (!mEdtNovemberExpenses.getText().toString().isEmpty()) {
                    s.setExpenses(Integer.parseInt(mEdtNovemberExpenses.getText().toString()));
                }
                sellExpenses.add(s);
            }

            if (!mEdtDecemberSales.getText().toString().isEmpty() || !mEdtDecemberExpenses.getText().toString().isEmpty()) {
                SellExpense s = new SellExpense(EnumMonth.Jan.name());
                if (!mEdtDecemberSales.getText().toString().isEmpty()) {
                    s.setSales(Integer.parseInt(mEdtDecemberSales.getText().toString()));
                }
                if (!mEdtDecemberExpenses.getText().toString().isEmpty()) {
                    s.setExpenses(Integer.parseInt(mEdtDecemberExpenses.getText().toString()));
                }
                sellExpenses.add(s);
            }

            Intent intent = new Intent(this, CustomViewActivity.class);
            System.out.println(sellExpenses.size());
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Constants.KEY_LIST_SELL_EXPENSES, sellExpenses);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}
