package com.example.asian.IssueFive;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.asian.IssueFive.Adapter.ViewPagerAdapter;
import com.example.asian.IssueFive.Fragment.IssueFiveFirstFragment;
import com.example.asian.IssueFive.Fragment.IssueFiveSecondFragment;
import com.example.asian.IssueFive.Fragment.IssueFiveThirdFragment;
import com.example.asian.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class IssueFiveActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private FloatingActionButton mFabAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_five);
        initUI();
        initListener();
        setUpTabLayout();
        setUpViewPager();
    }

    private void initUI() {
        mTabLayout = findViewById(R.id.tlIssueFive);
        mViewPager = findViewById(R.id.vpIssueFive);
        mFabAddItem = findViewById(R.id.fabAddItem);
    }

    private void initListener() {
        mFabAddItem.setOnClickListener(v -> {
            Log.d("androidruntime", mViewPager.getCurrentItem() + "");
            addItemFragment(mViewPager.getCurrentItem());
        });
    }

    private void setUpTabLayout() {
        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 1"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 2"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 3"));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setUpViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mTabLayout.selectTab(mTabLayout.getTabAt(position));
            }
        });
    }

    private void addItemFragment(int currentItem) {
        switch (currentItem){
            case 0:
                IssueFiveFirstFragment issueFiveFirstFragment = (IssueFiveFirstFragment) getSupportFragmentManager().findFragmentByTag("f"+currentItem);
                if (issueFiveFirstFragment != null){
                    issueFiveFirstFragment.updateListItem();
                }
                break;
            case 1:
                IssueFiveSecondFragment issueFiveSecondFragment = (IssueFiveSecondFragment) getSupportFragmentManager().findFragmentByTag("f"+currentItem);
                if (issueFiveSecondFragment != null){
                    issueFiveSecondFragment.updateListItem();
                }
                break;
            case 2:
                IssueFiveThirdFragment issueFiveThirdFragment = (IssueFiveThirdFragment) getSupportFragmentManager().findFragmentByTag("f"+currentItem);
                if (issueFiveThirdFragment != null) {
                    issueFiveThirdFragment.updateListItem();
                }
                break;
            default:
                break;
        }
    }
}
