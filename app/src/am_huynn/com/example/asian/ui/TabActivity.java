package com.example.asian.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

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

import java.util.ArrayList;

public class TabActivity extends AppCompatActivity implements ItemAdapter.IDeleteItem {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
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
        mViewPager = findViewById(R.id.vpPaper);
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        createListItems();
        mPagerAdapter = new PagerAdapter(fragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mItems);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void createItem(String name) {
        ViewFragment myFragment = (ViewFragment) mPagerAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
        myFragment.createItem(new Item(name));
    }

    public void editItem(Item item) {
        ViewFragment myFragment = (ViewFragment) mPagerAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
        myFragment.updateItem(item);
    }

    @Override
    public void deleteItem(int id) {
        ViewFragment myFragment = (ViewFragment) mPagerAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
        myFragment.deleteItem(id);
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