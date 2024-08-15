package com.example.asian;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private LinearLayout mLinearTopMenu;
    private Menu mMenu;
    private View mNavMenu;
    private NavController mNavController;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initControlNav();
        action();
        handleClickMenu();
        setupNavigationViewListener();

    }

    private void initView() {
        mToolBar = findViewById(R.id.tbApp);
        mNavMenu = findViewById(R.id.menuNav);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mNavigationView = findViewById(R.id.navigationView);
        mLinearTopMenu = mNavigationView.getHeaderView(0).findViewById(R.id.llTopMenu);
        mFloatingActionButton = findViewById(R.id.fabAction);

    }

    private void handleClickMenu() {
        mToolBar.setOnClickListener(view -> {
            mToolBar.setBackgroundColor(getResources().getColor(R.color.colorLightBlue));
            removeActionItem();
            addActionItem(R.id.action_search, R.drawable.ic_search);
            addActionItem(R.id.action_favorite, R.drawable.ic_love);
            mToolBar.setTitle(getString(R.string.title));
        });

        mFloatingActionButton.setOnClickListener(view -> Snackbar.make(view,
                getString(R.string.replace_your_own),
                Snackbar.LENGTH_SHORT).show());
    }

    private void initControlNav() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        assert navHostFragment != null;
        mNavController = navHostFragment.getNavController();
    }

    private void action() {
        mToolBar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mToolBar.setNavigationOnClickListener(view -> mDrawerLayout.openDrawer(GravityCompat.START));
        NavigationUI.setupWithNavController(mNavigationView, mNavController);
    }

    private void setupNavigationViewListener() {
        mNavigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeFragment) {
                mNavController.navigate(R.id.homeFragment);
                Snackbar.make(mNavigationView, getString(R.string.replace_your_own), Snackbar.LENGTH_SHORT).show();
            } else if (id == R.id.settingsFragment) {
                mNavController.navigate(R.id.settingsFragment);
                Snackbar.make(mNavigationView, getString(R.string.replace_your_own), Snackbar.LENGTH_SHORT).show();
            } else if (id == R.id.sendFragment) {
                mNavController.navigate(R.id.sendFragment);
                Snackbar.make(mNavigationView, getString(R.string.replace_your_own), Snackbar.LENGTH_SHORT).show();
            } else if (id == R.id.shareFragment) {
                mNavController.navigate(R.id.shareFragment);
                Snackbar.make(mNavigationView, getString(R.string.replace_your_own), Snackbar.LENGTH_SHORT).show();
            }
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        this.mMenu = menu;
        removeActionItem();
        return true;
    }

    private void removeActionItem() {
        mMenu.removeItem(R.id.action_search);
        mMenu.removeItem(R.id.action_favorite);
    }

    private void addActionItem(int id, int drawable) {
        MenuItem searchItem = mMenu.add(Menu.NONE, id, Menu.NONE, getString(R.string.search));
        searchItem.setIcon(drawable);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }
}
