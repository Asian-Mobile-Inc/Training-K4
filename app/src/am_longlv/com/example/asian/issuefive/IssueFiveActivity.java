package com.example.asian.issuefive;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.asian.issuefive.adapter.RecyclerViewAdapter;
import com.example.asian.issuefive.adapter.ViewPagerAdapter;
import com.example.asian.issuefive.fragment.IssueFiveFragment;
import com.example.asian.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class IssueFiveActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemSelected {
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
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager,
                (tab, position) -> tab.setText(String.format(getString(R.string.tab_int_param), position + 1))
        ).attach();
    }

    private void addItemFragment(int currentItem, String name) {
        IssueFiveFragment issueFiveFragment = (IssueFiveFragment) getSupportFragmentManager().findFragmentByTag("f" + currentItem);
        if (issueFiveFragment != null) {
            issueFiveFragment.updateListItem(name);
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

    @Override
    public void onItemSelected(int position) {
        int currentItem = mViewPager.getCurrentItem();
        IssueFiveFragment issueFiveFragment = (IssueFiveFragment) getSupportFragmentManager().findFragmentByTag("f" + currentItem);
        if (issueFiveFragment != null) {
            issueFiveFragment.showDialogActionItem(position);
        }
    }
}
