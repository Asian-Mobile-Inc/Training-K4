package com.example.asian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.R;
import com.example.asian.diff.ItemDiffUtilCallBack;
import com.example.asian.model.Item;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private final ArrayList<Item> mItems;
    private final Context mContext;
    private ISelectedItem mSelectedItem;

    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.mContext = context;
        this.mItems = new ArrayList<>(items);
    }

    public void setData(ArrayList<Item> newItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ItemDiffUtilCallBack(mItems, newItems));
        mItems.clear();
        this.mItems.addAll(newItems);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = mItems.get(position);
        holder.mTvItemName.setText(item.getName());

        holder.mLlItem.setOnClickListener(view -> {
            if (mContext instanceof ISelectedItem) {
                mSelectedItem = (ISelectedItem) mContext;
                mSelectedItem.selectedItem(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvItemName;
        private LinearLayout mLlItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            mTvItemName = view.findViewById(R.id.tvItemName);
            mLlItem = view.findViewById(R.id.llItem);
        }
    }

    public interface ISelectedItem {
        void selectedItem(Item item);
    }
}
