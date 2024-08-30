package com.example.asian.adapterissue5;

import static com.example.asian.adapterissue5.ActionMenu.ACT_ADD;
import static com.example.asian.adapterissue5.ActionMenu.ACT_DEL;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.asian.R;
import com.example.asian.adapterissue5.adapter.ViewPager2Adapter;
import com.example.asian.adapterissue5.fragment.TabOneActivity;
import com.example.asian.adapterissue5.fragment.TabThreeActivity;
import com.example.asian.adapterissue5.fragment.TabTwoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class MainActivityIssueFive extends AppCompatActivity {

    TabLayout mTabLayout;
    ViewPager2 mViewPager2;
    ViewPager2Adapter mViewPager2Adapter;
    FloatingActionButton mFloatingActionButton;
    public static final char FIRST_CHAR = 'A';
    public static final char LAST_CHAR = 'Z';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is5_main);
        initView();

        // Initialize the ViewPager2 adapter and set it to the ViewPager2
        mViewPager2Adapter = new ViewPager2Adapter(getSupportFragmentManager(), getLifecycle());
        mViewPager2Adapter.addFragment(new TabOneActivity(createData(getString(R.string.st_frg))), getString(R.string.st_tab));
        mViewPager2Adapter.addFragment(new TabTwoActivity(createData(getString(R.string.nd_frg))), getString(R.string.nd_tab));
        mViewPager2Adapter.addFragment(new TabThreeActivity(createData(getString(R.string.rd_frg))), getString(R.string.rd_tab));
        mViewPager2.setAdapter(mViewPager2Adapter);

        // Attach the TabLayout with ViewPager2 using TabLayoutMediator
        new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) ->
                tab.setText(mViewPager2Adapter.getPageTitle(position))
        ).attach();
        handleClick();
    }

    private void handleClick() {
        mFloatingActionButton.setOnClickListener(view -> {
            CustomDialog customDialog = new CustomDialog(MainActivityIssueFive.this);
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
        int currentItem = mViewPager2.getCurrentItem();
        return mViewPager2Adapter.createFragment(currentItem);
    }

    private void initView() {
        mFloatingActionButton = findViewById(R.id.flButton);
        mViewPager2 = findViewById(R.id.viewPager2);
        mTabLayout = findViewById(R.id.tbLayout);
    }

    public ArrayList<String> createData(String name) {
        ArrayList<String> stringList = new ArrayList<>();
        for (char ch = FIRST_CHAR; ch <= LAST_CHAR; ch++) {
            stringList.add(getString(R.string.item) + name + ch);
        }
        return stringList;
    }

    public void showTextDialog(String title, String hintText, ActionMenu caseAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        final EditText edtInfoItem = new EditText(this);
        edtInfoItem.setHint(hintText);
        builder.setView(edtInfoItem);
        builder.setPositiveButton(getString(R.string.save), (dialog, which) -> {
            try {
                handleAction(edtInfoItem.getText().toString(), caseAction);
            } catch (Exception e) {
                Toast.makeText(MainActivityIssueFive.this, getString(R.string.error) + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
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
            mButtonAdd.setOnClickListener(view -> showTextDialog(getString(R.string.add_item),
                    getString(R.string.enter_data), ACT_ADD));
            mButtonDel.setOnClickListener(view -> showTextDialog(getString(R.string.delete_item),
                    getString(R.string.enter_data), ACT_DEL));
        }
    }
}
