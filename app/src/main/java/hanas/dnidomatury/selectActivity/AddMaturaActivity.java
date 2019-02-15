package hanas.dnidomatury.selectActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import hanas.dnidomatury.R;
import hanas.dnidomatury.matura.ListOfMatura;
import hanas.dnidomatury.settingsActivity.SettingsActivity;

public class AddMaturaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_matura);

        final Spinner spinnerName = findViewById(R.id.spinner);
        final Spinner spinnerLevel = findViewById(R.id.spinnerLevel);
        final Spinner spinnerType = findViewById(R.id.spinner3);
        final TextView taskDate = findViewById(R.id.add_task_date);
        final Button addMaturaButton = findViewById(R.id.button_add_task);
        final Button addDateButton = findViewById(R.id.button_add_date_to_task);
        Button exit = findViewById(R.id.button_clear_new_task);


        final ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.typ_matury, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);


        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals(typeAdapter.getItem(0))) {
                    //Toast.makeText(AddMaturaActivity.this, "gowno", Toast.LENGTH_SHORT).show();
                    ArrayAdapter<CharSequence> nameAdapter = ArrayAdapter.createFromResource(AddMaturaActivity.this,
                            R.array.przedmiot_pisemny, android.R.layout.simple_spinner_item);
                    nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerName.setAdapter(nameAdapter);

                    ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(AddMaturaActivity.this,
                            R.array.poziom_pisemny_dj, android.R.layout.simple_spinner_item);
                    levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLevel.setAdapter(levelAdapter);
                    addDateButton.setEnabled(false);
                    addMaturaButton.setEnabled(true);


                } else {
                    ArrayAdapter<CharSequence> nameAdapter = ArrayAdapter.createFromResource(AddMaturaActivity.this,
                            R.array.przedmiot_ustny, android.R.layout.simple_spinner_item);
                    nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerName.setAdapter(nameAdapter);

                    ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(AddMaturaActivity.this,
                            R.array.poziom_ustny_dj, android.R.layout.simple_spinner_item);
                    levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLevel.setAdapter(levelAdapter);
                    addDateButton.setEnabled(true);
                    addMaturaButton.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //nwm co tu dac
            }
        });

        addMaturaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                String maturaName = spinnerName.getSelectedItem().toString();
                String maturaType = spinnerType.getSelectedItem().toString();
                String maturaLevel =  spinnerLevel.getSelectedItem().toString();
                if (maturaName.contains("Język")) {
                    maturaType = maturaType.substring(0, maturaType.length() - 1)+"y";
                    maturaLevel = maturaLevel.substring(0, maturaLevel.length() - 1) + "y";
                }
                intent.putExtra("maturaName", maturaName);
                intent.putExtra("maturaType", maturaType);
                intent.putExtra("maturaLevel",maturaLevel);
                if (maturaType.contains("ustn")) {
                    intent.putExtra("maturaDateText", taskDate.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else if(ListOfMatura.findMatura(maturaName, maturaLevel, maturaType, AddMaturaActivity.this, true) != null)
                    Toast.makeText(AddMaturaActivity.this, "Taka matura już istnieje!", Toast.LENGTH_SHORT).show();
                else if(ListOfMatura.findMatura(maturaName, maturaLevel, maturaType, AddMaturaActivity.this, false) == null)
                    Toast.makeText(AddMaturaActivity.this, "Taka matura nie istnieje!", Toast.LENGTH_SHORT).show();
                else {
                    setResult(RESULT_OK, intent);
                    finish();
                }


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
                addDate(taskDate);
                addMaturaButton.setEnabled(true);
            }
        });


    }

    private void addDate(final TextView taskDate) {
        final Calendar c = Calendar.getInstance();
        //Toast.makeText(this, c.getTimeZone().toString(), Toast.LENGTH_SHORT).show();
        //c.setTimeZone(TimeZone.getTimeZone("Europe/Warsaw"));
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        c.set(Calendar.AM_PM, Calendar.AM);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm", Locale.getDefault());

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddMaturaActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                c.set(year, monthOfYear, dayOfMonth);
                taskDate.setText(dateFormat.format(c.getTime()));
                //taskDate.setText(monthOfYear<9 ? dayOfMonth + "." + "0" + (monthOfYear + 1) + "." + year : dayOfMonth + "." + (monthOfYear + 1) + "." + year);

            }
        }, mYear, mMonth, mDay);

        datePickerDialog.setTitle("Wybierz datę matury");

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddMaturaActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                c.set(Calendar.HOUR, i);
                c.set(Calendar.MINUTE, i1);
                c.set(Calendar.SECOND, 0);
                Toast.makeText(AddMaturaActivity.this, c.toString(), Toast.LENGTH_SHORT).show();
                taskDate.setText(dateFormat.format(c.getTime()));
                //taskDate.setText(taskDate.getText()+" "+i+":"+i1);
            }
        }, mHourOfDay, mMinute, true);

        timePickerDialog.setTitle("Wybierz godzinę matury");

        timePickerDialog.show();
        datePickerDialog.show();
    }
}




