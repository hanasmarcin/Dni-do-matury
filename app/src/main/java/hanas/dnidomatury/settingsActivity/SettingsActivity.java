package hanas.dnidomatury.settingsActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import hanas.dnidomatury.settingsActivity.notification.NotifJobService;
import hanas.dnidomatury.R;


public class SettingsActivity extends AppCompatActivity {


    //private Calendar firstNotifDate = Calendar.getInstance();
    //public static Frequency notifFrequency = Frequency.NEVER;

    private SettingsData data;

    private void checkButton(Frequency frequency, RadioGroup radioGroup) {
        switch(frequency) {
            case DAILY:
                radioGroup.check(R.id.radioButton);
                break;
            case WEEKLY:
                radioGroup.check(R.id.radioButton2);
                break;
            case MONTHLY:
                radioGroup.check(R.id.radioButton3);
                break;
            default : radioGroup.check(R.id.radioButton4);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);

        final CardView notifFirstDate = findViewById(R.id.settings_first_notif_card);
        final CardView notifChangeHour = findViewById(R.id.settings_hour_card);
        final RadioGroup radioGroup = findViewById(R.id.radio_group);
        data = SettingsData.readFromFile(this);
        checkButton(data.notifFrequency, radioGroup);

        if ( Calendar.getInstance().compareTo(data.firstNotifDate) > 0 ) {
            data.firstNotifDate.setTimeInMillis(data.firstNotifDate.getTimeInMillis() + 86400000);
        }


        notifChangeHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mHourOfDay = data.firstNotifDate.get(Calendar.HOUR_OF_DAY);
                int mMinute = data.firstNotifDate.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        data.firstNotifDate.set(Calendar.HOUR_OF_DAY, i);
                        data.firstNotifDate.set(Calendar.MINUTE, i1);
                        if ( Calendar.getInstance().compareTo(data.firstNotifDate) > 0 ) {
                            data.firstNotifDate.setTimeInMillis(data.firstNotifDate.getTimeInMillis() + 86400000);
                        }
                    }
                }, mHourOfDay, mMinute, true);

                timePickerDialog.show();
            }

        });

        notifFirstDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //final Calendar c = Calendar.getInstance();
                int mYear = data.firstNotifDate.get(Calendar.YEAR);
                int mMonth = data.firstNotifDate.get(Calendar.MONTH);
                int mDay = data.firstNotifDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(SettingsActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        data.firstNotifDate.set(year, monthOfYear, dayOfMonth);
                        if(data.firstNotifDate.compareTo(Calendar.getInstance()) < 0) {
                            while (data.firstNotifDate.compareTo(Calendar.getInstance()) < 0) {
                                data.firstNotifDate.setTimeInMillis(data.firstNotifDate.getTimeInMillis() + 86400000);
                            }
                            String info = "Pierwsze powiadomienie musi się odbyć w przyszłości, ustawiono dzień na " + data.firstNotifDate.get(Calendar.DAY_OF_MONTH) +"."+data.firstNotifDate.get(Calendar.MONTH)+"."+data.firstNotifDate.get(Calendar.YEAR);
                            Toast.makeText(SettingsActivity.this, info, Toast.LENGTH_SHORT).show();
                        }

                        //taskDate.setText(monthOfYear < 9 ? dayOfMonth + "." + "0" + (monthOfYear + 1) + "." + year : dayOfMonth + "." + (monthOfYear + 1) + "." + year);

                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.setTitle("Wybierz datę zadania");
                datePickerDialog.show();
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                // Is the button now checked?

                // Check which radio button was clicked
                switch(radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radioButton4:
                        data.notifFrequency = Frequency.NEVER;
                        Toast.makeText(SettingsActivity.this, "d", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButton:
                        data.notifFrequency = Frequency.DAILY;
                        Toast.makeText(SettingsActivity.this, "a", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButton2:
                        data.notifFrequency = Frequency.WEEKLY;
                        Toast.makeText(SettingsActivity.this, "b", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButton3:
                        data.notifFrequency = Frequency.MONTHLY;
                        Toast.makeText(SettingsActivity.this, "c", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


    }

    public enum Frequency {NEVER, DAILY, WEEKLY, MONTHLY};
    public static long minimumLatency(Frequency frequency) {
        switch(frequency) {
            case DAILY: return 300000;
            case WEEKLY: return 600000;
            case MONTHLY: return 1200000;
            default : return 15000;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void firstNotif(SettingsData data) {

        long minLatency = data.firstNotifDate.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();

        JobScheduler jobScheduler =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(new JobInfo.Builder(123,
                new ComponentName(this, NotifJobService.class))
                .setMinimumLatency(minLatency)
                .setPersisted(true)
                .build());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_confirm_s) {
            //Intent intent = new Intent();
            //listOfMatura.saveToFile(this);
            //setResult(RESULT_OK, intent);
            data.saveToFile(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (NotifJobService.jobScheduler != null) NotifJobService.jobScheduler.cancelAll();
                if (data.notifFrequency != Frequency.NEVER) firstNotif(data);
            }
            else Toast.makeText(SettingsActivity.this, "Powiadomienia są dostępne od Androida w wersji Lollipop (5.0)", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
