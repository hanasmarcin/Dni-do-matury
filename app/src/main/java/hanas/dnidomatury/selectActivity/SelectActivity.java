package hanas.dnidomatury.selectActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.Menu;
import android.view.MenuItem;

import hanas.dnidomatury.R;
import hanas.dnidomatury.model.exam.ExamsFileSupportedList;
import hanas.dnidomatury.model.exam.ExamsList;
import hanas.dnidomatury.model.exam.Exam;
import hanas.dnidomatury.model.exam.SelectedExamsList;
import hanas.dnidomatury.touchHelper.SimpleItemTouchHelperCallback;

public class SelectActivity extends AppCompatActivity implements AddExamFragment.ExamDialogListener {

    private ExamsList mListOfExam;
    private SelectExamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Toolbar toolbar = findViewById(R.id.toolbar_select);
        setSupportActionBar(toolbar);

        mListOfExam = SelectedExamsList.getInstance(this);

        //final CoordinatorLayout coordinator = findViewById(R.id.full_coordinator);
        final RecyclerView recyclerView = findViewById(R.id.full_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SelectExamAdapter(this, mListOfExam, false);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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

    @Override
    public void addExam(String name, String type, String level, String color, String examDateText) {
        Exam newExam = ExamsFileSupportedList.fromFile(false, this).findExam(name, level, type);
        if (color != null && newExam != null) {
            newExam.setColorScheme(color);
        }
        if (type.contains("pisemn")) {
            if (newExam != null) {
                newExam.setNewTasksCounter(this);
                newExam.setNewSheetsAverage(this);
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
