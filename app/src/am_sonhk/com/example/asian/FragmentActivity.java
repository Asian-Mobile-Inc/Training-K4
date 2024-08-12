package com.example.asian;

import android.os.Bundle;
import android.util.Log;
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
    private FragmentManager mFragmentManager;
    private Button mBtnFragmentOne;
    private Button mBtnFragmentTwo;
    private static final int MAX_ON_CLICK = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_fragment_layout);
        initView();
        handleOnClick();
    }

    private void initView() {
        mFragmentClickCount = 0;
        mBtnFragmentOne = findViewById(R.id.btnFragmentOne);
        mBtnFragmentTwo = findViewById(R.id.btnFragmentTwo);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this::handleBackStackChanged);
    }

    private void handleOnClick() {
        mBtnFragmentOne.setOnClickListener(view -> {
            handleFragmentReplace(
                    FragmentOne.newInstance(ContextCompat.getColor(this, R.color.green_338837)),
                    getString(R.string.fragment_one)
            );
        });

        mBtnFragmentTwo.setOnClickListener(view -> {
            handleFragmentAdd(
                    FragmentTwo.newInstance(ContextCompat.getColor(this, R.color.purple_671063)),
                    getString(R.string.fragment_two)
            );
        });
    }

    private void handleFragmentAdd(Fragment fragment, String nameBackStack) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment);
        if (mFragmentClickCount < MAX_ON_CLICK) {
            transaction.addToBackStack(nameBackStack);
        }
        transaction.commit();
        mFragmentClickCount++;
    }

    private void handleFragmentReplace(Fragment fragment, String nameBackStack) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);

        if (mFragmentClickCount < MAX_ON_CLICK) {
            transaction.addToBackStack(nameBackStack);
        }
        transaction.commit();
        mFragmentClickCount++;;
    }

    private void handleBackStackChanged() {
        int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            FragmentManager.BackStackEntry backEntry = mFragmentManager.getBackStackEntryAt(backStackEntryCount - 1);
            String fragmentName = backEntry.getName();
            setTitle(fragmentName);
        }
    }
}
