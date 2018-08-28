package hanas.dnidomatury;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class ListOfMatura {

    private static List<Matura> listOfMatura;
    private static String listInString;
    private static GithubTypeConverters converter = new GithubTypeConverters();


    public ListOfMatura() {
        this.listOfMatura = new ArrayList<>();
    }

    public List<Matura> getListOfMatura() {
        return listOfMatura;
    }

    public void addMatura(Matura mMatura) {
        listOfMatura.add(mMatura);
    }

    public void deleteNotSelected() {

        int i=0;
        while (i<listOfMatura.size()) {
            Matura tmpMatura = listOfMatura.get(i);
            if (!tmpMatura.isSelected()) listOfMatura.remove(i);
            else i++;
        }

    }



    public static void saveToFile(Context context) {

        listInString = converter.someObjectListToString(listOfMatura);

        try {
            FileOutputStream fOut = context.openFileOutput("maturalist", context.MODE_PRIVATE);
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


    public static void readFromFile(Context context) {
        try {
            FileInputStream fileInputStream;
            InputStream inputStream;
            InputStreamReader isr;

            if (fileExists(context, "maturalist")) {
                fileInputStream = context.openFileInput("maturalist");
                isr = new InputStreamReader(fileInputStream, "utf-8");
            }
            else {
                inputStream  = context.getResources().openRawResource(R.raw.maturalist);
                isr = new InputStreamReader(inputStream, "utf-8");
            }



            BufferedReader reader = new BufferedReader(isr);
            listInString = reader.readLine();
            reader.close();
            isr.close();
            listOfMatura = converter.stringToSomeObjectList(listInString);
            }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
