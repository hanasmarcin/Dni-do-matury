package hanas.dnidomatury.model.exam;

import android.content.Context;

import java.util.List;

import hanas.dnidomatury.model.fileSupport.FileSupportedList;

public interface ExamsList extends FileSupportedList<Exam>, List<Exam> {

    //Exam remove(int index);
    boolean add(Exam newElement);
    void toFile(final Context context, final boolean areSelected);
    Exam findExam(String examName, String examLevel, String examType);
    void swap(int fromPosition, int toPosition);
}
