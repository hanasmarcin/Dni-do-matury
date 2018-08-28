package hanas.dnidomatury;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        final EditText taskName = (EditText) findViewById(R.id.add_task_name);
        final TextView taskDate = findViewById(R.id.add_task_date);
        Button addTaskButton = findViewById(R.id.button_add_task);
        Button addDateButton = findViewById(R.id.button_add_date_to_task);
        Button exit = findViewById(R.id.button_clear_new_task);
        final int taskID;

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            taskID = bundle.getInt("taskID");
            taskName.setText(bundle.getString("taskName"));
            taskDate.setText(bundle.getString("taskDate"));
            addDateButton.setText("Edytuj datę");
            addTaskButton.setText("Edytuj zadanie");
            setTitle("Edytuj zadanie");
        }
        else taskID = 1;

            addTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("taskName", taskName.getText().toString());
                    intent.putExtra("taskDateText", taskDate.getText().toString());
                    intent.putExtra("taskID", taskID);
                    setResult(RESULT_OK, intent);
                    finish();

                }
            });

            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            addDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    taskDate.setText(monthOfYear<9 ? dayOfMonth + "." + "0" + (monthOfYear + 1) + "." + year : dayOfMonth + "." + (monthOfYear + 1) + "." + year);

                                }
                            }, mYear, mMonth, mDay);

                    datePickerDialog.setTitle("Wybierz datę zadania");
                    datePickerDialog.show();
                }


            });



    }


}
