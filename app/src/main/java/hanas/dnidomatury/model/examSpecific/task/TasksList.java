package hanas.dnidomatury.model.examSpecific.task;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import hanas.dnidomatury.model.examSpecific.ExamItemsList;
import hanas.dnidomatury.model.fileSupport.FileSupported;
import hanas.dnidomatury.model.exam.Exam;

import static hanas.dnidomatury.model.examSpecific.task.Task.TaskHeader.DONE;
import static hanas.dnidomatury.model.examSpecific.task.Task.TaskHeader.NOT;
import static hanas.dnidomatury.model.examSpecific.task.Task.TaskHeader.TODO;

public class TasksList extends ArrayList<Task> implements ExamItemsList<Task>, Serializable {

    public final static String FILE_SUFFIX = "taskslist";
    private transient TasksCounter counter;

    private TasksList() {
        this.add(new Task("", "", TODO));
        this.add(new Task("", "", DONE));
    }

    @Override
    public String getFileSuffix() {
        return FILE_SUFFIX;
    }

    public static ExamItemsList<Task> fromFile(Context context, Exam exam) {
        String fileTitle = FileSupported.getFileTitle(exam, FILE_SUFFIX);
        TasksList tmpList = FileSupported.fromFile(context, fileTitle);
        if (tmpList == null) tmpList = new TasksList();
        tmpList.counter = exam.getTasksCounter();

        final TasksList list = tmpList;
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
        Task element = super.remove(fromPosition);
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
        super.add(toPosition, element);
        return toPosition;
    }

    @Override
    public void add(int index, Task task) {
        super.add(index, task);
        task.addObserver((observable, isDone) -> this.counter.updateCounter((boolean) isDone));
        if (!task.isDone() && counter != null) counter.incrementCounter();
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

}


