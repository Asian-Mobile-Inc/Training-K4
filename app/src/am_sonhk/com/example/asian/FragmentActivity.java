package com.example.asian;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.asian.fragment.FragmentOne;
import com.example.asian.fragment.FragmentTwo;

public class FragmentActivity extends AppCompatActivity {

    private int mFragmentClickCount;
    private int mLastClickTime = 0;
    private FragmentManager mFragmentManager;
    private Button mBtnFragmentOne;
    private Button mBtnFragmentTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_fragment_layout);
        initView();
        handleOnclick();
    }

    private void handleOnclick() {
        mBtnFragmentOne.setOnClickListener(view -> {
//            deleteBackStack();
            handleFragmentChange(
                    FragmentOne.newInstance(ContextCompat.getColor(this, R.color.green_338837)),
                    getString(R.string.fragment_one)
            );
        });
        mBtnFragmentTwo.setOnClickListener(view -> {
//            deleteBackStack();
            handleFragmentChange(
                    FragmentTwo.newInstance(ContextCompat.getColor(this, R.color.purple_671063)),
                    getString(R.string.fragment_two)
            );
        });
    }

    private void initView() {
        mFragmentClickCount = 0;
        mBtnFragmentOne = findViewById(R.id.btnFragmentOne);
        mBtnFragmentTwo = findViewById(R.id.btnFragmentTwo);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this::handleBackStackChanged);
    }

    private void handleFragmentChange(Fragment fragment, String nameBackStack) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction(); // Begin transaction
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(nameBackStack);
        fragmentTransaction.commit();
    }

    private void handleBackStackChanged() {
        int backStackEntryCount = mFragmentManager.getBackStackEntryCount(); // Get total item in list backstack
        if (backStackEntryCount > 0) {
            FragmentManager.BackStackEntry backEntry = mFragmentManager.getBackStackEntryAt(backStackEntryCount - 1);  // Get the nearly item in list
            String fragmentName = backEntry.getName();
            setTitle(fragmentName);
        }
    }

    private void deleteBackStack() {
        mFragmentClickCount++;
        if (mFragmentClickCount > 2) {
            mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // Delete list backstack
            mFragmentClickCount = 0;
        } else if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        mLastClickTime = (int) SystemClock.elapsedRealtime();
    }
}
