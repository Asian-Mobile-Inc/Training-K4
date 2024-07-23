package com.example.asian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.asian.fragment.FragmentOne;
import com.example.asian.fragment.FragmentTwo;

public class FragmentActivity extends AppCompatActivity {
    Button btnFragmentOne, btnFragmentTwo;
    FragmentManager fragmentManager;

    int btn1Click = 0;
    int btn2Click = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        btnFragmentOne = findViewById(R.id.btnFragmentOne);
        btnFragmentTwo = findViewById(R.id.btnFragmentTwo);
        fragmentManager = getSupportFragmentManager();

        btnFragmentOne.setOnClickListener(view -> {
            FragmentOne fragmentOne = new FragmentOne();
            Bundle data = new Bundle();
            data.putString("color", "#319532");
            fragmentOne.setArguments(data);
            if (btn1Click > 1) {
                fragmentManager.beginTransaction().replace(R.id.frContent, fragmentOne).commit();
            } else {
                fragmentManager.beginTransaction().replace(R.id.frContent, fragmentOne).addToBackStack(null).commit();
            }
            btn1Click++;
            btn2Click = 0;
        });

        btnFragmentTwo.setOnClickListener(view -> {
            FragmentTwo fragmentTwo = new FragmentTwo();
            Bundle data = new Bundle();
            data.putString("color", "#523051");
            fragmentTwo.setArguments(data);
            if (btn2Click > 1) {
                fragmentManager.beginTransaction().add(R.id.frContent, fragmentTwo).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.frContent, fragmentTwo).addToBackStack(null).commit();
            }
            btn2Click++;
            btn1Click = 0;
        });
    }
}
