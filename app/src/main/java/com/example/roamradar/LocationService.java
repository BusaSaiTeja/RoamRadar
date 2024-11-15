package com.example.roamradar;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class LocationService extends Service {

    private static final double LOCATION_THRESHOLD = 100.0;  // 100 meters
    private static final String TAG = "LocationService";
    private String userId;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Get the current user ID once (ensure user is logged in)
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            Log.e(TAG, "No user is logged in.");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Check if permission is granted before trying to get location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation(); // Proceed to get location if permission is granted
        } else {
            Log.e(TAG, "Location permission not granted.");
        }
        return START_STICKY;
    }

    private void getLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Fetch user data from Firestore and process it
                            FirebaseFirestore.getInstance().collection("users")
                                    .document(userId)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                GeoPoint userLastLocation = documentSnapshot.getGeoPoint("location");
                                                if (userLastLocation != null) {
                                                    // Compare the fetched location with the new location
                                                    if (isNewLocation(17.385044, 78.486671, userLastLocation)) {
                                                        // Fetch notification settings from Firestore
                                                        boolean sendNotifications = documentSnapshot.getBoolean("sendNotifications");
                                                        Log.d(TAG, "New location detected, should send notification");
                                                        if (sendNotifications) {
                                                            sendNotification(location.getLatitude(), location.getLongitude());
                                                        }
                                                    } else{
                                                        Log.d(TAG,"Location hasn't changed, skipping notification") ;
                                                    }
                                                } else {
                                                    Log.e(TAG, "No location data in Firestore for the user");
                                                }
                                            } else {
                                                Log.e(TAG, "User document not found");
                                            }
                                        }
                                    })
                                    .addOnFailureListener(e -> Log.e(TAG, "Error fetching user data", e));

                            // Update Firestore with the new location
                            FirebaseFirestore.getInstance().collection("users")
                                    .document(userId)
                                    .update("location", new GeoPoint(location.getLatitude(), location.getLongitude()))
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Location updated"))
                                    .addOnFailureListener(e -> Log.e(TAG, "Error updating location", e));
                        } else {
                            Log.e(TAG, "Failed to get location");
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to get last location", e));

    }

    private boolean isNewLocation(double latitude, double longitude, GeoPoint userLastLocation) {
        // Create Location objects to use Android's built-in distance calculation
        Location currentLocation = new Location("currentLocation");
        currentLocation.setLatitude(latitude);
        currentLocation.setLongitude(longitude);

        Location storedLocation = new Location("storedLocation");
        storedLocation.setLatitude(userLastLocation.getLatitude());
        storedLocation.setLongitude(userLastLocation.getLongitude());

        // Get the distance between the two locations
        float distance = currentLocation.distanceTo(storedLocation);

        // Check if the distance exceeds the threshold
        return distance > LOCATION_THRESHOLD;  // If distance > threshold, consider the location changed
    }



    private void sendNotification(double latitude, double longitude) {
        Log.d(TAG, "Sending notification with location: " + latitude + ", " + longitude);
        NotificationHelper.sendNotification(this, latitude, longitude);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
