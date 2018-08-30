package hanas.dnidomatury;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

public class NotificationHelper extends ContextWrapper {

    private static final String NOTIF_CHANNEL_ID = "hanas.dnidomatury.CHANNEL";
    private static final String NOTIF_CHANNEL_NAME = "dnidomatury CHANNEL";
    private NotificationManager manager;
    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel dnidomaturyChannel = new NotificationChannel(NOTIF_CHANNEL_ID, NOTIF_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            dnidomaturyChannel.enableLights(true);

            getManager().createNotificationChannel(dnidomaturyChannel);
        }
    }

    public NotificationManager getManager() {

        if (manager==null) manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }

    public Notification.Builder getDnidomaturyNotification(PendingIntent pendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), NOTIF_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Zodiac")
                    .setContentIntent(pendingIntent)
                    .setContentText("Check out your horoscope");
        }
        else {
            return new Notification.Builder(getApplicationContext())
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Zodiac")
                    .setContentIntent(pendingIntent)
                    .setContentText("Check out your horoscope")
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentInfo("Info");
        }
    }

}
