package com.example.asian.adapter;

import static com.example.asian.ActionMenu.ACT_ADD;
import static com.example.asian.ActionMenu.ACT_EDIT;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.ActionMenu;
import com.example.asian.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private final Context mContext;
    private List<String> mListItem;

    public CustomAdapter(Context mContext, List<String> mListItem) {
        this.mContext = mContext;
        this.mListItem = new ArrayList<>(mListItem);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View heroView = inflater.inflate(R.layout.item_row, parent, false);
        return new ViewHolder(heroView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String nameItem = mListItem.get(position);
        holder.mTextName.setText(nameItem);
        holder.itemView.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.menu_action);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                int id = menuItem.getItemId();
                if (id == R.id.option_delete) {
                    removeItem(position);
                } else if (id == R.id.option_edit) {
                    showTextDialog("Edit Item", mListItem.get(position), position, ACT_EDIT);
                } else if (id == R.id.option_add) {
                    showTextDialog("Add Item", "", position, ACT_ADD);
                }
                return true;
            });
            popupMenu.show();
        });
    }

    public void showTextDialog(String title, String currentText, final int position, ActionMenu caseAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        final EditText edtInfoItem = new EditText(mContext);
        edtInfoItem.setText(currentText);
        builder.setView(edtInfoItem);
        builder.setPositiveButton("Save", (dialog, which) -> {
            if (caseAction == ACT_EDIT) {
                editItem(edtInfoItem.getText().toString(), position);
            } else if (caseAction == ACT_ADD) {
                addItem(edtInfoItem.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void addItem(String newItem) {
        List<String> newList = new ArrayList<>(mListItem);
        newList.add(newItem);
        updateList(newList);
    }

    public void editItem(String textInfo, int position) {
        List<String> newList = new ArrayList<>(mListItem);
        newList.set(position, textInfo);
        updateList(newList);
    }

    public void removeItem(int position) {
        List<String> newList = new ArrayList<>(mListItem);
        newList.remove(position);
        updateList(newList);
    }

    public void updateList(List<String> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(mListItem, newList));
        mListItem.clear();
        mListItem.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTextName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextName = itemView.findViewById(R.id.tvName);
        }
    }
}
