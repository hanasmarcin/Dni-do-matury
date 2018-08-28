package hanas.dnidomatury;

public class Task {
    int taskID;
    String taskName;
    String taskDateText;
    boolean isDone;
    boolean isDoneHeader;

    public boolean isDoneHeader() {
        return isDoneHeader;
    }

    public Task(int taskID, String taskName, String taskDateText, boolean isDoneHeader) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.taskDateText = taskDateText;
        this.isDone = false;
        this.isDoneHeader = isDoneHeader;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getTaskID() {
        return taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
