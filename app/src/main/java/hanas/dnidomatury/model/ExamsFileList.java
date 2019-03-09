package hanas.dnidomatury.model;

import android.content.Context;

import hanas.dnidomatury.model.fileSupport.FileSupportedList;
import hanas.dnidomatury.model.matura.Exam;

public interface ExamsFileList extends FileSupportedList<Exam> {

    void toFile(final Context context, final boolean areSelected);
    Exam findExam(String examName, String examLevel, String examType);
    void swap(int fromPosition, int toPosition);
}
