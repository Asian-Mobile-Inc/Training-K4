package com.example.asian.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.asian.R;
import com.example.asian.fragment.HomeFragment;
import com.example.asian.fragment.SettingFragment;
import com.google.android.material.navigation.NavigationView;

public class DrawerNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static int FRAGMENT_HOME = 0;
    public static int FRAGMENT_SETTING = 1;

    private int currentFragment = FRAGMENT_HOME;
    private DrawerLayout mDlDrawer;
    private Toolbar mToolbar;
    private NavigationView mNvNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_navigation);
        initView();
    }

    private void initView() {
        mDlDrawer = findViewById(R.id.dlDrawer);
        mToolbar = findViewById(R.id.tbToolBar);
        mToolbar.setTitle(getString(R.string.home));
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDlDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDlDrawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        mNvNavigation = findViewById(R.id.nvNavigation);
        mNvNavigation.setNavigationItemSelectedListener(this);
        replaceFragment(new HomeFragment());
        mNvNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flSpaceContent, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if (currentFragment != FRAGMENT_HOME) {
                replaceFragment(new HomeFragment());
                Toast.makeText(this, getString(R.string.home_fragment), Toast.LENGTH_SHORT).show();
                mToolbar.setTitle(getString(R.string.home));
                currentFragment = FRAGMENT_HOME;
            }
        } else if (id == R.id.nav_setting) {
            if (currentFragment != FRAGMENT_SETTING) {
                replaceFragment(new SettingFragment());
                Toast.makeText(this, getString(R.string.setting_fragment), Toast.LENGTH_SHORT).show();
                mToolbar.setTitle(getString(R.string.setting));
                currentFragment = FRAGMENT_SETTING;
            }
        }
        mDlDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tool_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}