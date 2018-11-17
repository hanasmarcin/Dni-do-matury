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

public class ListOfTasks {

    private  List<Task> listOfTasks;
    private String listInString;
    private String fileTitle;

    public ListOfTasks(int maturaID, boolean isDone) {
        this.listOfTasks = new ArrayList<>();
        this.fileTitle = new String("matura_" +  maturaID + "_todo_list_isDone_" + isDone);

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


    public int sizeOfList() {
        return listOfTasks.size();
    }

    public void saveToFile(Context context) {

        listInString = GithubTypeConvertersTask.someObjectListToString(listOfTasks);

        try {
            FileOutputStream fOut = context.openFileOutput(fileTitle, context.MODE_PRIVATE);
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
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
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
                addTask(0, new Task("", "", false, true));
                addTask(1, new Task("","", true,false));
            }

        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        return listOfTasks;
    }
}


