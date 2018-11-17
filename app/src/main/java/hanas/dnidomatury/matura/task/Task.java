package hanas.dnidomatury.matura.task;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task implements Comparable<Task>{
    //private int taskID;
    private String taskName;
    private Date taskDate;
    private String taskDateText;
    private boolean isDone;
    private boolean isDoneHeader;
    private boolean isNewHeader;

    public Task(/*int taskID, */String taskName, String taskDateText, boolean isDoneHeader, boolean isNewHeader) {
        //this.taskID = taskID;
        this.taskName = taskName;
        this.taskDateText = taskDateText;
        this.isDone = false;
        this.isDoneHeader = isDoneHeader;
        this.isNewHeader = isNewHeader;
        this.taskDate = taskDateTextToDate(taskDateText);
    }

    public static Date taskDateTextToDate(String taskDateText) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        try {
            return formatter.parse(taskDateText);
        } catch (ParseException e) {
            return new Date();
        }
    }

    //public void setTaskID(int taskID) {
    //    this.taskID = taskID;
    //}

    //public int getTaskID() {
    //    return taskID;
    //}

    public String getTaskName() {
        return taskName;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isDoneHeader() {
        return isDoneHeader;
    }

    public String getTaskDateText() {
        return taskDateText;
    }

    @Override
    public int compareTo(@NonNull Task task) {
        //if (this.taskID==0) return -1;
        if (this.isNewHeader) return -1;
        else if (task.isNewHeader) return 1;
        else if (this.isDone && !task.isDone) return 1;
        else if (!this.isDone && task.isDone) return -1;
        else if (this.isDoneHeader && task.isDone) return -1;
        else if (this.isDoneHeader && !task.isDone) return 1;
        else if (!this.isDone && task.isDoneHeader) return -1;
        else if (this.isDone && task.isDoneHeader) return 1;
        else if (this.taskDateText.equals("Brak daty") && task.taskDateText.equals("Brak daty")) return (-1)*this.taskDate.compareTo(task.taskDate);
        else return this.taskDate.compareTo(task.taskDate);
    }
}
