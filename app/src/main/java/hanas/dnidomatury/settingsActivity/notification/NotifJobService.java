package hanas.dnidomatury.settingsActivity.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import androidx.annotation.RequiresApi;
import androidx.collection.ArraySet;
import hanas.dnidomatury.model.exam.ExamTimer;
import hanas.dnidomatury.model.exam.ExamsList;
import hanas.dnidomatury.model.exam.SelectedExamsList;
import hanas.dnidomatury.settingsActivity.SettingsFragment.Frequency;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotifJobService extends JobService {


    public static JobScheduler jobScheduler = null;
    private final DateFormat hourFormat = new SimpleDateFormat("H:mm", Locale.getDefault());
    private final DateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNotif = preferences.getBoolean("notif_preference", false);
        Calendar notifDate = Calendar.getInstance();

        if (isNotif) {
            String notifFrequency = preferences.getString("frequency_preference", "Codziennie");
            String hour = preferences.getString("notif_hour", "8:00");

            try {
                Date notifHour = hourFormat.parse(hour);
                notifDate.set(Calendar.HOUR_OF_DAY, notifHour.getHours());
                notifDate.set(Calendar.MINUTE, notifHour.getMinutes());
                notifDate.add(Calendar.DAY_OF_MONTH, 1);


                Frequency frequency = Frequency.getFrequency(notifFrequency);
                if (frequency != null)
                    switch (frequency) {
                        case DAILY: {
                            if (notifDate.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
                                notifDate.add(Calendar.DAY_OF_MONTH, 1);
                            break;
                        }
                        case WEEKLY: {
                            Set<String> daysOfWeek = preferences.getStringSet("notif_days_of_week", new ArraySet<>());

                            if (notifDate.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
                                notifDate.add(Calendar.DAY_OF_MONTH, 1);
                            while (!daysOfWeek.contains(dayOfWeekFormat.format(notifDate.getTime())))
                                notifDate.add(Calendar.DAY_OF_MONTH, 1);
                            break;
                        }
                        case MONTHLY: {
                            int dayOfMonth = preferences.getInt("day_of_month", 1);

                            if (dayOfMonth == notifDate.get(Calendar.DAY_OF_MONTH))
                                notifDate.add(Calendar.MONTH, 1);
                            else {
                                notifDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                if (notifDate.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
                                    notifDate.add(Calendar.MONTH, 1);
                            }
                            break;
                        }
                    }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        long minimumLatency = notifDate.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();

        jobScheduler =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(new JobInfo.Builder(123,
                new ComponentName(this, NotifJobService.class))
                .setMinimumLatency(minimumLatency)
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


    private void inboxStyleNotification() {
        int NOTIFICATION_ID = 1;
        ExamsList listOfExam = SelectedExamsList.getInstance(this);
        Notification.InboxStyle inboxNotifStyle = new Notification.InboxStyle();
        for (int i = 0; i < listOfExam.size(); i++) {
            long daysInMillis = ExamTimer.getMillisDiff(Calendar.getInstance(), listOfExam.get(i).getDate());
            long days = TimeUnit.MILLISECONDS.toDays(daysInMillis);
            inboxNotifStyle.addLine(listOfExam.get(i).getName() + " " + listOfExam.get(i).getLevel() + " - " + days + " dni");
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null)
            notificationManager.notify(NOTIFICATION_ID, new NotificationHelper(this).getDnidomaturyNotification(inboxNotifStyle)
                    .setContentTitle("Pierwsza matura już za " + getDaysToFirstExam(listOfExam) + " dni")
                    .build());

    }

    private long getDaysToFirstExam(ExamsList listOfExam) {
        Long[] daysInMillisToExam = new Long[listOfExam.size()];
        Calendar currentCal = Calendar.getInstance();
        for (int i = 0; i < listOfExam.size(); i++) {
            daysInMillisToExam[i] = ExamTimer.getMillisDiff(currentCal, listOfExam.get(i).getDate());
        }


        long daysInMillisToFirstExam = Collections.min(Arrays.asList(daysInMillisToExam));
        return TimeUnit.MILLISECONDS.toDays(daysInMillisToFirstExam);
    }

}