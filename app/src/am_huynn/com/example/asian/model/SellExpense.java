package com.example.asian.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SellExpense implements Parcelable {
    private String month;
    private float sales = 0;
    private float expenses = 0;

    public SellExpense(String month) {
        this.month = month;
    }

    public SellExpense(String month, int sales, int expenses) {
        this.month = month;
        this.sales = sales;
        this.expenses = expenses;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(month);
        parcel.writeFloat(sales);
        parcel.writeFloat(expenses);
    }

    public SellExpense(Parcel in) {
        month = in.readString();
        sales = in.readFloat();
        expenses = in.readFloat();
    }

    public static final Parcelable.Creator<SellExpense> CREATOR = new Parcelable.Creator<SellExpense>() {
        public SellExpense createFromParcel(Parcel in) {
            return new SellExpense(in);
        }

        public SellExpense[] newArray(int size) {
            return new SellExpense[size];
        }
    };
}
