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
    private int notifID = 0; // temporary solution, probably overwrites old notifications when
                             // restarting the app.

    /**
     * Notification handler to display notifications to the user.
     *
     * @param channelID The ID of the channel on which these notifications should be displayed
     * @param channelName The name of the channel
     * @param channelDescription
     * @param mContext
     */
    public NotificationHandler(String channelID, String channelName, String channelDescription, Context mContext) {
        this.channelID = channelID;
        this.channelName = channelName;
        this.channelDescription = channelDescription;
        this.mContext = mContext;
        createNotificationChannel();
    }

    /**
     * Starts the notification channel for the app
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID, channelName, importance);
            channel.setDescription(this.channelDescription);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = this.mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Creates a notification on the device running the app with the specified title and message.
     *
     * @param notificationTitle The title of the notification
     * @param notificationMessage The message contents of the notification
     */
    public void sendNotification(String notificationTitle, String notificationMessage) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.mContext, this.channelID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage);
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.mContext);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notifID++, builder.build());

    }

}
