package hanas.dnidomatury;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import hanas.dnidomatury.matura.ListOfMatura;

public class SelectActivity extends AppCompatActivity {

    private ListOfMatura listOfMatura = new ListOfMatura();
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_select);
        setSupportActionBar(toolbar);

        ListOfMatura.readFromFile(this);

        RecyclerView recyclerView = findViewById(R.id.full_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FullListMaturaAdapter adapter = new FullListMaturaAdapter(this, listOfMatura.getListOfMatura());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        listOfMatura.saveToFile(this);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_confirm) {
            Intent intent = new Intent();
            listOfMatura.saveToFile(this);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        else if (id == R.id.action_add) {
            Intent intent = new Intent(this, AddMaturaActivity.class);
            //intent.putExtra("selectedMaturaID", selectedMaturaID);
            startActivityForResult(intent, getResources().getInteger(R.integer.request_code_new_task));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
