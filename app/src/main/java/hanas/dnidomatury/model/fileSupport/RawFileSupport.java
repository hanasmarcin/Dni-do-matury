package hanas.dnidomatury.model.fileSupport;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public interface RawFileSupport {

    static int getRawFileId(String fileTitle, Context context) {
        return context.getResources().getIdentifier(fileTitle, "raw", context.getPackageName());
    }

    class RawFileReader<T> implements Runnable {
        private volatile T listFromFile;
        private final Context context;
        private final String fileTitle;
        private final Type objType;

        public RawFileReader(Context context, String fileTitle, Type objType) {
            this.context = context;
            this.fileTitle = fileTitle;
            this.objType = objType;
        }

        @Override
        public void run() {
            try {
                System.out.println("plik istnieeejeeee");
                InputStream inputStream = context.getResources().openRawResource(getRawFileId(fileTitle, context));
                InputStreamReader inputReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader reader = new BufferedReader(inputReader);
                String listInString = reader.readLine();
                System.out.println(listInString);
                reader.close();
                inputReader.close();
                inputStream.close();
                listFromFile = gson.fromJson(listInString, objType);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public T getValue() {
            return listFromFile;
        }
    }

    static <T> T fromRawFile(final Context context, String fileTitle, Type objType) {

        RawFileReader<T> fileReader = new RawFileReader<>(context, fileTitle, objType);
        Thread thread = new Thread(fileReader);
        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return fileReader.getValue();
    }

    Gson gson = new Gson();
}
