package hanas.dnidomatury.model;

import android.content.Context;

import hanas.dnidomatury.model.fileSupport.FileSupportedList;
import hanas.dnidomatury.model.matura.Exam;
import hanas.dnidomatury.model.task.Task;

public interface ExamSpecificList<T extends Comparable<T>>
        extends FileSupportedList<T> {

    void toFile(final Context context, final Exam exam);

    int moveAndSort(int fromPosition, boolean downTheList);
}
