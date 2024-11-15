package com.example.roamradar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationHelper {

    public static void sendNotification(Context context, double latitude, double longitude) {
        // Create notification channel if Android version >= Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Location Channel";
            String description = "Channel for location notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("location_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create the notification
        Notification notification = new Notification.Builder(context, "location_channel")
                .setContentTitle("Location Alert")
                .setContentText("New location: " + latitude + ", " + longitude)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .build();

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}