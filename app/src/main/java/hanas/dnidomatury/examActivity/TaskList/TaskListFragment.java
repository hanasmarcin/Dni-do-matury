package hanas.dnidomatury.examActivity.TaskList;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import hanas.dnidomatury.R;
import hanas.dnidomatury.examActivity.DataViewModel;
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
    //private NestedScrollView nested;

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

        rootView = inflater.inflate(R.layout.fragment_task_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long start = Calendar.getInstance().getTimeInMillis();
        System.out.println("ZACZYNAMY SPRAWDZAĆ");
        Bundle bundle = getArguments();
        mSelectedExam = ExamsList.fromFile(true, getActivity()).get(bundle.getInt("selectedExamPOS"));
        long a = Calendar.getInstance().getTimeInMillis();
        System.out.println(a - start);
        DataViewModel data = ViewModelProviders.of(getActivity()).get(DataViewModel.class);
        darkColorID = mSelectedExam.getDarkColorID(getActivity());
        todoList = data.getTasks();
        long b = Calendar.getInstance().getTimeInMillis();
        System.out.println(b - a);


        long c = Calendar.getInstance().getTimeInMillis();
        System.out.println(c - b);//new ReadData(bundle, this).execute();
        //nested = rootView.findViewById(R.id.nested);
        //int selectedExamPOS;
        recyclerView = rootView.findViewById(R.id.tasks_recycler_view);
        recyclerView.setHasFixedSize(true);
        CustomLayoutManager layoutManager = new CustomLayoutManager(getActivity());
        long d = Calendar.getInstance().getTimeInMillis();
        System.out.println(d - c);
        recyclerView.setLayoutManager(layoutManager);
        long e = Calendar.getInstance().getTimeInMillis();
        System.out.println(e - d);
        adapter = new TaskAdapter(this, todoList, darkColorID);
        recyclerView.setAdapter(adapter);
        long f = Calendar.getInstance().getTimeInMillis();
        System.out.println(f - e);
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
            //nested = rootView.findViewById(R.id.nested);
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
        //todoList.toFile(getActivity(), mSelectedExam);

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
            return true;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ADD_TASK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String taskName = bundle.getString("taskName");
                    String taskDateText = bundle.getString("taskDateText");

                    final Task newTask = new Task(taskName, taskDateText, NOT);
                    todoList.add(1, newTask);
                    int newPosition = todoList.moveAndSort(1, true);
                    adapter.notifyItemInserted(newPosition);
                }

            }
        } else if (requestCode == EDIT_TASK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String taskName = bundle.getString("taskName");
                    String taskDateText = bundle.getString("taskDateText");
                    int taskID = bundle.getInt("taskID");
                    Task editedTask = todoList.get(taskID);
                    boolean dateChanged = false;
                    if (!editedTask.getTaskDateText().equals(taskDateText)) dateChanged = true;
                    editedTask.update(taskName, taskDateText);
                    adapter.notifyItemChanged(taskID);

                    if (dateChanged) {
                        int newTaskPos = todoList.moveAndSort(taskID, false);
                        if (newTaskPos == taskID)
                            newTaskPos = todoList.moveAndSort(taskID, true);
                        adapter.notifyItemMoved(taskID, newTaskPos);
                    }
                    //todoList.sort();
                    //nested.scrollTo(0, 0);

                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_exam, menu);
    }

    static final int ADD_TASK_REQUEST_CODE = 3523;
    final static int EDIT_TASK_REQUEST_CODE = 4334;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_task) {
            AddTaskFragment addTaskDialog = AddTaskFragment.newInstanceAdd();
            addTaskDialog.setTargetFragment(this, ADD_TASK_REQUEST_CODE);
            addTaskDialog.show(getFragmentManager(), "WTF");
        }
        return super.onOptionsItemSelected(item);
    }
}
