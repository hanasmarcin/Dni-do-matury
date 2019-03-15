package hanas.dnidomatury.model.exam;

import android.content.Context;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import hanas.dnidomatury.model.examSpecific.ExamItemsList;
import hanas.dnidomatury.model.examSpecific.sheet.Sheet;
import hanas.dnidomatury.model.examSpecific.sheet.SheetsAverage;
import hanas.dnidomatury.model.examSpecific.sheet.SheetsList;
import hanas.dnidomatury.model.examSpecific.task.Task;
import hanas.dnidomatury.model.examSpecific.task.TasksCounter;
import hanas.dnidomatury.model.examSpecific.task.TasksList;

public class Exam implements Comparable<Exam>, Serializable {

    private String name;
    private String level;
    private String type;
    private Calendar date;
    private String primaryColor;
    private TasksCounter tasksCounter;
    private SheetsAverage sheetsAverage;
    private transient static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());


    public TasksCounter getTasksCounter() {
        return tasksCounter;
    }

    public SheetsAverage getSheetsAverage() {
        return sheetsAverage;
    }

    public Exam(String name, String level, String type, String dateText, String primaryColor) {
        this.name = name;
        this.level = level;
        this.type = type;
        this.primaryColor = primaryColor;
        this.tasksCounter = new TasksCounter();
        this.sheetsAverage = new SheetsAverage();
        try {
            Date date = sdf.parse(dateText);
            this.date = Calendar.getInstance();
            this.date.setTime(date);
        } catch (ParseException e) {
            this.date = Calendar.getInstance();
        }
    }

    public int getPrimaryColorID(Context context) {
        return context.getResources().getIdentifier(this.primaryColor, "color", context.getPackageName());
    }

    public int getDarkColorID(Context context) {
        return context.getResources().getIdentifier(this.primaryColor + "Dark", "color", context.getPackageName());
    }

    public void setColorScheme(String color) {
        this.primaryColor = color;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public String getType() {
        return type;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(String dateInString) {
        try {
            this.date.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getStyleID(Context context) {
        return context.getResources().getIdentifier(this.primaryColor, "style", context.getPackageName());
    }


    @Override
    public int compareTo(@NonNull Exam exam) {
        return this.getDate().compareTo(exam.getDate());
    }


    public void setNewTasksCounter(Context context) {
        this.tasksCounter = new TasksCounter();
        ExamItemsList<Task> tasks = TasksList.fromFile(context, this);
        tasksCounter.setCounter(tasks);
    }

    public void setNewSheetsAverage(Context context) {
        this.sheetsAverage = new SheetsAverage();
        ExamItemsList<Sheet> sheets = SheetsList.fromFile(context, this);
        sheetsAverage.setAverage(sheets);
    }

}
