package hanas.dnidomatury.model.sheet;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import hanas.dnidomatury.model.ExamSpecificList;
import hanas.dnidomatury.model.fileSupport.FileSupport;
import hanas.dnidomatury.model.matura.Exam;
import hanas.dnidomatury.model.task.Task;

import static hanas.dnidomatury.model.task.Task.TaskHeader.NOT;

public class SheetsList extends ArrayList<Sheet> implements ExamSpecificList<Sheet>, Serializable {

    private static final String FILE_SUFFIX = "sheets";

    public SheetsList() {
        for (int i = 0; i < 30; i++) {
            this.add(new Sheet("sheet nr. "+i, "Brak daty", i, 50));
        }
    }

    public static SheetsList fromFile(Context context, Exam exam) {
        String fileTitle = FileSupport.getFileTitle(exam, FILE_SUFFIX);
        SheetsList list = FileSupport.fromFile(context, fileTitle);
        if (list != null) return list;
        else return new SheetsList();
    }

    @Override
    public void sort() {
        Collections.sort(this);
    }

    @Override
    public boolean add(Sheet sheet) {
        return super.add(sheet);
    }

    @Override
    public boolean remove(Object sheet) {
        return super.remove(sheet);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public Sheet get(int i) {
        return super.get(i);
    }

    @Override
    public void toFile(Context context, Exam exam) {
        toFile(context, FileSupport.getFileTitle(exam, FILE_SUFFIX));
    }

    @Override
    public int moveAndSort(int fromPosition, boolean downTheList) {
        Sheet element = this.remove(fromPosition);
        int toPosition;
        for (toPosition = fromPosition; toPosition < size(); toPosition++) {
            if (element.compareTo(get(toPosition)) <= 0) break;
        }
        add(toPosition, element);
        return toPosition;
    }
}


