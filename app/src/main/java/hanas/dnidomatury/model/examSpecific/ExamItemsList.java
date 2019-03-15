package hanas.dnidomatury.model.examSpecific;

import android.content.Context;

import hanas.dnidomatury.model.fileSupport.FileSupportedList;
import hanas.dnidomatury.model.exam.Exam;

public interface ExamItemsList<T extends ExamItem>
        extends FileSupportedList<T> {

    int moveAndSort(int fromPosition, boolean downTheList);
}
