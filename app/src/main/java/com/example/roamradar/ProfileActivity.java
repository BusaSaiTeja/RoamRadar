package com.example.roamradar;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ensure the action bar is visible
        if (getSupportActionBar() != null) {
            getSupportActionBar().show(); // Show the action bar explicitly
        }

        // Initialize the notification switch
        Switch notificationSwitch = findViewById(R.id.notificationSwitch);

        // Get the current user ID from Firebase Authentication
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        // Firestore reference
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Fetch the current notification settings for the user
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                boolean notificationsEnabled = Boolean.TRUE.equals(documentSnapshot.getBoolean("sendNotifications"));
                notificationSwitch.setChecked(notificationsEnabled); // Set the switch state
            }
        });

        // Set up listener for the switch
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update Firestore with the new notification setting
            db.collection("users").document(userId)
                    .update("sendNotifications", isChecked);
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_profile; // Provide the layout for this activity
    }
}
