package hanas.dnidomatury;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;



public class MaturaActivity extends AppCompatActivity {

    ListOfMatura listOfMatura = new ListOfMatura();
    ListOfTasks todoList;
    Matura selectedMatura;
    int selectedMaturaID;
    RecyclerView recyclerView;
    TaskAdapter adapter;
    NestedScrollView nested;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matura);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_matura);
        nested = findViewById(R.id.nested);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            selectedMaturaID = bundle.getInt("selectedMaturaID");
            todoList = new ListOfTasks(selectedMaturaID, false);
            listOfMatura.readFromFile(this);
            todoList.readFromFile(this);
            selectedMatura = listOfMatura.getListOfMatura().get(selectedMaturaID);

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
            CustomLayoutManager doneLayoutManager = new CustomLayoutManager(this);
            //layoutManager.setReverseLayout(true);
            //layoutManager.setStackFromEnd(true);
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
        listOfMatura.saveToFile(this);
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
                    Toast.makeText(this, taskName, Toast.LENGTH_SHORT).show();
                    final Task newTask = new Task(1, taskName, taskDateText, false);
                    todoList.addTask(1, newTask);
                    selectedMatura.setTasksCounter(selectedMatura.getTasksCounter()+1);
                    adapter.incrementDoneHeaderID();
                    adapter.notifyDataSetChanged();
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
                    Toast.makeText(this, taskName, Toast.LENGTH_SHORT).show();
                    final Task newTask = new Task(taskID, taskName, taskDateText, false);
                    newTask.setDone(todoList.getTask(taskID).isDone());
                    todoList.deleteTask(taskID);
                    todoList.addTask(taskID, newTask);
                    adapter.notifyItemInserted(taskID);
                    adapter.notifyDataSetChanged();
                    nested.scrollTo(0, 0);

                }
            }
        }
    }
}