package hanas.dnidomatury.matura.task;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static hanas.dnidomatury.matura.task.Task.TaskHeader.DONE;
import static hanas.dnidomatury.matura.task.Task.TaskHeader.TODO;

public class Task implements Comparable<Task>{
    //private int taskID;
    private String taskName;
    private Calendar taskDate;
    private String taskDateText;
    private boolean isDone;
    private TaskHeader header;

    public enum TaskHeader {NOT, TODO, DONE}

    public Task(/*int taskID, */String taskName, String taskDateText, TaskHeader header) {
        //this.taskID = taskID;
        this.taskName = taskName;
        this.taskDate = TaskDateTextToDate(taskDateText);
        this.taskDateText = taskDateText;
        this.isDone = false;
        this.header = header;
    }

    public static Calendar TaskDateTextToDate(String taskDateText) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(formatter.parse(taskDateText));
            return cal;
        } catch (ParseException e) {
            return Calendar.getInstance();
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

    public TaskHeader getHeader() { return header; }

    public String getTaskDateText() { return taskDateText; }

    @Override
    public int compareTo(@NonNull Task task) {
        //if (this.taskID==0) return -1;
        if (this.header == TODO) return -1;
        else if (task.header == TODO) return 1;
        else if (this.isDone && !task.isDone) return 1;
        else if (!this.isDone && task.isDone) return -1;
        else if (this.header == DONE && task.isDone) return -1;
        else if (this.header == DONE && !task.isDone) return 1;
        else if (!this.isDone && task.header == DONE) return -1;
        else if (this.isDone && task.header == DONE) return 1;
        else if (this.taskDateText.equals("Brak daty") && task.taskDateText.equals("Brak daty")) return (-1)*this.taskDate.compareTo(task.taskDate);
        else return this.taskDate.compareTo(task.taskDate);
    }
}
