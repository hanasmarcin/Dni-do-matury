package hanas.dnidomatury.settingsActivity;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import hanas.dnidomatury.R;
import hanas.dnidomatury.converters.GithubTypeConverters;
import hanas.dnidomatury.converters.GithubTypeConvertersSettings;
import hanas.dnidomatury.converters.GithubTypeConvertersTask;
import hanas.dnidomatury.matura.task.Task;
import hanas.dnidomatury.settingsActivity.SettingsActivity.Frequency;

public  class SettingsData {

    public Calendar firstNotifDate;
    public Frequency notifFrequency;

    public SettingsData() {
        firstNotifDate = Calendar.getInstance();
        firstNotifDate.set(Calendar.HOUR_OF_DAY, 8);
        firstNotifDate.set(Calendar.MINUTE, 0);
        if ( Calendar.getInstance().compareTo(firstNotifDate) > 0 ) {
            firstNotifDate.setTimeInMillis(firstNotifDate.getTimeInMillis() + 86400000);
        }
        notifFrequency = Frequency.NEVER;
    }

    public void saveToFile(Context context) {

        String dataInString = GithubTypeConvertersSettings.someObjectListToString(this);

        try {
            FileOutputStream fOut = context.openFileOutput("settings", context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(dataInString);
            osw.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private static boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath("settings");
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }


    public static SettingsData readFromFile(Context context) {
        try {
            FileInputStream fileInputStream;
            InputStream inputStream;
            InputStreamReader isr;
            String dataInString;


            if (fileExists(context, "settings")) {
                fileInputStream = context.openFileInput("settings");
                isr = new InputStreamReader(fileInputStream, "utf-8");
                BufferedReader reader = new BufferedReader(isr);
                dataInString = reader.readLine();
                reader.close();
                isr.close();
                return GithubTypeConvertersSettings.stringToSomeObject(dataInString);
            }
            else {
                return new SettingsData();
            }

        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        return new SettingsData();
    }

}
