package hanas.dnidomatury.examActivity.TaskList;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import java.lang.ref.WeakReference;
import java.util.Calendar;

import hanas.dnidomatury.R;
import hanas.dnidomatury.examActivity.DataViewModel;
import hanas.dnidomatury.model.ExamSpecificList;
import hanas.dnidomatury.model.matura.Exam;
import hanas.dnidomatury.model.matura.ExamsList;
import hanas.dnidomatury.model.sheet.SheetsList;
import hanas.dnidomatury.model.task.Task;
import hanas.dnidomatury.model.task.TasksList;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

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

    View view;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        Bundle bundle = getArguments();
        mSelectedExam = ExamsList.fromFile(true, getActivity()).get(bundle.getInt("selectedExamPOS"));
        //DataViewModel data = ViewModelProviders.of(getActivity()).get(DataViewModel.class);
        darkColorID = mSelectedExam.getDarkColorID(getActivity());
        DataViewModel data = ViewModelProviders.of(getActivity()).get(DataViewModel.class);
        long a = Calendar.getInstance().getTimeInMillis();
        todoList = data.getTasks();
        new ReadData(this, todoList).execute(getActivity());
//        long b = Calendar.getInstance().getTimeInMillis();
//        System.out.println("ZACZYBNAMYYYFRAG" + (b - a));
//        long c = Calendar.getInstance().getTimeInMillis();
//        System.out.println(c - b);
//        RecyclerView recyclerView = view.findViewById(R.id.tasks_recycler_view);
//        recyclerView.setLayoutManager(layoutManager);
//        long d = Calendar.getInstance().getTimeInMillis();
//        System.out.println(d - c);
//        adapter = new TaskAdapter(this, todoList, darkColorID);
//        long e = Calendar.getInstance().getTimeInMillis();
//        System.out.println(e - d);
//        recyclerView.setAdapter(adapter);
//        long f = Calendar.getInstance().getTimeInMillis();
//        System.out.println(f - e);
//                    recyclerView.setAdapter(adapter);
        CompositeDisposable mDisposable = new CompositeDisposable();
//        mDisposable.add(Observable.fromCallable(() -> {
//            Context ctx = getActivity();
//            ExamSpecificList<Task> list = null;
//            if (ctx != null)
//                list = TasksList.fromFile(getActivity(), mSelectedExam);
//            //data.setFromFile(context, mSelectedExam);
//            return list;
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe((list) -> {
//                    RecyclerView recyclerView = view.findViewById(R.id.tasks_recycler_view);
//                    recyclerView.setHasFixedSize(true);
//                    CustomLayoutManager layoutManager = new CustomLayoutManager(getActivity());
//                    recyclerView.setLayoutManager(layoutManager);
//                    adapter = new TaskAdapter(this, list, darkColorID);
//                    todoList = list;
//                    recyclerView.setAdapter(adapter);
//                }));

        //todoList = TasksList.fromFile(getActivity(), mSelectedExam);

//        recyclerView = rootView.findViewById(R.id.tasks_recycler_view);
//        recyclerView.setHasFixedSize(true);
//        CustomLayoutManager layoutManager = new CustomLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new TaskAdapter(this, todoList, darkColorID);
//        recyclerView.setAdapter(adapter);
    }

//    private class ReadData extends AsyncTask<Void, Void, Void> {
//
//        Bundle bundle;
//        Fragment fragment;
//
//        ReadData(Bundle bundle, Fragment fragment) {
//            this.bundle = bundle;
//            this.fragment = fragment;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (bundle != null) {
//                mSelectedExam = ExamsList.fromFile(true, getActivity()).get(bundle.getInt("selectedExamPOS"));
//                darkColorID = mSelectedExam.getDarkColorID(getActivity());
//                todoList = TasksList.fromFile(getActivity(), mSelectedExam);
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            //nested = rootView.findViewById(R.id.nested);
//            //int selectedExamPOS;
//            recyclerView = rootView.findViewById(R.id.tasks_recycler_view);
//            recyclerView.setHasFixedSize(true);
//            CustomLayoutManager layoutManager = new CustomLayoutManager(getActivity());
//            recyclerView.setLayoutManager(layoutManager);
//            adapter = new TaskAdapter(fragment, todoList, darkColorID);
//            recyclerView.setAdapter(adapter);
//        }
//    }

    private class ReadData extends AsyncTask<Context, Void, CustomLayoutManager> {

        WeakReference<TaskListFragment> fragmentReference;
        ExamSpecificList<Task> tasks;
        public ReadData(TaskListFragment fragment, ExamSpecificList<Task> tasks) {
            this.fragmentReference = new WeakReference<>(fragment);
            this.tasks = tasks;
        }
        @Override
        protected CustomLayoutManager doInBackground(Context... contexts) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new CustomLayoutManager(contexts[0]);
        }

        @Override
        protected void onPostExecute(CustomLayoutManager customLayoutManager) {
            TaskListFragment fragment = fragmentReference.get();
            if (fragment == null) return;
            RecyclerView recyclerView = fragment.view.findViewById(R.id.tasks_recycler_view);
            recyclerView.setLayoutManager(customLayoutManager);
            TaskAdapter adapter = new TaskAdapter(fragment, tasks);
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
                    if (adapter != null) adapter.notifyItemInserted(newPosition);
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
                    if (adapter != null) adapter.notifyItemChanged(taskID);

                    if (dateChanged) {
                        int newTaskPos = todoList.moveAndSort(taskID, false);
                        if (newTaskPos == taskID)
                            newTaskPos = todoList.moveAndSort(taskID, true);
                        if (adapter != null) adapter.notifyItemMoved(taskID, newTaskPos);
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
