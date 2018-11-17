package hanas.dnidomatury.settingsActivity.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import hanas.dnidomatury.maturaListActivity.MaturaListActivity;
import hanas.dnidomatury.matura.ListOfMatura;
import hanas.dnidomatury.matura.MaturaTimer;
import hanas.dnidomatury.settingsActivity.SettingsActivity;
import hanas.dnidomatury.settingsActivity.SettingsData;

@RequiresApi (api = Build.VERSION_CODES.LOLLIPOP)
public class NotifJobService extends JobService {


    public static JobScheduler jobScheduler = null;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        Intent myIntent = new Intent(this, MaturaListActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this,0, myIntent, FLAG_ONE_SHOT);
        SettingsData data = new SettingsData();

        jobScheduler =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(new JobInfo.Builder(123,
                new ComponentName(this, NotifJobService.class))
                .setMinimumLatency(SettingsActivity.minimumLatency(data.notifFrequency))
                .setPersisted(true)
                .build());

        inboxStyleNotification();

        jobFinished(jobParameters, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    //private int SUMMARY_ID = 0;
    //private String GROUP_KEY_MATURA_NOTIF = "hanas.dnidomatury.matura.notif";

    private void inboxStyleNotification() {
        int NOTIFICATION_ID = 1;
        ListOfMatura listOfMatura = new ListOfMatura();
        ListOfMatura.readFromFile(this);
        Notification.InboxStyle inboxNotifStyle = new Notification.InboxStyle();
        long daysInMillisToFirstMatura = new MaturaTimer().getMillisDiff(Calendar.getInstance(), listOfMatura.getListOfMatura().get(0).getDate());
        long daysToFirstMatura = TimeUnit.MILLISECONDS.toDays(daysInMillisToFirstMatura);
        for (int i=0; i < listOfMatura.getListOfMatura().size(); i++){
            if(!listOfMatura.getListOfMatura().get(i).isSelected()) continue;
            long daysInMillis = new MaturaTimer().getMillisDiff(Calendar.getInstance(), listOfMatura.getListOfMatura().get(i).getDate());
            long days = TimeUnit.MILLISECONDS.toDays(daysInMillis);
            inboxNotifStyle.addLine(listOfMatura.getListOfMatura().get(i).getName()+" "+listOfMatura.getListOfMatura().get(i).getLevel() + " - " + days + " dni");
        }

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, new NotificationHelper(this).getDnidomaturyNotification(inboxNotifStyle)
                .setContentTitle("Pierwsza matura już za "+daysToFirstMatura+" dni")
                .build());

    }
}