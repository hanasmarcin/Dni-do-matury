package hanas.dnidomatury.model.examSpecific.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Observable;

import androidx.annotation.NonNull;
import hanas.dnidomatury.model.examSpecific.ExamItem;

import static hanas.dnidomatury.model.examSpecific.task.Task.TaskHeader.DONE;
import static hanas.dnidomatury.model.examSpecific.task.Task.TaskHeader.TODO;

public class Task extends Observable implements ExamItem {
    private String taskName;
    private Calendar taskDate;
    private String taskDateText;
    private boolean isDone;
    private TaskHeader header;

    public void update(String taskName, String taskDateText) {
        this.taskName = taskName;
        this.taskDateText = taskDateText;
        this.taskDate = taskDateTextToDate(taskDateText);
    }

    public enum TaskHeader {NOT, TODO, DONE}

    public Task(String taskName, String taskDateText, TaskHeader header) {
        this.taskName = taskName;
        this.taskDate = taskDateTextToDate(taskDateText);
        this.taskDateText = taskDateText;
        this.isDone = false;
        this.header = header;
    }

    public static Calendar taskDateTextToDate(String taskDateText) {
        SimpleDateFormat formatter = new SimpleDateFormat("d.MM.yyyy", Locale.ENGLISH);
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(formatter.parse(taskDateText));
            return cal;
        } catch (ParseException e) {
            return Calendar.getInstance();
        }
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
        setChanged();
        notifyObservers(done);
    }

    public TaskHeader getHeader() { return header; }

    public String getTaskDateText() { return taskDateText; }

    @Override
    public int compareTo(@NonNull Object obj) {
        Task task = (Task) obj;
        if (this.header == TODO) return -1;
        else if (task.header == TODO) return 1;
        else if (this.isDone && !task.isDone) return 1;
        else if (!this.isDone && task.isDone) return -1;
        else if (this.header == DONE && task.isDone) return -1;
        else if (this.header == DONE) return 1;
        else if (!this.isDone && task.header == DONE) return -1;
        else if (this.isDone && task.header == DONE) return 1;
        else if (this.taskDateText.equals("Brak daty") && task.taskDateText.equals("Brak daty")) return (-1)*this.taskDate.compareTo(task.taskDate);
        else return this.taskDate.compareTo(task.taskDate);
    }
}
