package hanas.dnidomatury.model.matura;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ExamInfoData implements Serializable {
    private String time;
    private String room;
    private String person;
    private String extra;
    private String fileTitle;

    public ExamInfoData(String maturaName, String maturaType, String maturaLevel) {
        this.fileTitle = new String(maturaName +"_"+ maturaLevel +"_"+ maturaType + "_info");
    }

    public void set(String time, String room, String person, String extra) {
        this.time = time;
        this.room = room;
        this.person = person;
        this.extra = extra;
    }

    private ExamInfoData(String fileTitle) {
        this.fileTitle = fileTitle;
    }
    public String getTime() {
        return time;
    }

    public String getRoom() {
        return room;
    }

    public String getPerson() {
        return person;
    }

    public String getExtra() {
        return extra;
    }

    public void saveToFile(Context context) {

        try {
            FileOutputStream fileOut = context.openFileOutput(fileTitle, Context.MODE_PRIVATE);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private static boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        return file != null && file.exists();
    }


    public ExamInfoData readFromFile(Context context) {
        try {
            if (fileExists(context, fileTitle)) {
                FileInputStream fileIn = context.openFileInput(fileTitle);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);

                ExamInfoData obj = (ExamInfoData) objectIn.readObject();
                objectIn.close();
                return obj;
            }
            else {
                return new ExamInfoData(fileTitle);
            }

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return new ExamInfoData(fileTitle);
    }
}
