package hanas.dnidomatury.selectActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import hanas.dnidomatury.R;
import hanas.dnidomatury.examActivity.sheetList.AddSheetFragment;
import hanas.dnidomatury.model.exam.ExamsFileSupportedList;
import hanas.dnidomatury.model.exam.ExamsList;
import hanas.dnidomatury.model.exam.Exam;
import hanas.dnidomatury.model.exam.SelectedExamsList;
import hanas.dnidomatury.touchHelper.SimpleItemTouchHelperCallback;

public class SelectActivity extends AppCompatActivity implements AddExamFragment.ExamDialogListener {

    private static final int ADD_EXAM_REQUEST_CODE = 4364;
    private ExamsList mListOfExam;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private ItemTouchHelper mItemTouchHelper;
    private SelectExamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Toolbar toolbar = findViewById(R.id.toolbar_select);
        setSupportActionBar(toolbar);

        mListOfExam = SelectedExamsList.getInstance(this);

        final CoordinatorLayout coordinator = findViewById(R.id.full_coordinator);
        final RecyclerView recyclerView = findViewById(R.id.full_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SelectExamAdapter(this, mListOfExam, false);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    protected void onPause() {
        mListOfExam.toFile(this, true);
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
            AddExamFragment addExamDialog = AddExamFragment.forAdd();
            addExamDialog.show(getSupportFragmentManager(), "WTF4");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == 5320) {
//            if (resultCode == RESULT_OK) {
//                Bundle bundle = data.getExtras();
//                if (bundle != null) {
//                    String examName = bundle.getString("examName");
//                    String examType = bundle.getString("examType");
//                    String examLevel = bundle.getString("examLevel");
//                    String examDateText = bundle.getString("examDateText");
//                    String examColor = bundle.getString("examColor");
//                    boolean isNew = bundle.getBoolean("isNew");
//                    Toast.makeText(this, isNew + "", Toast.LENGTH_SHORT).show();
//
//                    if (!isNew) {
//                        Toast.makeText(this, "Nie jest nowa!", Toast.LENGTH_SHORT).show();
//                        Exam oldExam = mListOfExam.findExam(examName, examLevel, examType);
//                        if (examColor != null && oldExam != null) {
//                            oldExam.setColorScheme(examColor);
//                        }
//                        if (examDateText != null && oldExam != null) {
//                            oldExam.setDate(examDateText);
//                        }
//                    } else {
//                        Exam newExam = ExamsFileSupportedList.fromFile(false, this).findExam(examName, examLevel, examType);
//                        if (examColor != null && newExam != null) {
//                            newExam.setColorScheme(examColor);
//                        }
//                        if (examType.contains("pisemn")) {
//                            if (newExam != null) {
//                                newExam.setNewTasksCounter();
//                                newExam.setNewSheetsAverage();
//                                mListOfExam.add(newExam);
//                            }
//                        } else {
//
//                            try {
//                                if (examColor == null)
//                                    mListOfExam.add((new Exam(examName, examLevel, examType, examDateText, "Green")));
//                                else
//                                    mListOfExam.add((new Exam(examName, examLevel, examType, examDateText, examColor)));
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    adapter.notifyDataSetChanged();
//
//                }
//            }
//        }
//        /*else if (requestCode == 5636){
//            if (resultCode == RESULT_OK){
//                Bundle bundle = data.getExtras();
//                if (bundle!=null) {
//                    String taskName = bundle.getString("taskName");
//                    String taskDateText = bundle.getString("taskDateText");
//                    int taskID = bundle.getInt("taskID");
//                    Toast.makeText(this, taskName, Toast.LENGTH_SHORT).show();
//                    //final Task newTask = new Task(taskID, taskName, taskDateText, false);
//                    final Task newTask = new Task(taskName, taskDateText, false, false);
//                    newTask.setDone(todoList.getTask(taskID).isDone());
//                    todoList.deleteTask(todoList.getTask(taskID));
//                    todoList.addTask(taskID, newTask);
//                    adapter.notifyItemInserted(taskID);
//                    adapter.notifyDataSetChanged();
//                    todoList.sort();
//                    nested.scrollTo(0, 0);
//
//                }
//            }
//        }*/
//    }

    @Override
    public void addExam(String name, String type, String level, String color, String examDateText) {
        Exam newExam = ExamsFileSupportedList.fromFile(false, this).findExam(name, level, type);
        if (color != null && newExam != null) {
            newExam.setColorScheme(color);
        }
        if (type.contains("pisemn")) {
            if (newExam != null) {
                newExam.setNewTasksCounter();
                newExam.setNewSheetsAverage();
                mListOfExam.add(newExam);
            }
        } else {

            try {
                if (color == null)
                    color = "Green";

                mListOfExam.add((new Exam(name, level, type, examDateText, color)));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        adapter.notifyItemInserted(mListOfExam.size()-1);
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void editExam(String name, String type, String level, String color, String examDateText, int examPOS) {
        //Toast.makeText(this, "Nie jest nowa!", Toast.LENGTH_SHORT).show();
        Exam oldExam = mListOfExam.findExam(name, level, type);
        if (color != null && oldExam != null) {
            oldExam.setColorScheme(color);
        }
        if (examDateText != null && oldExam != null) {
            oldExam.setDate(examDateText);
        }
        adapter.notifyItemChanged(examPOS);
    }
}
