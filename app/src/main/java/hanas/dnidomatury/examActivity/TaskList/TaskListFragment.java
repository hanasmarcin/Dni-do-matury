package hanas.dnidomatury.examActivity.TaskList;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import hanas.dnidomatury.R;
import hanas.dnidomatury.model.ExamSpecificList;
import hanas.dnidomatury.model.matura.Exam;
import hanas.dnidomatury.model.matura.ExamsList;
import hanas.dnidomatury.model.task.Task;
import hanas.dnidomatury.model.task.TasksList;

import static android.app.Activity.RESULT_OK;
import static hanas.dnidomatury.model.task.Task.TaskHeader.NOT;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment {

    private ExamSpecificList<Task> todoList;
    private Exam mSelectedExam;
    private int darkColorID;
    RecyclerView recyclerView;
    TaskAdapter adapter;
    private NestedScrollView nested;

    public TaskListFragment() {
        // Required empty public constructor
    }

    public static TaskListFragment newInstance(int selectedExamPOS) {

        Bundle args = new Bundle();
        args.putInt("selectedMaturaPOS", selectedExamPOS);
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setHasOptionsMenu(true);

    }

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        rootView = inflater.inflate(R.layout.fragment_task_list, container, false);
        new ReadData(bundle, this).execute();

        return rootView;
    }

    private class ReadData extends AsyncTask<Void, Void, Void> {

        Bundle bundle;
        Fragment fragment;

        ReadData(Bundle bundle, Fragment fragment) {
            this.bundle = bundle;
            this.fragment = fragment;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (bundle != null) {
                mSelectedExam = ExamsList.fromFile(true, getActivity()).get(bundle.getInt("selectedExamPOS"));
                darkColorID = mSelectedExam.getDarkColorID(getActivity());
                todoList = TasksList.fromFile(getActivity(), mSelectedExam);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            nested = rootView.findViewById(R.id.nested);
            //int selectedExamPOS;
            recyclerView = rootView.findViewById(R.id.tasks_recycler_view);
            recyclerView.setHasFixedSize(true);
            CustomLayoutManager layoutManager = new CustomLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new TaskAdapter(fragment, todoList, darkColorID);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        todoList.toFile(getActivity(), mSelectedExam);
    }

    // TODO: 09.03.2019 IDENTYCZNY CLM, ZROBIĆ TYLKO JEDEN
    public class CustomLayoutManager extends LinearLayoutManager {

        public CustomLayoutManager(Context context) {
            super(context);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return true;
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == getResources().getInteger(R.integer.request_code_new_task)) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String taskName = bundle.getString("taskName");
                    String taskDateText = bundle.getString("taskDateText");

                    final Task newTask = new Task(taskName, taskDateText, NOT);
                    todoList.add(newTask);

                    //adapter.incrementDoneHeaderID();
                    adapter.notifyDataSetChanged();
                    todoList.sort();
                    nested.scrollTo(0, 0);

                }
            }
        } else if (requestCode == 5636) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String taskName = bundle.getString("taskName");
                    String taskDateText = bundle.getString("taskDateText");
                    int taskID = bundle.getInt("taskID");

                    final Task newTask = new Task(taskName, taskDateText, NOT);
                    newTask.setDone(todoList.get(taskID).isDone());
                    todoList.remove(taskID);
                    todoList.add(newTask);
                    adapter.notifyItemInserted(taskID);
                    adapter.notifyDataSetChanged();
                    todoList.sort();
                    nested.scrollTo(0, 0);

                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_exam, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_task) {
            Intent intent = new Intent(getActivity(), AddTaskActivity.class);
            startActivityForResult(intent, 22);
        }
        return super.onOptionsItemSelected(item);
    }
}
