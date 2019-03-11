package hanas.dnidomatury.model;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import hanas.dnidomatury.model.fileSupport.FileSupportedList;
import hanas.dnidomatury.model.matura.Exam;

public interface ExamSpecificList<T extends Comparable<T>>
        extends FileSupportedList<T> {

    void readFromFile(Context context, final Exam exam);
    void toFile(final Context context, final Exam exam);
    //FileOutputStream getOutputStream(final Context context, final Exam exam);
    //void toFile(final FileOutputStream fileOut);
    int moveAndSort(int fromPosition, boolean downTheList);
}
