package hanas.dnidomatury.model.examSpecific.task;


import java.io.Serializable;
import java.util.Observable;

import hanas.dnidomatury.model.examSpecific.ExamItemsList;

import static hanas.dnidomatury.model.examSpecific.task.Task.TaskHeader.DONE;
import static hanas.dnidomatury.model.examSpecific.task.Task.TaskHeader.NOT;

public class TasksCounter extends Observable implements Serializable {
    private long counter = 0;

    public long getCounter() {
        return counter;
    }

    void setCounter(long counter) {
        this.counter = counter;
        setChanged();
        notifyObservers(counter);
    }

    void incrementCounter() {
        this.counter++;
        setChanged();
        notifyObservers(counter);
    }

    void decrementCounter() {
        this.counter--;
        setChanged();
        notifyObservers(counter);
    }

    public void updateCounter(boolean isChangedToDone) {
        if (isChangedToDone) this.counter--;
        else this.counter++;
        setChanged();
        notifyObservers(counter);
    }

    public void setCounter(ExamItemsList<Task> list) {
        counter = 0;
        for (Task task : list) {
            System.out.println("TASK COUNTER" + counter);
            if (task.getHeader().equals(NOT)) counter++;
            else if (task.getHeader().equals(DONE)) break;
        }
        setChanged();
        notifyObservers(counter);
    }
}
