package nl.tue.group2.Warranteed.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.util.Random;

import nl.tue.group2.Warranteed.R;

public class NotificationHandler extends AppCompatActivity {

    private final String channelID;
    private final String channelName;
    private final String channelDescription;
    private Context mContext;

    public NotificationHandler(String channelID, String channelName, String channelDescription, Context mContext) {
        this.channelID = channelID;
        this.channelName = channelName;
        this.channelDescription = channelDescription;
        this.mContext = mContext;
        createNotificationChannel();
    }

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

    public void sendNotification(View view) {
        Random r = new Random();
        int notifID = r.nextInt();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.mContext, this.channelID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Some notifc" + notifID)
                .setContentText("Hello World!");
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.mContext);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(notifID, builder.build());

    }

}
