package hanas.dnidomatury.matura;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Matura {

    //private int maturaID;
    private String name;
    private String level;
    private String type;
    private Calendar date;
    private String primaryColor;
    private String darkColor;
    private long tasksCounter = 0;

    public long getTasksCounter() {
        return tasksCounter;
    }

    public void setTasksCounter(long tasksCounter) {
        this.tasksCounter = tasksCounter;
    }

    /*public void setTasksCounter(int tasksCounter) {
        this.tasksCounter = tasksCounter;
    }*/

    public Matura(String name, String level, String type, String dateText, String primaryColor, String darkColor) {
        this.name = name;
        this.level = level;
        this.type = type;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date date = sdf.parse(dateText);
            this.date = Calendar.getInstance();
            this.date.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.date = date;
        this.primaryColor = primaryColor;
        this.darkColor = darkColor;
        //this.tasksCounter = 0;
    }

    public int getPrimaryColorID(Context context) {
        return context.getResources().getIdentifier(this.primaryColor, "color", context.getPackageName());
    }

    public int getDarkColorID(Context context) {
        return context.getResources().getIdentifier(this.darkColor, "color", context.getPackageName());
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

    public void setDate(Calendar date) {
        this.date = date;
    }
}
