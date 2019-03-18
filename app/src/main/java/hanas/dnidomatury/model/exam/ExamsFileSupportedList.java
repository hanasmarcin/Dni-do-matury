package hanas.dnidomatury.model.exam;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

import hanas.dnidomatury.model.fileSupport.FileSupported;
import hanas.dnidomatury.model.fileSupport.RawFileSupported;

public class ExamsFileSupportedList extends ArrayList<Exam> implements ExamsList {

    private ExamsFileSupportedList() {}

    public static ExamsFileSupportedList fromFile(boolean areSelected, Context context) {
        String fileTitle = getFileTitle(areSelected);
        if (areSelected) {
            ExamsFileSupportedList list = FileSupported.fromFile(context, fileTitle);
            if (list != null) return list;
        }
        return RawFileSupported.fromRawFile(context, fileTitle, ExamsFileSupportedList.class);
    }

    private static String getFileTitle(boolean areSelected) {
        return areSelected ? "examlist" : "everyexam";
    }

    public static ExamsList newObject() {
        return new ExamsFileSupportedList();
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
        String levelSubstr = examLevel.substring(0, examLevel.length()-1);
        String typeSubstr = examType.substring(0, examType.length()-1);
        for (Exam exam : this) {
            if (exam.getName().equals(examName) && exam.getType().contains(typeSubstr) && exam.getLevel().contains(levelSubstr))
                return exam;
        }
        return null;
    }


    @Override
    public void swap(int fromPosition, int toPosition) {
        Collections.swap(this, fromPosition, toPosition);
    }


    @Override
    public String getFileSuffix() {
        return "";
    }
}
