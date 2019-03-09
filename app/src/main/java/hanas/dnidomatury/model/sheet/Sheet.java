package hanas.dnidomatury.model.sheet;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;

public class Sheet implements Comparable<Sheet>, Serializable {
    //private int taskID;
    private String sheetName;
    private Calendar sheetDate;
    private double points;
    private double maxPoints;
    private String sheetDateText;

    public Sheet(String sheetName, String sheetDateText, double points, double maxPoints) {
        //this.taskID = taskID;
        this.sheetName = sheetName;
        this.sheetDate = dateTextToDate(sheetDateText);
        this.points = points;
        this.maxPoints = maxPoints;
        this.sheetDateText = sheetDateText;
    }

    public static Calendar dateTextToDate(String taskDateText) {
        SimpleDateFormat formatter = new SimpleDateFormat("d.MM.yyyy", Locale.ENGLISH);
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(formatter.parse(taskDateText));
            return cal;
        } catch (ParseException e) {
            return Calendar.getInstance();
        }
    }

    public String getSheetName() {
        return sheetName;
    }

    public String getSheetDateText() { return sheetDateText; }

    public double getPoints() {
        return points;
    }

    public double getMaxPoints() {
        return maxPoints;
    }

    public String getPercentScore() {
        return points/maxPoints*100 + "%";
    }

    @Override
    public int compareTo(@NonNull Sheet sheet) {
        return (-1)*this.sheetDate.compareTo(sheet.sheetDate);
    }
}
