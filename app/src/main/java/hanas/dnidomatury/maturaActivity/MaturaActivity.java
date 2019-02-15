package hanas.dnidomatury.maturaActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hanas.dnidomatury.R;
import hanas.dnidomatury.matura.ListOfMatura;
import hanas.dnidomatury.matura.Matura;
import hanas.dnidomatury.matura.MaturaTimer;
import hanas.dnidomatury.matura.task.ListOfTasks;
import hanas.dnidomatury.matura.task.Task;

import static hanas.dnidomatury.matura.task.Task.TaskHeader.NOT;


public class MaturaActivity extends AppCompatActivity {

    ListOfTasks todoList;
    Matura selectedMatura;
    int selectedMaturaPOS;
    RecyclerView recyclerView;
    TaskAdapter adapter;
    NestedScrollView nested;
    List<Matura> listOfMatura = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matura);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_matura);
        nested = findViewById(R.id.nested);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            selectedMaturaPOS = bundle.getInt("selectedMaturaPOS");

            listOfMatura = ListOfMatura.readFromFile(this, true);
            selectedMatura = listOfMatura.get(selectedMaturaPOS);

            todoList = new ListOfTasks(selectedMatura.getName(), selectedMatura.getType(), selectedMatura.getLevel());
            todoList.readFromFile(this);

            final int primaryColorID = selectedMatura.getPrimaryColorID(this);
            final int darkColorID = selectedMatura.getDarkColorID(this);

            setTitle(selectedMatura.getName());
            toolbar.setSubtitle(selectedMatura.getType() + " " + selectedMatura.getLevel());
            toolbar.setBackgroundColor(ContextCompat.getColor(this, primaryColorID));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, darkColorID));
            }

            TextView daysTimer = findViewById(R.id.days_timer_matura_activity);
            TextView hmsTimer = findViewById(R.id.hms_timer_matura_activity);

            new MaturaTimer().startMaturaTimer(this, selectedMatura, daysTimer, hmsTimer);

            recyclerView = findViewById(R.id.tasks_recycler_view);
            recyclerView.setHasFixedSize(true);
            CustomLayoutManager layoutManager = new CustomLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            adapter = new TaskAdapter(this, selectedMatura, todoList);
            adapter.setHasStableIds(true);
            recyclerView.setAdapter(adapter);

        }

    }


    public class CustomLayoutManager extends LinearLayoutManager {

        public CustomLayoutManager(Context context) {
            super(context);
        }

        @Override
        public boolean supportsPredictiveItemAnimations(){
            return true;
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }


        @Override
    protected void onPause() {
        super.onPause();
        todoList.saveToFile(this);
        selectedMatura.setTasksCounter(todoList.getTasksCounter());
        ListOfMatura.saveToFile(this, listOfMatura);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //todoList.saveToFile(this);
        //selectedMatura.setTasksCounter(todoList.getTasksCounter());
        //ListOfMatura.saveToFile(this, listOfMatura);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_matura, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_task) {

            Intent intent = new Intent(this, AddTaskActivity.class);
            //intent.putExtra("selectedMaturaID", selectedMaturaID);
            startActivityForResult(intent, getResources().getInteger(R.integer.request_code_new_task));
            //adapter.notifyDataSetChanged();
            /*Task newTask = new Task(todoList.getListOfTasks().size(), "adffdfv");
            todoList.addTask(newTask);
            adapter.notifyDataSetChanged();*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == getResources().getInteger(R.integer.request_code_new_task)){
            if (resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                if (bundle!=null) {
                    String taskName = bundle.getString("taskName");
                    String taskDateText = bundle.getString("taskDateText");
                    //Toast.makeText(this, taskName, Toast.LENGTH_SHORT).show();
                    //final Task newTask = new Task(1, taskName, taskDateText, false);
                    final Task newTask = new Task(taskName, taskDateText, NOT);
                    todoList.addTask(1, newTask);
                    todoList.incrementTasksCounter();
                    //Toast.makeText(this, "counter "+todoList.getTasksCounter(), Toast.LENGTH_SHORT).show();
                    //selectedMatura.setTasksCounter(selectedMatura.getTasksCounter()+1);
                    adapter.incrementDoneHeaderID();
                    adapter.notifyDataSetChanged();
                    //todoList.sort();
                    nested.scrollTo(0, 0);

                }
            }
        }
        else if (requestCode == 5636){
            if (resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                if (bundle!=null) {
                    String taskName = bundle.getString("taskName");
                    String taskDateText = bundle.getString("taskDateText");
                    int taskID = bundle.getInt("taskID");
                    //Toast.makeText(this, taskName, Toast.LENGTH_SHORT).show();
                    //final Task newTask = new Task(taskID, taskName, taskDateText, false);
                    final Task newTask = new Task(taskName, taskDateText, NOT);
                    newTask.setDone(todoList.getTask(taskID).isDone());
                    todoList.deleteTask(todoList.getTask(taskID));
                    todoList.addTask(taskID, newTask);
                    adapter.notifyItemInserted(taskID);
                    adapter.notifyDataSetChanged();
                    //todoList.sort();
                    nested.scrollTo(0, 0);

                }
            }
        }
    }
}