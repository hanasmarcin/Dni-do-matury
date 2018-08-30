package hanas.dnidomatury;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by azem on 11/3/17.
 */

public class BootReceiver extends BroadcastReceiver
{

    public void onReceive(Context context, Intent intent)
    {

        //startAlarm(context);

        // Your code to execute when Boot Completd

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, MyService.class));
            Toast.makeText(context, "doing oreo shiet", Toast.LENGTH_SHORT).show();
        }
        else {*/
            context.startService(new Intent(context, MyService.class));
        //}

        //Toast.makeText(context, "Booting Completed", Toast.LENGTH_LONG).show();

    }

   /* private void startAlarm(Context context) {
        Toast.makeText(context, "restart", Toast.LENGTH_SHORT).show();
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        //THIS IS WHERE YOU SET NOTIFICATION TIME FOR CASES WHEN THE NOTIFICATION NEEDS TO BE RESCHEDULED
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,0);

        myIntent = new Intent(context, AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context,0,myIntent,0);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES/30 ,pendingIntent);
    }*/

}