package hanas.dnidomatury.selectActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import hanas.dnidomatury.R;
import hanas.dnidomatury.model.ExamsFileList;
import hanas.dnidomatury.model.matura.Exam;
import hanas.dnidomatury.model.matura.ExamsList;
import hanas.dnidomatury.model.matura.SelectedExamsList;

public class AddExamActivity extends AppCompatActivity {

    int examColor;
    String examColorID;
    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy H:mm", Locale.getDefault());

    private void setExamData(Exam exam, FloatingActionButton fabPrimary, FloatingActionButton fabDark, TextView examDate) {
        if (exam != null) {
            fabPrimary.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(AddExamActivity.this, exam.getPrimaryColorID(AddExamActivity.this))));
            fabDark.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(AddExamActivity.this, exam.getDarkColorID(AddExamActivity.this))));
            examDate.setText(dateFormat.format(exam.getDate().getTime()));
        } else {
            examColorID = "Green";
            fabPrimary.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(AddExamActivity.this, getResources().getIdentifier(examColorID, "color", getPackageName()))));
            fabDark.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(AddExamActivity.this, getResources().getIdentifier(examColorID + "Dark", "color", getPackageName()))));
            examDate.setText("Brak daty");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);

        final Spinner spinnerName = findViewById(R.id.spinner);
        final Spinner spinnerLevel = findViewById(R.id.spinnerLevel);
        final Spinner spinnerType = findViewById(R.id.spinner3);
        final TextView examDate = findViewById(R.id.exam_date);
        final Button addExamButton = findViewById(R.id.button_add_task);
        final Button addDateButton = findViewById(R.id.button_add_date_to_task);
        final LinearLayout colorLayout = findViewById(R.id.colorLayout);
        final FloatingActionButton fabPrimary = findViewById(R.id.fab);
        final FloatingActionButton fabDark = findViewById(R.id.fab2);
        final Button exit = findViewById(R.id.button_clear_new_task);

        final ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.typ_matury, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        final int[] examColors = getResources().getIntArray(R.array.exam_colors);
        final String[] colorNames = getResources().getStringArray(R.array.exam_color_names);
        final ExamsFileList allExamsList = ExamsList.fromFile(false, this);
        final ExamsFileList selectedExamsList = SelectedExamsList.getInstance(this);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            addDateButton.setText("Edytuj datę");
            addExamButton.setText("Zatwierdź");
            setTitle("Edytuj maturę");
            int selectedExamPOS = bundle.getInt("selectedExamPOS");
            ExamsFileList listOfExam = SelectedExamsList.getInstance(this);
            Exam selectedExam = listOfExam.get(selectedExamPOS);
            spinnerType.setSelection(getIndex(spinnerType, selectedExam.getType().substring(0, selectedExam.getType().length() - 1) + "a"));
            spinnerType.setEnabled(false);
            if (spinnerType.getSelectedItem().equals("pisemna")) {
                addAdapterForPisemna(spinnerName, spinnerLevel);
                addDateButton.setEnabled(false);
                addExamButton.setEnabled(true);


            } else {
                addAdapterForUstna(spinnerName, spinnerLevel);
                addDateButton.setEnabled(true);
            }
            spinnerName.setSelection(getIndex(spinnerName, selectedExam.getName()));
            spinnerName.setEnabled(false);
            spinnerLevel.setSelection(getIndex(spinnerLevel, selectedExam.getLevel().substring(0, selectedExam.getLevel().length() - 1) + "a"));
            spinnerLevel.setEnabled(false);
            Toast.makeText(this, selectedExam.getLevel().substring(0, selectedExam.getType().length() - 1) + "a", Toast.LENGTH_SHORT).show();
        }

        final SpectrumDialog.Builder colorDialogBuilder = new SpectrumDialog.Builder(this)
                .setColors(R.array.exam_colors)
                //.setSelectedColorRes(color)
                .setDismissOnColorSelected(false)
                //.setOutlineWidth()
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        if (positiveResult) {
                            examColor = color;
                            int colorPosition = 0;
                            for (int i = 0; i < examColors.length; i++) {
                                if (examColor == examColors[i]) colorPosition = i;
                            }
                            examColorID = colorNames[colorPosition];
                            fabPrimary.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(AddExamActivity.this, getResources().getIdentifier(examColorID, "color", getPackageName()))));
                            fabDark.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(AddExamActivity.this, getResources().getIdentifier(examColorID + "Dark", "color", getPackageName()))));

                        } else {
                            Toast.makeText(AddExamActivity.this, "Dialog cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        if (bundle == null)
            spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (adapterView.getItemAtPosition(i).equals(typeAdapter.getItem(0))) {
                        addAdapterForPisemna(spinnerName, spinnerLevel);
                        addDateButton.setEnabled(false);
                        addExamButton.setEnabled(true);


                    } else {
                        addAdapterForUstna(spinnerName, spinnerLevel);
                        addDateButton.setEnabled(true);
                        addExamButton.setEnabled(false);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    //nwm co tu dac
                }
            });


        if (bundle == null)
            spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Exam exam = allExamsList.findExam(spinnerName.getSelectedItem().toString(),
                            spinnerLevel.getSelectedItem().toString(),
                            spinnerType.getSelectedItem().toString());
                    setExamData(exam, fabPrimary, fabDark, examDate);
                    //setColor(spinnerName.getSelectedItem().toString(), spinnerType.getSelectedItem().toString(), spinnerLevel.getSelectedItem().toString(), spectrumPalette);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        spinnerLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Exam exam;
                if (bundle != null) {
                    int selectedExamPOS = bundle.getInt("selectedExamPOS");
                    ExamsFileList listOfExam = SelectedExamsList.getInstance(AddExamActivity.this);
                    exam = listOfExam.get(selectedExamPOS);
                } else {
                    exam = allExamsList.findExam(spinnerName.getSelectedItem().toString(),
                            spinnerLevel.getSelectedItem().toString(),
                            spinnerType.getSelectedItem().toString());
                }
                setExamData(exam, fabPrimary, fabDark, examDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                String examName = spinnerName.getSelectedItem().toString();
                String examType = spinnerType.getSelectedItem().toString();
                String examLevel = spinnerLevel.getSelectedItem().toString();
                if (examName.contains("Język")) {
                    examType = examType.substring(0, examType.length() - 1) + "y";
                    examLevel = examLevel.substring(0, examLevel.length() - 1) + "y";
                }
                intent.putExtra("examName", examName);
                intent.putExtra("examType", examType);
                intent.putExtra("examLevel", examLevel);
                intent.putExtra("examColor", examColorID);
                intent.putExtra("isNew", bundle == null);
                if (bundle != null) {
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (selectedExamsList.findExam(examName, examLevel, examType) != null)
                    Toast.makeText(AddExamActivity.this, "Taka exam już istnieje!", Toast.LENGTH_SHORT).show();
                else if (examType.contains("ustn")) {
                    intent.putExtra("examDateText", examDate.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (allExamsList.findExam(examName, examLevel, examType) == null)
                    Toast.makeText(AddExamActivity.this, "Taka exam nie istnieje!", Toast.LENGTH_SHORT).show();
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
                addDate(examDate, addExamButton);
            }
        });

        colorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorDialogBuilder.build().show(getSupportFragmentManager(), "dialog_demo_1");
            }
        });

    }

    private void addAdapterForUstna(Spinner spinnerName, Spinner spinnerLevel) {
        ArrayAdapter<CharSequence> nameAdapter = ArrayAdapter.createFromResource(AddExamActivity.this,
                R.array.przedmiot_ustny, android.R.layout.simple_spinner_item);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerName.setAdapter(nameAdapter);

        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(AddExamActivity.this,
                R.array.poziom_ustny_dj, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(levelAdapter);
    }

    private int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    private void addAdapterForPisemna(Spinner spinnerName, Spinner spinnerLevel) {
        ArrayAdapter<CharSequence> nameAdapter = ArrayAdapter.createFromResource(AddExamActivity.this,
                R.array.przedmiot_pisemny, android.R.layout.simple_spinner_item);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerName.setAdapter(nameAdapter);

        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(AddExamActivity.this,
                R.array.poziom_pisemny_dj, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(levelAdapter);
    }

    private void addDate(final TextView examDate, final Button addExamButton) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        final TimePickerDialog timePickerDialog = new TimePickerDialog(AddExamActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                c.set(Calendar.HOUR_OF_DAY, i);
                c.set(Calendar.MINUTE, i1);
                c.set(Calendar.SECOND, 0);
                //Toast.makeText(AddExamActivity.this, c.toString(), Toast.LENGTH_SHORT).show();
                addExamButton.setEnabled(true);
                //taskDate.setText(taskDate.getText()+" "+i+":"+i1);
            }
        }, mHourOfDay, mMinute, true);
        timePickerDialog.setTitle("Wybierz godzinę matury");

        final DatePickerDialog datePickerDialog = new DatePickerDialog(AddExamActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                examDate.setText(dateFormat.format(c.getTime()));
                timePickerDialog.show();
                //taskDate.setText(monthOfYear<9 ? dayOfMonth + "." + "0" + (monthOfYear + 1) + "." + year : dayOfMonth + "." + (monthOfYear + 1) + "." + year);
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.setTitle("Wybierz datę matury");
        datePickerDialog.show();
    }

}




