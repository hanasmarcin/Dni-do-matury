package hanas.dnidomatury;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showNotification(this);
    }

        public void showNotification(Context context) {


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


            notificationManager.notify(1,notificationHelper.getDnidomaturyNotification(pendingIntent).build());
        }
}
