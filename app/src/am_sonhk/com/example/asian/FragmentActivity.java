package com.example.asian;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_fragment_layout);
        initView();
        handleOnclick();
    }

    private void initView() {
        mFragmentClickCount = 0;
        mBtnFragmentOne = findViewById(R.id.btnFragmentOne);
        mBtnFragmentTwo = findViewById(R.id.btnFragmentTwo);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this::handleBackStackChanged);
    }

    private void handleOnclick() {
        mBtnFragmentOne.setOnClickListener(view -> {
            handleFragmentClick(
                    FragmentOne.newInstance(ContextCompat.getColor(this, R.color.green_338837)),
                    getString(R.string.fragment_one)
            );
        });

        mBtnFragmentTwo.setOnClickListener(view -> {
            handleFragmentClick(
                    FragmentTwo.newInstance(ContextCompat.getColor(this, R.color.purple_671063)),
                    getString(R.string.fragment_two)
            );
        });
    }

    private void handleFragmentClick(Fragment fragment, String nameBackStack) {
        boolean addToBackStack = shouldAddToBackStack();
        handleFragmentChange(fragment, nameBackStack, addToBackStack);
        mFragmentClickCount++; // Increment the click count after handling the fragment change
    }

    private void handleFragmentChange(Fragment fragment, String nameBackStack, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction(); // Begin transaction
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(nameBackStack);
        }
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

    private boolean shouldAddToBackStack() {
        return mFragmentClickCount < 2; // Only add to back stack for the first two clicks
    }
}
