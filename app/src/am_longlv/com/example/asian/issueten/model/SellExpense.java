package com.example.asian.issueten.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SellExpense implements Parcelable {
    private int mMonth;
    private long mExpense;
    private long mSales;

    public SellExpense(int mMonth, long mExpense, long mSales) {
        this.mMonth = mMonth;
        this.mExpense = mExpense;
        this.mSales = mSales;
    }

    protected SellExpense(Parcel in) {
        mMonth = in.readInt();
        mExpense = in.readLong();
        mSales = in.readLong();
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public long getExpense() {
        return mExpense;
    }

    public void setExpense(long mExpense) {
        this.mExpense = mExpense;
    }

    public long getSales() {
        return mSales;
    }

    public void setSales(long mSales) {
        this.mSales = mSales;
    }

    public String convertMonthToString() {
        switch (getMonth()) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(this.mMonth);
        dest.writeLong(this.mExpense);
        dest.writeLong(this.mSales);
    }

    public static final Creator<SellExpense> CREATOR = new Creator<SellExpense>() {
        @Override
        public SellExpense createFromParcel(Parcel in) {
            return new SellExpense(in);
        }

        @Override
        public SellExpense[] newArray(int size) {
            return new SellExpense[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "SellExpense{" +
                "mMonth=" + mMonth +
                ", mExpense=" + mExpense +
                ", mSales=" + mSales +
                '}';
    }
}
