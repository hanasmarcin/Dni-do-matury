package hanas.dnidomatury.settingsActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import hanas.dnidomatury.R;
import hanas.dnidomatury.settingsActivity.customPreferences.NumberPickerPreference;
import hanas.dnidomatury.settingsActivity.customPreferences.TimePreference;
import hanas.dnidomatury.settingsActivity.notification.NotifJobService;

import static hanas.dnidomatury.settingsActivity.SettingsFragment.Frequency.MONTHLY;
import static hanas.dnidomatury.settingsActivity.SettingsFragment.Frequency.WEEKLY;

public class SettingsFragment extends PreferenceFragment {

    private SwitchPreference notifPreference;
    private ListPreference frequencyPreference;
    private MultiSelectListPreference daysOfWeekPreference;
    private NumberPickerPreference dayOfMonthPreference;
    private TimePreference timePreference;

    private final DateFormat hourFormat = new SimpleDateFormat("H:mm", Locale.getDefault());
    private final DateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

    private String[] getDaysOfWeek() {
        String[] daysOfWeek = new String[7];

        // Get array with names of days of week
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        for (int i = 0; i < 7; i++) {
            daysOfWeek[i] = dayOfWeekFormat.format(cal.getTime());
            cal.add(Calendar.DAY_OF_WEEK, 1);
        }
        return daysOfWeek;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Add preferences from resources to variables
        addPreferencesFromResource(R.xml.notif_preference);
        notifPreference = (SwitchPreference) findPreference("notif_preference");
        frequencyPreference = (ListPreference) findPreference("frequency_preference");
        daysOfWeekPreference = (MultiSelectListPreference) findPreference("notif_days_of_week");
        dayOfMonthPreference = (NumberPickerPreference) findPreference("day_of_month");
        timePreference = (TimePreference) findPreference("notif_hour");
        String[] daysOfWeek = getDaysOfWeek();

        // Set values for choosing days of week
        daysOfWeekPreference.setEntryValues(daysOfWeek);
        daysOfWeekPreference.setEntries(daysOfWeek);

        notifPreference.setOnPreferenceChangeListener((preference, enabled) -> {
            // If the preference is changed, enable/disable other preferences
            frequencyPreference.setEnabled((boolean) enabled);
            daysOfWeekPreference.setEnabled((boolean) enabled && frequencyPreference.getValue().equals(WEEKLY.getKey()));
            dayOfMonthPreference.setEnabled((boolean) enabled && frequencyPreference.getValue().equals(MONTHLY.getKey()));
            timePreference.setEnabled((boolean) enabled);
            if ((boolean) enabled) {
                if (NotifJobService.jobScheduler != null)
                    NotifJobService.jobScheduler.cancelAll();
                firstNotif((boolean) enabled, frequencyPreference.getValue(), timePreference.getValue(), daysOfWeekPreference.getValues(), dayOfMonthPreference.getValue());
            }
            return true;
        });

        frequencyPreference.setOnPreferenceChangeListener((preference, frequency) -> {

            daysOfWeekPreference.setEnabled(frequency.equals(WEEKLY.getKey()));
            dayOfMonthPreference.setEnabled(frequency.equals(MONTHLY.getKey()));
            frequencyPreference.setSummary(frequency.toString());

            if (NotifJobService.jobScheduler != null)
                NotifJobService.jobScheduler.cancelAll();
            firstNotif(true, (String) frequency, timePreference.getValue(), daysOfWeekPreference.getValues(), dayOfMonthPreference.getValue());

            return true;
        });

        daysOfWeekPreference.setOnPreferenceChangeListener((preference, daysOfWeek1) -> daysOfWeekChange(daysOfWeek1));

        dayOfMonthPreference.setOnPreferenceChangeListener((preference, dayOfMonth) -> {
            dayOfMonthPreference.setSummary(dayOfMonth.toString() + ". dzień każdego miesiąca");
            if (NotifJobService.jobScheduler != null)
                NotifJobService.jobScheduler.cancelAll();
            firstNotif(true, frequencyPreference.getValue(), timePreference.getValue(), daysOfWeekPreference.getValues(), (int) dayOfMonth);
            return true;
        });

        timePreference.setOnPreferenceChangeListener((preference, time) -> {
            timePreference.setSummary(time.toString());

            if (NotifJobService.jobScheduler != null)
                NotifJobService.jobScheduler.cancelAll();

            firstNotif(true, frequencyPreference.getValue(), (String) time, daysOfWeekPreference.getValues(), dayOfMonthPreference.getValue());
            return true;
        });
    }

    @SuppressWarnings("unchecked")
    private boolean daysOfWeekChange(Object daysOfWeek1) {
        String daysOfWeekSummary = daysOfWeek1.toString().substring(1, daysOfWeek1.toString().length() - 1);
        daysOfWeekPreference.setSummary(daysOfWeekSummary);
        if (!((Set<String>) daysOfWeek1).isEmpty()) {
            if (NotifJobService.jobScheduler != null)
                NotifJobService.jobScheduler.cancelAll();

            firstNotif(true, frequencyPreference.getValue(), timePreference.getValue(), (Set<String>) daysOfWeek1, dayOfMonthPreference.getValue());
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean enabled = notifPreference.isChecked();
        frequencyPreference.setEnabled(enabled);
        frequencyPreference.setSummary(frequencyPreference.getValue());
        daysOfWeekPreference.setEnabled(enabled && frequencyPreference.getValue().equals(WEEKLY.getKey()));
        String daysOfWeekSummary = daysOfWeekPreference.getValues().toString().substring(1, daysOfWeekPreference.getValues().toString().length() - 1);
        daysOfWeekPreference.setSummary(daysOfWeekSummary);
        dayOfMonthPreference.setEnabled(enabled && frequencyPreference.getValue().equals(MONTHLY.getKey()));
        dayOfMonthPreference.setSummary(dayOfMonthPreference.getValue() + ". dzień każdego miesiąca");
        timePreference.setEnabled(enabled);
        timePreference.setSummary(timePreference.getValue());
    }

    public enum Frequency {
        NEVER("Nigdy"), DAILY("Codziennie"), WEEKLY("W wybrane dni tygodnia"), MONTHLY("Raz w miesiącu");
        final String key;

        Frequency(String key) {
            this.key = key;
        }

        public static Frequency getFrequency(String key) {
            switch (key) {
                case "Codziennie":
                    return DAILY;
                case "W wybrane dni tygodnia":
                    return WEEKLY;
                case "Raz w miesiącu":
                    return MONTHLY;
                default:
                    return null;
            }
        }

        String getKey() {
            return this.key;
        }
    }

    private void firstNotif(boolean isNotif, String notifFrequency, String hour, Set<String> daysOfWeek, int dayOfMonth) {

        Calendar notifDate = Calendar.getInstance();

        if (isNotif) {
            if (notifFrequency.equals("W wybrane dni tygodnia") && daysOfWeek.isEmpty()) return;
            try {
                Date notifHour = hourFormat.parse(hour);
                notifDate.set(Calendar.HOUR_OF_DAY, notifHour.getHours());
                notifDate.set(Calendar.MINUTE, notifHour.getMinutes());
                notifDate.set(Calendar.SECOND, 0);


                Frequency frequency = Frequency.getFrequency(notifFrequency);
                if (frequency != null)
                    switch (frequency) {
                        case DAILY: {
                            if (notifDate.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
                                notifDate.add(Calendar.DAY_OF_MONTH, 1);
                            break;
                        }
                        case WEEKLY: {
                            if (notifDate.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
                                notifDate.add(Calendar.DAY_OF_MONTH, 1);
                            while (!daysOfWeek.contains(dayOfWeekFormat.format(notifDate.getTime())))
                                notifDate.add(Calendar.DAY_OF_MONTH, 1);
                            break;
                        }
                        case MONTHLY: {
                            notifDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            if (notifDate.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
                                notifDate.add(Calendar.MONTH, 1);

                            break;
                        }
                    }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        long minimumLatency = notifDate.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();

        JobScheduler jobScheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null)
            jobScheduler.schedule(new JobInfo.Builder(123,
                    new ComponentName(getActivity(), NotifJobService.class))
                    .setMinimumLatency(minimumLatency)
                    .setPersisted(true)
                    .build());

    }


}
