package hanas.dnidomatury.model.exam;

import android.content.Context;

import hanas.dnidomatury.model.fileSupport.FileSupportedList;
import hanas.dnidomatury.model.exam.Exam;

public interface ExamsList extends FileSupportedList<Exam> {

    void toFile(final Context context, final boolean areSelected);
    Exam findExam(String examName, String examLevel, String examType);
    void swap(int fromPosition, int toPosition);
}
