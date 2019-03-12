package hanas.dnidomatury.examActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public interface AddDate {

    default void addDate(TextView previousDate, Context context) {
        SimpleDateFormat formatter = new SimpleDateFormat("d.MM.yyyy", Locale.ENGLISH);
        final Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(formatter.parse(String.valueOf(previousDate.getText())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            // Set date to textView, when the date is chosen
            cal.set(year, monthOfYear, dayOfMonth);
            previousDate.setText(formatter.format(cal.getTime()));
        }, mYear, mMonth, mDay);

        datePickerDialog.setTitle("Wybierz datę zadania");
        datePickerDialog.show();


    }
}
