package com.example.asian.issueten.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.R;
import com.example.asian.issueten.model.SellExpense;

import java.util.List;
import java.util.Objects;

public class CreateDataAdapter extends RecyclerView.Adapter<CreateDataAdapter.ViewHolder> {
    private final List<SellExpense> mSellExpenses;
    private final Context mContext;

    public CreateDataAdapter(List<SellExpense> mSellExpenses, Context mContext) {
        this.mSellExpenses = mSellExpenses;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_create_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SellExpense sellExpense = mSellExpenses.get(position);
        holder.mTvMonth.setText(sellExpense.convertMonthToString());
        holder.mTvExpense.setText(String.valueOf(sellExpense.getExpense()));
        holder.mTvSales.setText(String.valueOf(sellExpense.getSales()));
        holder.mBtnEdit.setOnClickListener(v -> showDiaLogEdit(sellExpense));
    }

    @Override
    public int getItemCount() {
        if (mSellExpenses == null) {
            return 0;
        } else {
            return mSellExpenses.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvMonth;
        public TextView mTvExpense;
        public TextView mTvSales;
        public Button mBtnEdit;
        public Button mBtnHide;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initUI(itemView);
        }

        private void initUI(View itemView) {
            mTvMonth = itemView.findViewById(R.id.tvMonth);
            mTvExpense = itemView.findViewById(R.id.tvExpenses);
            mTvSales = itemView.findViewById(R.id.tvSales);
            mBtnEdit = itemView.findViewById(R.id.btnEdit);
            mBtnHide = itemView.findViewById(R.id.btnHide);
        }
    }

    private void showDiaLogEdit(SellExpense sellExpense) {
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_edit_data_is_ten);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        TextView tvMonth = dialog.findViewById(R.id.tvMonth);
        TextView edtExpense = dialog.findViewById(R.id.edtExpense);
        TextView edtSales = dialog.findViewById(R.id.edtSales);
        tvMonth.setText(sellExpense.convertMonthToString());
        edtExpense.setText(String.valueOf(sellExpense.getExpense()));
        edtSales.setText(String.valueOf(sellExpense.getSales()));
        Button btnSave = dialog.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            sellExpense.setExpense(Long.parseLong(edtExpense.getText().toString()));
            sellExpense.setSales(Long.parseLong(edtSales.getText().toString()));
            dialog.dismiss();
        });
        notifyItemChanged(mSellExpenses.indexOf(sellExpense));
    }
}
