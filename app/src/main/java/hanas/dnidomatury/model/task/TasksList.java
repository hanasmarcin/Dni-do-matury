package hanas.dnidomatury.model.task;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import hanas.dnidomatury.model.ExamSpecificList;
import hanas.dnidomatury.model.fileSupport.FileSupport;
import hanas.dnidomatury.model.matura.Exam;

import static hanas.dnidomatury.model.task.Task.TaskHeader.DONE;
import static hanas.dnidomatury.model.task.Task.TaskHeader.NOT;
import static hanas.dnidomatury.model.task.Task.TaskHeader.TODO;

public class TasksList extends ArrayList<Task> implements ExamSpecificList<Task> {

    private final static String FILE_SUFFIX = "taskslist";
    private transient TasksCounter counter;

    private TasksList() {
        this.add(new Task("", "", TODO));
        for (int i = 0; i < 100; i++) {
            this.add(new Task("abc", "Brak daty", NOT));
        }
        this.add(new Task("", "", DONE));
    }

    public static TasksList fromFile(Context context, Exam exam) {
        String fileTitle = FileSupport.getFileTitle(exam, FILE_SUFFIX);
        TasksList tmpList = FileSupport.fromFile(context, fileTitle);
        if (tmpList == null) tmpList = new TasksList();
        final TasksList list = tmpList;
        list.counter = exam.getTasksCounter();
        for (Task task : list)
            task.addObserver((observable, isDone) -> list.counter.updateCounter((boolean) isDone));
        return list;
    }


    @Override
    public void sort() {
        Collections.sort(this);
    }

    @Override
    public boolean add(Task task) {
        if (super.add(task)) {
            task.addObserver((observable, isDone) -> this.counter.updateCounter((boolean) isDone));
            if (!task.isDone() && counter != null) counter.incrementCounter();
            return true;
        } else return false;
    }

    @Override
    public boolean remove(Object task) {
        if (super.remove(task)) {
            if (!((Task) task).isDone() && counter != null) counter.decrementCounter();
            return true;
        } else return false;
    }

    @Override
    public void clear() {
        super.clear();
        if (counter != null) counter.setCounter(0);
    }

    @Override
    public Task get(int i) {
        return super.get(i);
    }

    @Override
    public void toFile(Context context, Exam exam) {
        toFile(context, FileSupport.getFileTitle(exam, FILE_SUFFIX));
    }
}


