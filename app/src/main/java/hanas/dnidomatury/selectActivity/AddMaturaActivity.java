package hanas.dnidomatury.selectActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import hanas.dnidomatury.R;
import hanas.dnidomatury.settingsActivity.SettingsActivity;

public class AddMaturaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_matura);

        final Spinner spinner = findViewById(R.id.spinner);
        final Spinner spinner2 = findViewById(R.id.spinner2);
        final TextView taskDate = findViewById(R.id.add_task_date);
        Button addTaskButton = findViewById(R.id.button_add_task);
        Button addDateButton = findViewById(R.id.button_add_date_to_task);
        Button exit = findViewById(R.id.button_clear_new_task);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.przedmiot_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.poziom_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);


        addTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("maturaName", spinner.getSelectedItem().toString());
                    intent.putExtra("maturaType", "ustna");
                    intent.putExtra("maturaLevel", spinner2.getSelectedItem().toString());
                    intent.putExtra("maturaDateText", taskDate.getText().toString());
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
                    int mHourOfDay = c.get(Calendar.HOUR_OF_DAY);
                    int mMinute = c.get(Calendar.MINUTE);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddMaturaActivity.this, new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    taskDate.setText(monthOfYear<9 ? dayOfMonth + "." + "0" + (monthOfYear + 1) + "." + year : dayOfMonth + "." + (monthOfYear + 1) + "." + year);

                                }
                            }, mYear, mMonth, mDay);

                    datePickerDialog.setTitle("Wybierz datę zadania");

                    TimePickerDialog timePickerDialog = new TimePickerDialog(AddMaturaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            taskDate.setText(taskDate.getText()+" "+i+":"+i1);

                        }
                    }, mHourOfDay, mMinute, true);

                    timePickerDialog.show();
                    datePickerDialog.show();
                }


            });



    }


}
