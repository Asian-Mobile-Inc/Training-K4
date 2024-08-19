package com.example.asian.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.asian.R;
import com.example.asian.adapter.ItemAdapter;
import com.example.asian.adapter.PagerAdapter;
import com.example.asian.constants.Constants;
import com.example.asian.fragment.ViewFragment;
import com.example.asian.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class TabActivity extends AppCompatActivity implements ItemAdapter.ISelectedItem {
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private FloatingActionButton mFabAdd;
    private ArrayList<Item> mItems;
    private ActivityResultLauncher<Intent> mLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        initView();
        initListener();
        createListItems();
        setAdapter();
        initLauncher();
    }

    private void initView() {
        mTabLayout = findViewById(R.id.tlTab);
        mViewPager = findViewById(R.id.vpPager);
        mFabAdd = findViewById(R.id.fabAdd);
    }

    private void initListener() {
        mFabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateItemActivity.class);
            mLauncher.launch(intent);
        });
    }

    private void initLauncher() {
        mLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();
                    if (resultCode == Constants.RESULT_CODE_ADD) {
                        if (data != null) {
                            String nameCreate = data.getStringExtra(Constants.KEY_NAME_BACK);
                            createItem(nameCreate);
                        }
                    }

                    if (resultCode == Constants.RESULT_CODE_EDIT) {
                        if (data != null) {
                            String nameEdit = data.getStringExtra(Constants.KEY_NAME_BACK);
                            int idEdit = data.getIntExtra(Constants.KEY_ID_BACK, 0);
                            editItem(new Item(idEdit, nameEdit));
                        }
                    }
                });
    }

    private void createListItems() {
        mItems = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j < 7; j++) {
                mItems.add(new Item("" + i + (char) (64 + j)));
            }
        }
    }

    private void setAdapter() {
        PagerAdapter mPagerAdapter = new PagerAdapter(this, mItems);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> tab.setText(getString(R.string.name_tab_param, position + 1))).attach();
    }

    public void createItem(String name) {
        ViewFragment myFragment = (ViewFragment) getSupportFragmentManager().findFragmentByTag("f" + mViewPager.getCurrentItem());
        if (myFragment != null) {
            myFragment.createItem(new Item(name));
        }
    }

    public void editItem(Item item) {
        ViewFragment myFragment = (ViewFragment) getSupportFragmentManager().findFragmentByTag("f" + mViewPager.getCurrentItem());
        if (myFragment != null) {
            myFragment.updateItem(item);
        }
    }

    public void deleteItem(int id) {
        ViewFragment myFragment = (ViewFragment) getSupportFragmentManager().findFragmentByTag("f" + mViewPager.getCurrentItem());
        if (myFragment != null) {
            myFragment.deleteItem(id);
        }
    }

    @Override
    public void selectedItem(Item item) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.show();
        TextView tvEditItem = dialog.findViewById(R.id.tvEditItem);
        TextView tvDeleteItem = dialog.findViewById(R.id.tvDeleteItem);
        tvDeleteItem.setOnClickListener(view1 -> {
            deleteItem(item.getId());
            dialog.dismiss();
        });
        tvEditItem.setOnClickListener(view1 -> {
            dialog.dismiss();
            Intent intent = new Intent(this, EditItemActivity.class);
            intent.putExtra(Constants.KEY_NAME_ITEM, item.getName());
            intent.putExtra(Constants.KEY_ID_ITEM, item.getId());
            mLauncher.launch(intent);
        });
    }
}
