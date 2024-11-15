package com.example.roamradar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class BaseActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    protected BottomNavigationView bottomNavigationView;

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
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation
                if (item.getItemId() == R.id.home) {
                    startActivity(new Intent(BaseActivity.this, HomeActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.logout) {
                    mAuth.signOut();
                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    // Abstract method for child activities to define their layout
    protected abstract int getLayoutResourceId();
}
