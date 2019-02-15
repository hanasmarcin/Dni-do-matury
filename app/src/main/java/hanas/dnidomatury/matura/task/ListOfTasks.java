package hanas.dnidomatury.matura.task;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hanas.dnidomatury.converters.GithubTypeConvertersTask;

import static hanas.dnidomatury.matura.task.Task.TaskHeader.DONE;
import static hanas.dnidomatury.matura.task.Task.TaskHeader.NOT;
import static hanas.dnidomatury.matura.task.Task.TaskHeader.TODO;

public class ListOfTasks {

    private  List<Task> listOfTasks;
    private String listInString;
    private String fileTitle;
    private long tasksCounter;

    public long getTasksCounter() { return this.tasksCounter; }

    public void incrementTasksCounter() { this.tasksCounter++; }

    public void decrementTasksCounter() { this.tasksCounter--; }


    public ListOfTasks(String maturaName, String maturaType, String maturaLevel) {
        this.listOfTasks = new ArrayList<>();
        this.fileTitle = new String(maturaName +"_"+ maturaLevel +"_"+ maturaType + "_todo_list");
        this.tasksCounter = 0;

    }

    /*public List<Task> getListOfTasks() {
        return listOfTasks;
    }*/

    public void sort() {
        Collections.sort(listOfTasks);
    }

    public Task getTask(int taskID) {
        return listOfTasks.get(taskID);
    }

    public void addTask(int taskID, Task task) {
        if (taskID>sizeOfList()) listOfTasks.add(task);
        else {
            listOfTasks.add(taskID, task);
            for(int i=taskID+1; i<sizeOfList(); i++){
                //listOfTasks.get(i).setTaskID(i);
            }
        }
        //task.setTaskID(taskID);
    }

    public void deleteTask(Task task) {
        int taskID = listOfTasks.indexOf(task);
        listOfTasks.remove(task);
    }

    public void swapTasks(int fromPosition, int toPosition) {
        Collections.swap(listOfTasks, fromPosition, toPosition);
    }


    public int sizeOfList() {
        return listOfTasks.size();
    }

    public void saveToFile(Context context) {

        listInString = GithubTypeConvertersTask.someObjectListToString(listOfTasks);

        try {
            FileOutputStream fOut = context.openFileOutput(fileTitle, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(listInString);
            osw.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

    }

    private static boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        return file != null && file.exists();
    }


    public List<Task> readFromFile(Context context) {
        try {
            FileInputStream fileInputStream;
            InputStream inputStream;
            InputStreamReader isr;


            if (fileExists(context, fileTitle)) {
                fileInputStream = context.openFileInput(fileTitle);
                isr = new InputStreamReader(fileInputStream, "utf-8");
                BufferedReader reader = new BufferedReader(isr);
                listInString = reader.readLine();
                reader.close();
                isr.close();
                listOfTasks = GithubTypeConvertersTask.stringToSomeObjectList(listInString);
            }
            else {
                //addTask(0, new Task(0, "", "", false));
                //addTask(1, new Task(1, "","", true));
                addTask(0, new Task("", "", TODO));
                addTask(1, new Task("","", DONE));
            }

        }
        catch(IOException ex){
            ex.printStackTrace();
        }


        for (Task task : listOfTasks) {
            if (!task.equals(listOfTasks.get(0)) && task.getHeader() == NOT && !task.isDone()) this.tasksCounter++;
        }

        return listOfTasks;
    }
}


