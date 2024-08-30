package com.example.asian.issuesix;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
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
    private TextView mTvShowTitleMenu;
    private boolean mIsFavourite = false;

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
        mTvShowTitleMenu = findViewById(R.id.tvShowItemMenu);
    }

    private void initListener() {
        mFabIssueSix.setOnClickListener(v -> {
            showSnakeBar(v, getString(R.string.replace_with_your_own_action));
        });
    }

    private void setUpDrawerLayout() {
        setSupportActionBar(mToolbar);
        mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setStatusBarColor();
    }

    private void setStatusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.red_FF0000));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_favourite):
                Drawable drawable = item.getIcon();
                if (drawable != null) {
                    int color;
                    if (!mIsFavourite) {
                        color = ContextCompat.getColor(this, R.color.pink_FF5C78);
                    } else {
                        color = ContextCompat.getColor(this, R.color.white);
                    }
                    mIsFavourite = !mIsFavourite;
                    DrawableCompat.setTint(drawable, color);
                    item.setIcon(drawable);
                    showSnakeBar(mDrawerLayout, getString(R.string.favourite));
                }
                return true;
            case (R.id.action_settings):
                mNvIssueSix.setCheckedItem(R.id.navSetting);
                mTvShowTitleMenu.setText(getString(R.string.setting));
                showSnakeBar(mDrawerLayout, getString(R.string.setting));
                return true;
            case (R.id.action_home):
                mNvIssueSix.setCheckedItem(R.id.navHome);
                mTvShowTitleMenu.setText(getString(R.string.home));
                showSnakeBar(mDrawerLayout, getString(R.string.home));
                return true;
            default:
                break;
        }
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second_drawer_issue_six, menu);
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
                mTvShowTitleMenu.setText(getString(R.string.home));
                showSnakeBar(mDrawerLayout, getString(R.string.home));
                break;
            case (R.id.navSetting):
                mTvShowTitleMenu.setText(getString(R.string.setting));
                showSnakeBar(mDrawerLayout, getString(R.string.setting));
                break;
            case (R.id.navShare):
                mTvShowTitleMenu.setText(getString(R.string.share));
                showSnakeBar(mDrawerLayout, getString(R.string.share));
                break;
            case (R.id.navSend):
                mTvShowTitleMenu.setText(getString(R.string.send));
                showSnakeBar(mDrawerLayout, getString(R.string.send));
                break;
            default:
                break;
        }
    }

    private void showSnakeBar(View v, String text) {
        if (v != null) {
            Snackbar.make(v, text, Snackbar.LENGTH_LONG)
                    .show();
        }
    }
}
