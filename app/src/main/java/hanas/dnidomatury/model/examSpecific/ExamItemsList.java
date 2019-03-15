package hanas.dnidomatury.model.examSpecific;


import hanas.dnidomatury.model.fileSupport.FileSupportedList;

public interface ExamItemsList<T extends ExamItem>
        extends FileSupportedList<T> {

    int moveAndSort(int fromPosition, boolean downTheList);
}
