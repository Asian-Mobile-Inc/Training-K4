package com.example.asian.issueten.model;

public class SellExpense {
    private int mMonth;
    private long mExpense;
    private long mSales;

    public SellExpense(int mMonth, long mExpense, long mSales) {
        this.mMonth = mMonth;
        this.mExpense = mExpense;
        this.mSales = mSales;
    }

    public int getmMonth() {
        return mMonth;
    }

    public void setmMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public long getmExpense() {
        return mExpense;
    }

    public void setmExpense(long mExpense) {
        this.mExpense = mExpense;
    }

    public long getmSales() {
        return mSales;
    }

    public void setmSales(long mSales) {
        this.mSales = mSales;
    }
}
