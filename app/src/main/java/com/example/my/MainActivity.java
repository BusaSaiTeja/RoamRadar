package com.example.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up DrawerLayout and ActionBarDrawerToggle
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up NavigationView for sidebar and its listener using lambda (alias)
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_order_transactions) {
                startActivity(new Intent(this, OrderTransactionsActivity.class));
            } else if (itemId == R.id.nav_rewards) {
                startActivity(new Intent(this, RewardsActivity.class));
            } else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
            } else if (itemId == R.id.nav_sign_out) {
                finish(); // Or implement sign-out logic if required
            }

            drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer after an item is selected
            return true;
        });

        // Set up BottomNavigationView and its listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Code for Home action
                return true;
            } else if (itemId == R.id.nav_orders) {
                startActivity(new Intent(this, OrderTransactionsActivity.class));
                return true;
            } else if (itemId == R.id.nav_bookmarks) {
                startActivity(new Intent(this, BookmarksActivity.class));
                return true;
            } else if (itemId == R.id.nav_helpbot) {
                startActivity(new Intent(this, HelpBotActivity.class));
                return true;
            }
            return false;
        });

        // Handle back button press with OnBackPressedDispatcher to close drawer if it's open
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish(); // Close the activity if the drawer is not open
                }
            }
        });

        // Set up touch listener on the main content area to close the drawer when clicked outside
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); // Unlock the drawer to allow swipe gestures
        drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent)); // Make background transparent when drawer is open

        // Set touch listener on the main content area to detect clicks outside the drawer
        View mainContent = findViewById(R.id.main_content);  // Assuming main_content is the id for your layout that represents the content area
        mainContent.setOnTouchListener((v, event) -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer if it's open
                return true;  // Return true to indicate the touch was handled
            }
            return false;  // Return false to allow other touch events to be handled
        });
    }
}
