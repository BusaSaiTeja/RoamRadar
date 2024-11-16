package com.example.roamradar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class BaseActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    protected BottomNavigationView bottomNavigationView;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

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

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("NavBar", "Selected item: " + item.getItemId());

                if (item.getItemId() == R.id.home) {
                    // Avoid opening a new HomeActivity if already on it
                    if (!(BaseActivity.this instanceof HomeActivity)) {
                        startActivity(new Intent(BaseActivity.this, HomeActivity.class));
                    }
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    // Avoid opening a new ProfileActivity if already on it
                    if (!(BaseActivity.this instanceof ProfileActivity)) {
                        startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
                    }
                    return true;
                } else if (item.getItemId() == R.id.logout) {
                    mAuth.signOut();
                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });

        // Request location permission when BaseActivity is created
        requestLocationPermission();
    }

    // Override this method in your child activity to specify the layout resource
    protected abstract int getLayoutResourceId();

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure the BottomNavigationView shows the correct selected item when coming back to the activity
        if (this instanceof HomeActivity) {
            bottomNavigationView.setSelectedItemId(R.id.home);
        } else if (this instanceof ProfileActivity) {
            bottomNavigationView.setSelectedItemId(R.id.profile);
        }
    }

    // Request location permission
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // If permission is granted, start the location service
            startLocationService();
        } else {
            // If permission is not granted, request the permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // Start the location service
    private void startLocationService() {
        // Start the LocationService once permission is granted
        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start the location service
                startLocationService();
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission denied. Cannot fetch location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
