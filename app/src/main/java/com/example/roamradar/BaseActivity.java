package com.example.roamradar;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class BaseActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    protected BottomNavigationView bottomNavigationView;
    private Fragment homeFragment;
    private Fragment profileFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base); // Use the base layout

        // Load child activity's layout into the container
        getLayoutInflater().inflate(getLayoutResourceId(), findViewById(R.id.container));

        // Check user authentication
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Redirect to login if not authenticated
            startActivity(new Intent(BaseActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Initialize the fragments
        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        fragmentManager = getSupportFragmentManager();

        // Set the default fragment
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                showFragment(homeFragment);
                return true;

            case R.id.profile:
                showFragment(profileFragment);
                return true;

            case R.id.logout:
                // Handle logout logic
                mAuth.signOut();
                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return false;
        }
    }

    private void showFragment(Fragment fragmentToShow) {
        // Hide all fragments
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null) {
                fragmentManager.beginTransaction().hide(fragment).commit();
            }
        }

        // Show the selected fragment
        if (fragmentToShow != null) {
            fragmentManager.beginTransaction()
                    .show(fragmentToShow)
                    .commit();
        }
    }

    // Abstract method for child activities to define their layout
    protected abstract int getLayoutResourceId();
}
