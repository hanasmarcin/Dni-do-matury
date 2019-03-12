package hanas.dnidomatury.model.exam;

import android.content.Context;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import hanas.dnidomatury.model.examSpecific.task.TasksCounter;

public class Exam implements Comparable<Exam>, Serializable {

    private String name;
    private String level;
    private String type;
    private Calendar date;
    private String primaryColor;
    private String darkColor;
    private TasksCounter tasksCounter = new TasksCounter();
    private double sheetsAverage;
    private transient static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    public void setTasksCounter(long x) {
        this.tasksCounter = new TasksCounter();
        tasksCounter.setCounter(x);
    }

    public TasksCounter getTasksCounter() {
        return tasksCounter;
    }

//    public void setTasksCounter(long tasksCounter) {
//        this.tasksCounter = tasksCounter;
//    }

    public double getSheetsAverage() {
        return sheetsAverage;
    }

    public void setSheetsAverage(double sheetsAverage) {
        this.sheetsAverage = sheetsAverage;
    }

    public Exam(String name, String level, String type, String dateText, String primaryColor, String darkColor) {
        this.name = name;
        this.level = level;
        this.type = type;
        try {
            Date date = sdf.parse(dateText);
            this.date = Calendar.getInstance();
            this.date.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.primaryColor = primaryColor;
        //this.darkColor = darkColor;
    }

    public int getPrimaryColorID(Context context) {
        return context.getResources().getIdentifier(this.primaryColor, "color", context.getPackageName());
    }

    public int getDarkColorID(Context context) {
        return context.getResources().getIdentifier(this.primaryColor + "Dark", "color", context.getPackageName());
        //return context.getResources().getIdentifier(this.darkColor, "color", context.getPackageName());
    }

    public void setColorScheme(String color) {
        this.primaryColor = color;
        //this.darkColor = color + "Dark";
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
    public int compareTo(Exam exam) {
        return this.getDate().compareTo(exam.getDate());
    }
}
