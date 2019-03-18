package hanas.dnidomatury.examActivity.taskList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import hanas.dnidomatury.R;
import hanas.dnidomatury.examActivity.AddDate;

import static android.app.Activity.RESULT_OK;

public class AddTaskFragment extends DialogFragment implements AddDate {

    private EditText taskName;
    private TextView taskDate;
    private boolean isNew;
    private int taskID;


    public AddTaskFragment() {
        // Empty constructor is required for DialogFragment
    }

    static AddTaskFragment forEdit(int taskID, String taskName, String taskDate) {
        // Create new instance of dialogFragment for task's editing
        Bundle args = new Bundle();
        args.putInt("taskID", taskID);
        args.putString("taskName", taskName);
        args.putString("taskDateText", taskDate);
        args.putBoolean("isNew", false);

        AddTaskFragment fragment = new AddTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    static AddTaskFragment forAdd() {
        // Create new instance of dialogFragment for adding new task
        Bundle args = new Bundle();
        args.putBoolean("isNew", true);

        AddTaskFragment fragment = new AddTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view,
                             Bundle savedInstanceState) {
        //Create fragment's view
        isNew = getArguments().getBoolean("isNew");
        getDialog().setTitle(isNew ? "Dodaj zadanie" : "Edytuj zadanie");

        return inflater.inflate(R.layout.dialog_fragment_add_task, view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get fields from view
        taskName = view.findViewById(R.id.add_task_name);
        taskDate = view.findViewById(R.id.add_task_date);
        Button addTaskButton = view.findViewById(R.id.button_add_task);
        Button addDateButton = view.findViewById(R.id.button_add_date_to_task);
        Button exit = view.findViewById(R.id.button_clear_new_task);
        TextView title = view.findViewById(R.id.add_task_title);

        title.setText(isNew ? "Dodaj zadanie" : "Edytuj zadanie");

        // Set fields' values
        if (!isNew) {
            String taskNameStr = getArguments().getString("taskName");
            String taskDateStr = getArguments().getString("taskDateText");
            taskID = getArguments().getInt("taskID");
            taskName.setText(taskNameStr);
            taskDate.setText(taskDateStr);
        } else {
            taskID = 1;
        }

        // Set buttons' listeners
        addTaskButton.setOnClickListener(v -> sendResult());
        exit.setOnClickListener(v -> dismiss());
        addDateButton.setOnClickListener(v -> addDate(taskDate, getActivity()));


    }

    private void sendResult() {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("taskName", taskName.getText().toString());
        intent.putExtra("taskDateText", taskDate.getText().toString());
        intent.putExtra("taskID", taskID);
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
        dismiss();
    }

}
