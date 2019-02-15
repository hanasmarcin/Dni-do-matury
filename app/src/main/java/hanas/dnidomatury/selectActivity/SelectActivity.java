package hanas.dnidomatury.selectActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hanas.dnidomatury.R;
import hanas.dnidomatury.matura.ListOfMatura;
import hanas.dnidomatury.matura.Matura;
import hanas.dnidomatury.matura.task.ListOfTasks;
import hanas.dnidomatury.matura.task.Task;
import hanas.dnidomatury.maturaListActivity.MaturaAdapter;
import hanas.dnidomatury.touchHelper.SimpleItemTouchHelperCallback;

public class SelectActivity extends AppCompatActivity {

    private List<Matura> listOfMatura = new ArrayList<>();
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private ItemTouchHelper mItemTouchHelper;
    SelectMaturaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Toolbar toolbar = findViewById(R.id.toolbar_select);
        setSupportActionBar(toolbar);

        listOfMatura = ListOfMatura.readFromFile(this, true);

        RecyclerView recyclerView = findViewById(R.id.full_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SelectMaturaAdapter(this, listOfMatura);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    protected void onPause() {
        ListOfMatura.saveToFile(this, listOfMatura);
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
        if (id == R.id.action_add) {
            Intent intent = new Intent(this, AddMaturaActivity.class);
            //intent.putExtra("selectedMaturaID", selectedMaturaID);
            startActivityForResult(intent, 5320);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 5320){
            if (resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                if (bundle!=null) {
                    String maturaName = bundle.getString("maturaName");
                    String maturaType = bundle.getString("maturaType");
                    String maturaLevel = bundle.getString("maturaLevel");
                    String maturaDateText = bundle.getString("maturaDateText");
                    Matura newMatura = ListOfMatura.findMatura(maturaName, maturaLevel, maturaType, SelectActivity.this, false);
                    if(maturaType.contains("pisemn")) {
                        if (newMatura != null) {
                            ListOfTasks lot = new ListOfTasks(maturaName, maturaType, maturaLevel);
                            lot.readFromFile(this);
                            //newMatura.setTasksCounter(lot.sizeOfList());
                            newMatura.setTasksCounter(lot.getTasksCounter());
                            listOfMatura.add(newMatura);
                        }
                    }
                    else {

                        //Toast.makeText(this, maturaName+maturaType+maturaLevel+maturaDateText, Toast.LENGTH_SHORT).show();
                        try {
                            listOfMatura.add((new Matura(maturaName, maturaLevel, maturaType, maturaDateText, "Green", "GreenDark")));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();

                }
            }
        }
        /*else if (requestCode == 5636){
            if (resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                if (bundle!=null) {
                    String taskName = bundle.getString("taskName");
                    String taskDateText = bundle.getString("taskDateText");
                    int taskID = bundle.getInt("taskID");
                    Toast.makeText(this, taskName, Toast.LENGTH_SHORT).show();
                    //final Task newTask = new Task(taskID, taskName, taskDateText, false);
                    final Task newTask = new Task(taskName, taskDateText, false, false);
                    newTask.setDone(todoList.getTask(taskID).isDone());
                    todoList.deleteTask(todoList.getTask(taskID));
                    todoList.addTask(taskID, newTask);
                    adapter.notifyItemInserted(taskID);
                    adapter.notifyDataSetChanged();
                    todoList.sort();
                    nested.scrollTo(0, 0);

                }
            }
        }*/
    }

}
