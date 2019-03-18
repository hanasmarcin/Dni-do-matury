package hanas.dnidomatury.settingsActivity.notification;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import hanas.dnidomatury.R;
import hanas.dnidomatury.examListActivity.ExamListActivity;

class NotificationHelper extends ContextWrapper {

    private static final String NOTIF_CHANNEL_ID = "hanas.dnidomatury.CHANNEL";
    private static final String NOTIF_CHANNEL_NAME = "powiadomienia aplikacji \"Dni do matury\"";
    private NotificationManager manager;


    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannels();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {

        NotificationChannel dnidomaturyChannel = new NotificationChannel(NOTIF_CHANNEL_ID, NOTIF_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        dnidomaturyChannel.enableLights(true);
        getManager().createNotificationChannel(dnidomaturyChannel);
    }

    private NotificationManager getManager() {
        if (manager == null)
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }

    public Notification.Builder getDnidomaturyNotification(Notification.InboxStyle inboxNotifStyle) {

        // Get the layouts to use in the custom notification
        //RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification_large);
        Intent notifIntent = new Intent(this, ExamListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifIntent, 0);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), NOTIF_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(inboxNotifStyle)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_notif);
        } else {
            return new Notification.Builder(getApplicationContext())
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setStyle(inboxNotifStyle)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_notif)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentInfo("Info");
        }
    }
}
