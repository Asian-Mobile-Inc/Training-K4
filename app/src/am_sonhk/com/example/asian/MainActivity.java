package com.example.asian;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.example.asian.ActionMenu.ACT_ADD;
import static com.example.asian.ActionMenu.ACT_DEL;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.asian.adapter.ViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;
    FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPagerAdapter.addFragment(new TabOneActivity(createData("1")), "Tab-1");
        mViewPagerAdapter.addFragment(new TabTwoActivity(createData("2")), "Tab-2");
        mViewPagerAdapter.addFragment(new TabThreeActivity(createData("3")), "Tab-3");
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        handleClick();
    }

    private void handleClick() {
        mFloatingActionButton.setOnClickListener(view -> {
            CustomDialog customDialog = new CustomDialog(MainActivity.this);
            customDialog.show();
        });
    }

    private void handleAction(String value, ActionMenu caseAction) {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof TabOneActivity) {
            if (caseAction == ACT_ADD) {
                ((TabOneActivity) currentFragment).addDataToRecyclerView(value);
            } else {
                ((TabOneActivity) currentFragment).delDataToRecyclerView(value);
            }
        } else if (currentFragment instanceof TabTwoActivity) {
            if (caseAction == ACT_ADD) {
                ((TabTwoActivity) currentFragment).addDataToRecyclerView(value);
            } else {
                ((TabTwoActivity) currentFragment).delDataToRecyclerView(value);
            }
        } else if (currentFragment instanceof TabThreeActivity) {
            if (caseAction == ACT_ADD) {
                ((TabThreeActivity) currentFragment).addDataToRecyclerView(value);
            } else {
                ((TabThreeActivity) currentFragment).delDataToRecyclerView(value);
            }
        }
    }

    private Fragment getCurrentFragment() {
        int currentItem = mViewPager.getCurrentItem();
        return mViewPagerAdapter.getItem(currentItem);
    }

    private void initView() {
        mFloatingActionButton = findViewById(R.id.flButton);
        mViewPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tbLayout);
    }

    public ArrayList<String> createData(String name) {
        ArrayList<String> stringList = new ArrayList<>();
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            stringList.add("Item " + name + ch);
        }
        return stringList;
    }

    public void showTextDialog(String title, String hintText, ActionMenu caseAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        final EditText edtInfoItem = new EditText(this);
        edtInfoItem.setHint(hintText);
        builder.setView(edtInfoItem);
        builder.setPositiveButton("Save", (dialog, which) -> {
            try {
                handleAction(edtInfoItem.getText().toString(), caseAction);
            } catch (Exception e) {
                Log.e("MainActivity", "Error handling action: " + e.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public class CustomDialog extends Dialog {

        public Button mButtonAdd;
        public Button mButtonDel;

        public CustomDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_menu);
            initView();
            handleClick();
        }

        private void initView() {
            mButtonAdd = findViewById(R.id.btnAdd);
            mButtonDel = findViewById(R.id.btnDel);
        }

        private void handleClick() {
            mButtonAdd.setOnClickListener(view -> showTextDialog("Add Item", "Enter data", ACT_ADD));
            mButtonDel.setOnClickListener(view -> showTextDialog("Delete Item", "Enter data", ACT_DEL));
        }
    }
}
