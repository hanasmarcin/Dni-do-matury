package hanas.dnidomatury.examActivity.TaskList;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import hanas.dnidomatury.R;
import hanas.dnidomatury.model.task.Task;

import static android.app.Activity.RESULT_OK;

public class AddTaskFragment extends DialogFragment {

    public AddTaskFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddTaskFragment newInstanceEdit(int taskID, String taskName, String taskDate) {

        Bundle args = new Bundle();
        args.putInt("taskID", taskID);
        args.putString("taskName", taskName);
        args.putString("taskDateText", taskDate);
        args.putBoolean("isNew", false);
        AddTaskFragment fragment = new AddTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AddTaskFragment newInstanceAdd() {

        Bundle args = new Bundle();
        args.putBoolean("isNew", true);

        AddTaskFragment fragment = new AddTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    boolean isNew;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view,
                             Bundle savedInstanceState) {
        isNew = getArguments().getBoolean("isNew");
        getDialog().setTitle(isNew ? "Dodaj zadanie" : "Edytuj zadanie");

        return inflater.inflate(R.layout.activity_add_task, view);
    }

    private EditText taskName;
    private TextView taskDate;
    Button addTaskButton;
    Button addDateButton;
    Button exit;
    private int taskID;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get field from view
        //String title = getArguments().getString("title", "Enter Name");
        taskName = view.findViewById(R.id.add_task_name);
        taskDate = view.findViewById(R.id.add_task_date);
        addTaskButton = view.findViewById(R.id.button_add_task);
        addDateButton = view.findViewById(R.id.button_add_date_to_task);
        exit = view.findViewById(R.id.button_clear_new_task);

        if (!isNew) {
            String taskNameStr = getArguments().getString("taskName");
            String taskDateStr = getArguments().getString("taskDateText");
            taskID = getArguments().getInt("taskID");
            taskName.setText(taskNameStr);
            taskDate.setText(taskDateStr);
        } else {
            taskID = 1;
        }

        addTaskButton.setOnClickListener(v -> {
            sendResult();
            dismiss();
        });

        exit.setOnClickListener(v ->

        {
            dismiss();
        });

        addDateButton.setOnClickListener(v ->

        {
            final Calendar c = Task.taskDateTextToDate((String) taskDate.getText());

            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {

                    taskDate.setText(monthOfYear < 9 ? dayOfMonth + "." + "0" + (monthOfYear + 1) + "." + year : dayOfMonth + "." + (monthOfYear + 1) + "." + year);

                }
            }, mYear, mMonth, mDay);

            datePickerDialog.setTitle("Wybierz datę zadania");
            datePickerDialog.show();
        });


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


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_task);
//
//        final EditText taskName = findViewById(R.id.add_task_name);
//        final TextView taskDate = findViewById(R.id.add_task_date);
//        Button addTaskButton = findViewById(R.id.button_add_task);
//        Button addDateButton = findViewById(R.id.button_add_date_to_task);
//        Button exit = findViewById(R.id.button_clear_new_task);
//        final int taskID;
//
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            taskID = bundle.getInt("taskID");
//            taskName.setText(bundle.getString("taskName"));
//            taskDate.setText(bundle.getString("taskDate"));
//            addDateButton.setText("Edytuj datę");
//            addTaskButton.setText("Zatwierdź");
//            setTitle("Edytuj zadanie");
//        } else taskID = 1;
//
//        addTaskButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.putExtra("taskName", taskName.getText().toString());
//                intent.putExtra("taskDateText", taskDate.getText().toString());
//                intent.putExtra("taskID", taskID);
//                setResult(RESULT_OK, intent);
//                finish();
//
//            }
//        });
//
//        exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//        addDateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Calendar c = Task.taskDateTextToDate((String) taskDate.getText());
//
//                int mYear = c.get(Calendar.YEAR);
//                int mMonth = c.get(Calendar.MONTH);
//                int mDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskFragment.this, new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//
//                        taskDate.setText(monthOfYear < 9 ? dayOfMonth + "." + "0" + (monthOfYear + 1) + "." + year : dayOfMonth + "." + (monthOfYear + 1) + "." + year);
//
//                    }
//                }, mYear, mMonth, mDay);
//
//                datePickerDialog.setTitle("Wybierz datę zadania");
//                datePickerDialog.show();
//            }
//
//
//        });
//
//
//    }


}
