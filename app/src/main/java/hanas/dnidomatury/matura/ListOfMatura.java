package hanas.dnidomatury.matura;

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

import hanas.dnidomatury.converters.GithubTypeConverters;
import hanas.dnidomatury.R;

public class ListOfMatura {

    private static List<Matura> listOfMatura;
    private static String listInString;

    static {
        listOfMatura = new ArrayList<>();
    }

    public static List<Matura> getListOfMatura() {
        return listOfMatura;
    }

    public static void addMatura(Matura mMatura) {
        listOfMatura.add(mMatura);
    }

    public static void deleteNotSelected() {

        int i=0;
        while (i<listOfMatura.size()) {
            Matura tmpMatura = listOfMatura.get(i);
            if (!tmpMatura.isSelected()) listOfMatura.remove(i);
            else i++;
        }

    }



    public static void saveToFile(Context context) {

        listInString = GithubTypeConverters.someObjectListToString(listOfMatura);

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
            listOfMatura = GithubTypeConverters.stringToSomeObjectList(listInString);
            }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
