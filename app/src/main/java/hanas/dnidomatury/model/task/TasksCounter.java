package hanas.dnidomatury.model.task;


import java.io.Serializable;
import java.util.Observable;

import static hanas.dnidomatury.model.task.Task.TaskHeader.*;

public class TasksCounter extends Observable implements Serializable {
    private long counter = 0;

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
        setChanged();
        notifyObservers(counter);
    }

    public void incrementCounter() {
        this.counter++;
        setChanged();
        notifyObservers(counter);
    }

    public void decrementCounter() {
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

    public void setCounter(TasksList list) {
        counter = 0;
        for (Task task : list) {
            if (task.getHeader().equals(NOT)) counter++;
            else if (task.getHeader().equals(DONE)) break;
        }
        setChanged();
        notifyObservers(counter);
    }
}
