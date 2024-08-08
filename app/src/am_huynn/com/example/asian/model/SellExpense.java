package com.example.asian.model;

import com.example.asian.enums.EnumMonth;

public class SellExpense {
    private EnumMonth month;
    private float sales;
    private float expenses;

    public SellExpense(EnumMonth month, int sales, int expenses) {
        this.month = month;
        this.sales = sales;
        this.expenses = expenses;
    }

    public EnumMonth getMonth() {
        return month;
    }

    public void setMonth(EnumMonth month) {
        this.month = month;
    }

    public float getSales() {
        return sales;
    }

    public void setSales(float sales) {
        this.sales = sales;
    }

    public float getExpenses() {
        return expenses;
    }

    public void setExpenses(float expenses) {
        this.expenses = expenses;
    }
}
