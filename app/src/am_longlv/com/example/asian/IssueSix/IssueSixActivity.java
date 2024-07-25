package com.example.asian.IssueSix;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.asian.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class IssueSixActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNvIssueSix;
    private FloatingActionButton mFabIssueSix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_six);
        initUI();
        initListener();
        setUpDrawerLayout();
        setUpNavigationView();
    }

    private void initUI() {
        mToolbar = findViewById(R.id.tbIssuesSix);
        mDrawerLayout = findViewById(R.id.dlIssueSix);
        mNvIssueSix = findViewById(R.id.nvIssueSix);
        mFabIssueSix = findViewById(R.id.fabIssueSix);
    }

    private void initListener() {
        mFabIssueSix.setOnClickListener(v -> {
            Snackbar.make(v, getString(R.string.replace_with_your_own_action), Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        });

    }

    private void setUpDrawerLayout() {
        setSupportActionBar(mToolbar);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setStatusbarColor();
    }

    private void setStatusbarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.red_A8FF0000));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        eventOnClickMenu(item.getItemId());
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_drawer_issue_six, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setUpNavigationView() {
        mNvIssueSix.setNavigationItemSelectedListener(item -> {
            eventOnClickMenu(item.getItemId());
            mDrawerLayout.closeDrawers();
            return true;
        });
    }

    private void eventOnClickMenu(int id) {
        switch (id) {
            case (R.id.navHome):
                mToolbar.setTitle(getString(R.string.home));
                break;
            case (R.id.navSetting):
                mToolbar.setTitle(getString(R.string.setting));
                break;
            case (R.id.navShare):
                mToolbar.setTitle(getString(R.string.share));
                break;
            case (R.id.navSend):
                mToolbar.setTitle(getString(R.string.send));
                break;
            default:
                break;
        }
    }
}
