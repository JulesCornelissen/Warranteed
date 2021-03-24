package nl.tue.group2.Warranteed.notifications;

public class NotificationManager {

    private static NotificationHandler NH;

    public static void setNotificationHandler(NotificationHandler nh) {
        NH = nh;
    }

    public static NotificationHandler getNotificationHandler() {
        return NH;
    }

}
