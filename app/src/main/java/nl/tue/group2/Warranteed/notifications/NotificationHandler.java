package nl.tue.group2.Warranteed.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import nl.tue.group2.Warranteed.R;

public class NotificationHandler extends AppCompatActivity {

    private final String channelID;
    private final String channelName;
    private final String channelDescription;
    private Context mContext;
    private final NotificationManagerCompat notificationManager;

    // temporary solution, probably overwrites old notifications when restarting the app.
    private int notifID = 0;

    /**
     * Notification handler to display notifications to the user.
     *
     * @param channelID          The ID of the channel on which these notifications should be
     *                           displayed, should be unique for this app.
     * @param channelName        The name of the channel, visible to the user. Max length 300
     *                           chars (not robust).
     * @param channelDescription The description of the channel, visible to the user. Max length
     *                           300 chars (not robust).
     * @param mContext           The context in which this notification handler should exist
     *                           (should be same context as MainActivity).
     */
    public NotificationHandler(String channelID, String channelName, String channelDescription,
                               Context mContext) {
        this.channelID = channelID;
        this.channelName = channelName;
        this.channelDescription = channelDescription;
        this.mContext = mContext;
        createNotificationChannel();
        this.notificationManager = NotificationManagerCompat.from(this.mContext);
    }

    /**
     * Starts the notification channel for the app
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ (Android 8.0+) because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID, channelName,
                    importance);
            channel.setDescription(this.channelDescription);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =
                    this.mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Returns the notification ID of the previous sent notifcation that did not have an ID
     * specified.
     *
     * @return The last used unspecified notification ID.
     */
    public int getLastNotificationID() {
        return this.notifID;
    }

    /**
     * Creates a notification on the device running the app with the specified title and message.
     * If no notification ID is specified then an incrementing notification ID is used.
     *
     * @param notificationTitle   The title of the notification
     * @param notificationMessage The message contents of the notification
     */
    public void sendNotification(String notificationTitle, String notificationMessage) {
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.mContext,
                this.channelID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        // Send the notification
        this.notificationManager.notify(++notifID, builder.build());
    }

    /**
     * Creates a notification on the device running the app with the specified title and message.
     *
     * @param notificationTitle   The title of the notification
     * @param notificationMessage The message contents of the notification
     * @param notifID             The ID of the notification, if the same ID is used twice then
     *                            the previously sent notification gets overwritten.
     */
    public void sendNotification(String notificationTitle, String notificationMessage,
                                 int notifID) {
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.mContext,
                this.channelID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        // Send the notification with the specified ID
        this.notificationManager.notify(notifID, builder.build());
    }
}
