package hanas.dnidomatury.model.task;

import android.content.Context;

import java.io.Serializable;
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

public class TasksList extends ArrayList<Task> implements ExamSpecificList<Task>, Serializable {

    private final static String FILE_SUFFIX = "taskslist";
    private transient TasksCounter counter;

    private TasksList() {
        this.add(new Task("", "", TODO));
        for (int i = 0; i < 30; i++) {
            System.out.println(i + " wtf");
            this.add(new Task("task nr. " + i, "Brak daty", NOT));
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
    public int moveAndSort(int fromPosition, boolean startDownTheList) {
        Task element = this.remove(fromPosition);
        int toPosition;
        if (startDownTheList) {
            for (toPosition = fromPosition; toPosition < size(); toPosition++)
                if (element.compareTo(get(toPosition)) <= 0) break;
            if (toPosition == fromPosition)
                for (toPosition = fromPosition - 1; toPosition > 0; toPosition--)
                    if (element.compareTo(get(toPosition)) >= 0) {
                        toPosition++;
                        break;
                    }

        } else {
            for (toPosition = fromPosition - 1; toPosition > 1; toPosition--)
                if (element.compareTo(get(toPosition)) >= 0) {
                    toPosition++;
                    break;
                }
            if (toPosition == fromPosition)
                for (toPosition = fromPosition; toPosition < size(); toPosition++)
                    if (element.compareTo(get(toPosition)) <= 0) break;

        }
        if (toPosition == 0) toPosition++;
        add(toPosition, element);
        return toPosition;
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


