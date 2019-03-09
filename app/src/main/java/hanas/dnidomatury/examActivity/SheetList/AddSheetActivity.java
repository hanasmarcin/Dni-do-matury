package hanas.dnidomatury.examActivity.SheetList;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;

import hanas.dnidomatury.R;
import hanas.dnidomatury.model.task.Task;

public class AddSheetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sheet);

        final EditText sheetName = findViewById(R.id.sheet_name);
        final EditText points = findViewById(R.id.ponts);
        final EditText maxPoints = findViewById(R.id.max_points);
        final TextView sheetDate = findViewById(R.id.sheet_date);
        final Button addSheetButton = findViewById(R.id.button_add_sheet);
        Button addDateButton = findViewById(R.id.button_add_date_to_sheet);
        Button exit = findViewById(R.id.button_clear_new_sheet);
        final int sheetID;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sheetID = bundle.getInt("sheetID");
            sheetName.setText(bundle.getString("sheetName"));
            sheetDate.setText(bundle.getString("sheetDateText"));

            DecimalFormat df = new DecimalFormat("#.##");
            points.setText(df.format(bundle.getDouble("points")));
            maxPoints.setText(df.format(bundle.getDouble("maxPoints")));

            addDateButton.setText("Edytuj datę");
            addSheetButton.setText("Zatwierdź");
            setTitle("Edytuj arkusz");
        } else sheetID = 1;

        addSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("sheetName", sheetName.getText().toString());
                intent.putExtra("sheetDateText", sheetDate.getText().toString());
                intent.putExtra("points", Double.parseDouble(points.getText().toString()));
                intent.putExtra("maxPoints", Double.parseDouble(maxPoints.getText().toString()));
                intent.putExtra("sheetID", sheetID);
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
                final Calendar c = Task.TaskDateTextToDate((String) sheetDate.getText());

                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddSheetActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        sheetDate.setText(monthOfYear < 9 ? dayOfMonth + "." + "0" + (monthOfYear + 1) + "." + year : dayOfMonth + "." + (monthOfYear + 1) + "." + year);

                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.setTitle("Wybierz datę arkusza");
                datePickerDialog.show();
            }


        });

        points.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (points.getText().length() != 0 && maxPoints.getText().length() != 0)
                    addSheetButton.setEnabled(true);
            }
        });

        maxPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (points.getText().length() != 0 && maxPoints.getText().length() != 0)
                    addSheetButton.setEnabled(true);
            }
        });


    }

}
