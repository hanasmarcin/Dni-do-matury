package hanas.dnidomatury.matura;

import android.content.Context;
import android.widget.Toast;

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

    //private static List<Matura> listOfMatura;
    private static String listInString;

    static {
        //listOfMatura = new ArrayList<>();
    }

    /*public static List<Matura> getListOfMatura() {
        return listOfMatura;
    }

    public static void addMatura(Matura mMatura) {
        listOfMatura.add(mMatura);
    }*/

    public static Matura findMatura(String name, String level, String type, Context context, boolean isSelected) {
        for (Matura matura : readFromFile(context, isSelected)) {
            //Toast.makeText(context, matura.getName()+name+matura.getLevel()+level+matura.getType()+type, Toast.LENGTH_SHORT).show();
            if (matura.getName().equals(name) && matura.getLevel().equals(level) && matura.getType().equals(type))
                return matura;
        }
        return null;
    }



    public static void saveToFile(Context context, List<Matura> listOfMatura) {

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


    public static List<Matura> readFromFile(Context context, boolean isSelected) {
        try {
            FileInputStream fileInputStream;
            InputStream inputStream;
            InputStreamReader isr;

            if(isSelected) {
                if (fileExists(context, "maturalist")) {
                    fileInputStream = context.openFileInput("maturalist");
                    isr = new InputStreamReader(fileInputStream, "utf-8");
                } else {
                    inputStream = context.getResources().openRawResource(R.raw.maturalist);
                    isr = new InputStreamReader(inputStream, "utf-8");
                }
            }
            else {
                inputStream = context.getResources().openRawResource(R.raw.everymatura);
                isr = new InputStreamReader(inputStream, "utf-8");
            }



            BufferedReader reader = new BufferedReader(isr);
            listInString = reader.readLine();
            reader.close();
            isr.close();
            return GithubTypeConverters.stringToSomeObjectList(listInString);
            }
        catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
}
