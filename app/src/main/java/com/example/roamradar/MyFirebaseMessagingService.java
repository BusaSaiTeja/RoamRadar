package com.example.roamradar;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM messages here
        // Show notification based on message content
        // Assuming the message contains latitude and longitude
        double latitude = Double.parseDouble(remoteMessage.getData().get("latitude"));
        double longitude = Double.parseDouble(remoteMessage.getData().get("longitude"));

        NotificationHelper.sendNotification(this, latitude, longitude);
    }
}
