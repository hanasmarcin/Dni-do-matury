package hanas.dnidomatury.model.examSpecific;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public interface ExamItem extends Serializable, Comparable {

    static Calendar dateTextToDate(String taskDateText) {
        SimpleDateFormat formatter = new SimpleDateFormat("d.MM.yyyy", Locale.ENGLISH);
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(formatter.parse(taskDateText));
            return cal;
        } catch (ParseException e) {
            return Calendar.getInstance();
        }
    }
}
