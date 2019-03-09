package hanas.dnidomatury.model.sheet;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

import hanas.dnidomatury.model.ExamSpecificList;
import hanas.dnidomatury.model.fileSupport.FileSupport;
import hanas.dnidomatury.model.matura.Exam;

public class SheetsList extends ArrayList<Sheet> implements ExamSpecificList<Sheet> {

    private static final String FILE_SUFFIX = "sheets";

    public SheetsList() { }

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
}


