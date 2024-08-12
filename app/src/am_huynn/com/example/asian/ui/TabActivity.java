package com.example.asian.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

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

public class TabActivity extends AppCompatActivity implements ItemAdapter.IDeleteItem {
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private FloatingActionButton mFabAdd;
    private ArrayList<Item> mItems;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        initView();
        initListener();
        createListItems();
        setAdapter();
    }
    private void initView() {
        mTabLayout = findViewById(R.id.tlTab);
        mViewPager = findViewById(R.id.vpPager);
        mFabAdd = findViewById(R.id.fabAdd);
    }

    private void initListener() {
        mFabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateItemActivity.class);
            startActivityForResult(intent, Constants.RESULT_CODE_ADD);
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
        mPagerAdapter = new PagerAdapter(this, mItems);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        new TabLayoutMediator(mTabLayout,mViewPager,(tab, position) -> {
            tab.setText("Tab "+(position+1));
        }).attach();
    }

    public void createItem(String name) {
        ViewFragment myFragment = (ViewFragment) getSupportFragmentManager().findFragmentByTag("f" + mViewPager.getCurrentItem());
        if(myFragment != null) {
            myFragment.createItem(new Item(name));
        }
    }

    public void editItem(Item item) {
        ViewFragment myFragment = (ViewFragment) getSupportFragmentManager().findFragmentByTag("f" + mViewPager.getCurrentItem());
        if(myFragment != null) {
            myFragment.updateItem(item);
        }
    }

    @Override
    public void deleteItem(int id) {
        ViewFragment myFragment = (ViewFragment) getSupportFragmentManager().findFragmentByTag("f" + mViewPager.getCurrentItem());
        if(myFragment != null) {
            myFragment.deleteItem(id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RESULT_CODE_ADD) {
            if (data != null) {
                String nameCreate = data.getStringExtra(Constants.KEY_NAME_BACK);
                createItem(nameCreate);
            }
        }

        if (requestCode == Constants.RESULT_CODE_EDIT) {
            if (data != null) {
                String nameEdit = data.getStringExtra(Constants.KEY_NAME_BACK);
                int idEdit = data.getIntExtra(Constants.KEY_ID_BACK, 0);
                editItem(new Item(idEdit, nameEdit));
            }
        }
    }
}