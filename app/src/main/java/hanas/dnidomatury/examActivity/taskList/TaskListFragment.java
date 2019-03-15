package hanas.dnidomatury.examActivity.taskList;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import hanas.dnidomatury.R;
import hanas.dnidomatury.examActivity.DataViewModel;
import hanas.dnidomatury.examActivity.CustomLayoutManager;
import hanas.dnidomatury.model.examSpecific.ExamItemsList;
import hanas.dnidomatury.model.examSpecific.task.Task;

import static android.app.Activity.RESULT_OK;
import static hanas.dnidomatury.model.examSpecific.task.Task.TaskHeader.NOT;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment {

    private ExamItemsList<Task> tasksList;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;

    public TaskListFragment() {
        // Required empty public constructor
    }

    public static TaskListFragment newInstance() {
        // Create new fragment with variables passed via bundle
        System.out.println(111);
        Bundle args = new Bundle();
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate view from xml
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get the data from ViewModel, which is available for parent fragment and its' fragments
        DataViewModel data = ViewModelProviders.of(getActivity()).get(DataViewModel.class);
        tasksList = data.getTasks();
        int colorID = data.getColorID();
        recyclerView = view.findViewById(R.id.tasks_recycler_view);

        // Set up the recyclerView
        adapter = new TaskAdapter(this, tasksList, colorID);
        CustomLayoutManager customLayoutManager = new CustomLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(customLayoutManager);
        // After the recyclerView is set up, update the observable loading state in data,
        // that fragment is loaded and ready to be swiped to
        data.loadingState.setTaskListFragmentLoaded(true);

    }

    private void addTask(Bundle bundle) {
        // Get task/s properties from bundle
        String taskName = bundle.getString("taskName");
        String taskDateText = bundle.getString("taskDateText");

        // Create new task and add it to the list
        final Task newTask = new Task(taskName, taskDateText, NOT);
        tasksList.add(1, newTask);
        // Move task to the right position on the list and get this position
        int newPosition = tasksList.moveAndSort(1, true);
        recyclerView.scrollToPosition(newPosition);
        if (adapter != null) adapter.notifyItemInserted(newPosition);
    }

    private void editTask(Bundle bundle) {
        // Get task/s properties from bundle
        String taskName = bundle.getString("taskName");
        String taskDateText = bundle.getString("taskDateText");
        int taskID = bundle.getInt("taskID");

        // Find the task, that needs to be edited
        Task editedTask = tasksList.get(taskID);
        // Check, if it's date was edited
        boolean dateChanged = false;
        if (!editedTask.getTaskDateText().equals(taskDateText)) dateChanged = true;
        // Update task's properties
        editedTask.update(taskName, taskDateText);
        if (adapter != null) recyclerView.getAdapter().notifyItemChanged(taskID);

        // If task's date was edited, move task to the correct position
        if (dateChanged) {
            int newTaskPos = tasksList.moveAndSort(taskID, false);
            if (newTaskPos == taskID)
                newTaskPos = tasksList.moveAndSort(taskID, true);
            if (adapter != null) adapter.notifyItemMoved(taskID, newTaskPos);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // Get data provided by finished dialogFragment
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            // Depending on requested action, add or edit task
            if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == RESULT_OK)
                addTask(bundle);
            else if (requestCode == EDIT_TASK_REQUEST_CODE && resultCode == RESULT_OK)
                editTask(bundle);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_exam, menu);
    }

    private static final int ADD_TASK_REQUEST_CODE = 3523;
    final static int EDIT_TASK_REQUEST_CODE = 4334;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_task) {
            // If ADD button on the toolbar was clicked, start new dialogFragment
            AddTaskFragment addTaskDialog = AddTaskFragment.forAdd();
            addTaskDialog.setTargetFragment(this, ADD_TASK_REQUEST_CODE);
            addTaskDialog.show(getFragmentManager(), "WTF");
        }
        return super.onOptionsItemSelected(item);
    }
}
