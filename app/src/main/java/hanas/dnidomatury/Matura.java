package hanas.dnidomatury;

import android.content.Context;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Matura {

    private String name;
    private String level;
    private String type;
    private Calendar date;
    private boolean isSelected;
    private String primaryColor;
    private String darkColor;
    private int maturaID;
    private int tasksCounter;

    public int getTasksCounter() {
        return tasksCounter;
    }

    public void setTasksCounter(int tasksCounter) {
        this.tasksCounter = tasksCounter;
    }

    public Matura(String name, String level, String type, Calendar date, boolean isSelected, String primaryColor, String darkColor, int maturaID) {
        this.maturaID = maturaID;
        this.name = name;
        this.level = level;
        this.type = type;
        this.date = date;
        this.isSelected = isSelected;
        this.primaryColor = primaryColor;
        this.darkColor = darkColor;
        this.tasksCounter = 0;
    }

    public int getMaturaID() { return maturaID; }

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
