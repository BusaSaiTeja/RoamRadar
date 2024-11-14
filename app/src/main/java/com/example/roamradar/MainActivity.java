package com.example.roamradar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is logged in
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // If user is not logged in, redirect to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Finish the current activity so the user can't come back to it
        } else {
            // User is logged in, show the main content
            setContentView(R.layout.activity_main);


            // Set up the Bottom Navigation View
            bottomNavigationView = findViewById(R.id.bottomNavigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // Handle navigation item selection using if-else
                    if (item.getItemId() == R.id.home) {
                        // Go to Home (current activity)
                        return true;
                    } else if (item.getItemId() == R.id.profile) {
                        // Navigate to Profile Activity
                        Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(profileIntent);
                        return true;
                    } else if (item.getItemId() == R.id.logout) {
                        // Log out and go back to LoginActivity
                        mAuth.signOut();
                        Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(logoutIntent);
                        finish();  // Finish the current activity
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}
