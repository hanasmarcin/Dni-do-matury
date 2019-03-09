package hanas.dnidomatury.model.matura;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

import hanas.dnidomatury.model.fileSupport.FileSupport;
import hanas.dnidomatury.model.fileSupport.RawFileSupport;
import hanas.dnidomatury.model.ExamsFileList;

public class ExamsList extends ArrayList<Exam> implements ExamsFileList {

    public ExamsList() {}

    public static ExamsList fromFile(boolean areSelected, Context context) {
        String fileTitle = getFileTitle(areSelected);
        System.out.println(fileTitle);
        if (areSelected) {
            ExamsList list = FileSupport.fromFile(context, fileTitle);
            if (list != null) return list;
        }
        System.out.println("lista byla null");
        ExamsList list = RawFileSupport.fromRawFile(context, fileTitle, ExamsList.class);
        return list;
    }

    public static String getFileTitle (boolean areSelected) {
        return areSelected ? "examlist" : "everyexam";
    }

    @Override
    public void sort() {
        Collections.sort(this);
    }

    @Override
    public void toFile(Context context, boolean areSelected) {
        toFile(context, getFileTitle(areSelected));
    }

    @Override
    public Exam findExam(String examName, String examLevel, String examType) {
        for (Exam exam : this) {
            if (exam.getName().equals(examName) && exam.getType().equals(examType) && exam.getLevel().equals(examLevel))
                return exam;
        }
        return null;
    }

    @Override
    public void swap(int fromPosition, int toPosition) {
        Collections.swap(this, fromPosition, toPosition);
    }


}
