package hanas.dnidomatury.model.examSpecific.sheet;

import java.util.Calendar;
import java.util.Observable;

import androidx.annotation.NonNull;
import hanas.dnidomatury.model.examSpecific.ExamItem;

public class Sheet extends Observable implements ExamItem {
    //private int taskID;
    private String sheetName;
    private Calendar sheetDate;
    private double points;
    private double maxPoints;
    private String sheetDateText;

    public Sheet(String sheetName, String sheetDateText, double points, double maxPoints) {
        //this.taskID = taskID;
        this.sheetName = sheetName;
        this.sheetDate = ExamItem.dateTextToDate(sheetDateText);
        this.points = points;
        this.maxPoints = maxPoints;
        this.sheetDateText = sheetDateText;
    }

    public String getSheetName() {
        return sheetName;
    }

    public String getSheetDateText() {
        return sheetDateText;
    }

    public double getPoints() {
        return points;
    }

    public double getMaxPoints() {
        return maxPoints;
    }

    @Override
    public int compareTo(@NonNull Object obj) {
        Sheet sheet = (Sheet) obj;
        return (-1) * this.sheetDate.compareTo(sheet.sheetDate);
    }

    public void update(String sheetName, String sheetDateText, double points, double maxPoints) {
        double[] percentages = new double[2];
        percentages[0] = this.points / this.maxPoints * 100;
        percentages[1] = points / maxPoints * 100;
        this.sheetName = sheetName;
        this.sheetDateText = sheetDateText;
        this.sheetDate = ExamItem.dateTextToDate(sheetDateText);
        this.points = points;
        this.maxPoints = maxPoints;
        setChanged();
        notifyObservers(percentages);
    }
}
