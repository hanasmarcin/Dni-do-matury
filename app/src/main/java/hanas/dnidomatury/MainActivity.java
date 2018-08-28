package hanas.dnidomatury;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ListOfMatura listOfMatura = new ListOfMatura();
    static int mainToSelectRequestCode=21;
    static public int mainToMaturaRequestCode=15;

    static public int getMainToSelectRequestCode() {
        return mainToSelectRequestCode;
    }

    static public int getMainToMaturaRequestCode() {
        return mainToMaturaRequestCode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        Calendar date=Calendar.getInstance();
        listOfMatura.readFromFile(this);
        //listOfMatura.saveToFile(this);
        listOfMatura.deleteNotSelected();

        RecyclerView recyclerView = findViewById(R.id.selected_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        SelectedMaturaAdapter adapter = new SelectedMaturaAdapter(this, listOfMatura.getListOfMatura());
        recyclerView.setAdapter(adapter);

        //zaczynamy powiadomienia

        startAlarm(true,true);

    }

    private void startAlarm(boolean isNotification, boolean isRepeat) {
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        // SET TIME HERE
        Calendar calendar= Calendar.getInstance();
        //calendar.set(Calendar.HOUR_OF_DAY,22);
        //calendar.set(Calendar.MINUTE,40);


        myIntent = new Intent(MainActivity.this, AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,myIntent,0);


        if(!isRepeat)
            manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(),pendingIntent);
        else
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES/30,pendingIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        listOfMatura.readFromFile(this);
        listOfMatura.deleteNotSelected();

        RecyclerView recyclerView = findViewById(R.id.selected_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        SelectedMaturaAdapter adapter = new SelectedMaturaAdapter(this, listOfMatura.getListOfMatura());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Intent intent = new Intent(this, SelectActivity.class);
            startActivityForResult(intent, mainToSelectRequestCode);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == mainToSelectRequestCode || requestCode == mainToMaturaRequestCode){
            if (resultCode == RESULT_OK){
                listOfMatura.readFromFile(this);
                listOfMatura.deleteNotSelected();

                RecyclerView recyclerView = findViewById(R.id.selected_recycle_view);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

                SelectedMaturaAdapter adapter = new SelectedMaturaAdapter(this, listOfMatura.getListOfMatura());
                recyclerView.setAdapter(adapter);

            }
        }
    }
}
