package hanas.dnidomatury.model;

import android.content.Context;

import hanas.dnidomatury.model.fileSupport.FileSupportedList;
import hanas.dnidomatury.model.matura.Exam;

public interface ExamSpecificList<T> extends FileSupportedList<T> {

    void toFile(final Context context, final Exam exam);
}
