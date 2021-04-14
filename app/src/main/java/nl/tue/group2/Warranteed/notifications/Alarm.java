package nl.tue.group2.Warranteed.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //make notification
        NotificationManager.getNotificationHandler().sendNotification("Warranteed", "There is a receipt expiring soon");
    }
}
