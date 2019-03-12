package hanas.dnidomatury.model.examSpecific;

import android.content.Context;

import hanas.dnidomatury.model.fileSupport.FileSupportedList;
import hanas.dnidomatury.model.exam.Exam;

public interface ExamItemsList<T extends ExamItem>
        extends FileSupportedList<T> {

    void toFile(final Context context, final Exam exam);
    int moveAndSort(int fromPosition, boolean downTheList);
}
