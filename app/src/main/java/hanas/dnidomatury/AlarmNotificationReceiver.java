package hanas.dnidomatury;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import hanas.dnidomatury.MainActivity;
import hanas.dnidomatury.R;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        NotificationHelper notificationHelper;
        notificationHelper = new NotificationHelper(context);
        //NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Intent myIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                myIntent,
                FLAG_ONE_SHOT );

        /*builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Zodiac")
                .setContentIntent(pendingIntent)
                .setContentText("Check out your horoscope")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");*/

        Toast.makeText(context, "po minutce", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForeground(1, notificationHelper.getDnidomaturyNotification(pendingIntent).build());

        notificationManager.notify(1332,notificationHelper.getDnidomaturyNotification(pendingIntent).build());
    }
}
