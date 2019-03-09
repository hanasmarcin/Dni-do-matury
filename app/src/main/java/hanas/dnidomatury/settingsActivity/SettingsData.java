package hanas.dnidomatury.settingsActivity;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import hanas.dnidomatury.settingsActivity.SettingsFragment.Frequency;
import static hanas.dnidomatury.settingsActivity.SettingsFragment.Frequency.*;

public  class SettingsData implements Serializable {

    public boolean enabled;
    public Calendar firstNotifDate;
    public SettingsFragment.Frequency notifFrequency;
    private final static String fileTitle = "settings";

    public SettingsData(){
        enabled = false;
        notifFrequency = Frequency.DAILY;
        firstNotifDate = Calendar.getInstance();
        firstNotifDate.set(Calendar.HOUR_OF_DAY, 8);
        firstNotifDate.set(Calendar.MINUTE, 0);
        if ( Calendar.getInstance().compareTo(firstNotifDate) > 0 ) {
            firstNotifDate.setTimeInMillis(firstNotifDate.getTimeInMillis() + 86400000);
        }
    }

    public SettingsData (Calendar firstNotifDate, Frequency notifFrequency) {
        this.firstNotifDate = firstNotifDate;
        this.notifFrequency = notifFrequency;
    }

    public void saveToFile(Context context) {

        try {
            FileOutputStream fOut = context.openFileOutput(fileTitle, Context.MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(fOut);
            output.writeObject(this);
            output.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private static boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        return file != null && file.exists();
    }


    public static SettingsData readFromFile(Context context) {

            try {
                if (fileExists(context, fileTitle)) {
                    FileInputStream fileIn = context.openFileInput(fileTitle);
                    ObjectInputStream objectIn = new ObjectInputStream(fileIn);

                    SettingsData obj = (SettingsData) objectIn.readObject();

                    objectIn.close();
                    return obj;
                }
                else {
                    return new SettingsData();
                }

            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            return new SettingsData();
        }
}
