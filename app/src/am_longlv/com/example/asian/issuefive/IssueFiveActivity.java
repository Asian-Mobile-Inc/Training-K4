package com.example.asian.issuefive;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.asian.issuefive.adapter.ViewPagerAdapter;
import com.example.asian.issuefive.fragment.IssueFiveFirstFragment;
import com.example.asian.issuefive.fragment.IssueFiveSecondFragment;
import com.example.asian.issuefive.fragment.IssueFiveThirdFragment;
import com.example.asian.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class IssueFiveActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private FloatingActionButton mFabAddItem;
    private static final String NAME_TAG_ONE = "Tab 1", NAME_TAG_TWO = "Tab 2", NAME_TAG_THREE = "Tab 3";

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
        mFabAddItem.setOnClickListener(v -> showDialogAddItem(mViewPager.getCurrentItem()));
    }

    private void setUpTabLayout() {
        mTabLayout.addTab(mTabLayout.newTab().setText(NAME_TAG_ONE));
        mTabLayout.addTab(mTabLayout.newTab().setText(NAME_TAG_TWO));
        mTabLayout.addTab(mTabLayout.newTab().setText(NAME_TAG_THREE));
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

    private void addItemFragment(int currentItem, String name) {
        switch (currentItem) {
            case 0:
                IssueFiveFirstFragment issueFiveFirstFragment = (IssueFiveFirstFragment) getSupportFragmentManager().findFragmentByTag("f" + currentItem);
                if (issueFiveFirstFragment != null) {
                    issueFiveFirstFragment.updateListItem(name);
                }
                break;
            case 1:
                IssueFiveSecondFragment issueFiveSecondFragment = (IssueFiveSecondFragment) getSupportFragmentManager().findFragmentByTag("f" + currentItem);
                if (issueFiveSecondFragment != null) {
                    issueFiveSecondFragment.updateListItem(name);
                }
                break;
            case 2:
                IssueFiveThirdFragment issueFiveThirdFragment = (IssueFiveThirdFragment) getSupportFragmentManager().findFragmentByTag("f" + currentItem);
                if (issueFiveThirdFragment != null) {
                    issueFiveThirdFragment.updateListItem(name);
                }
                break;
            default:
                break;
        }
    }

    private void showDialogAddItem(int currentItem) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_name);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            addItemFragment(currentItem, ((EditText) dialog.findViewById(R.id.edtNewName)).getText().toString());
            dialog.dismiss();
        });
        dialog.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
