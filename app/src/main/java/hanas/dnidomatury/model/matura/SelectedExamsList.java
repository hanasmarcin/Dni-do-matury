package hanas.dnidomatury.model.matura;

import android.content.Context;

import java.io.Serializable;

import hanas.dnidomatury.model.ExamsFileList;
import hanas.dnidomatury.model.matura.ExamsList;

public class SelectedExamsList implements Serializable {

    private volatile static ExamsFileList list;

    private SelectedExamsList(Context context) {}

    public static synchronized ExamsFileList getInstance(Context context) {
        if (list == null) {
            list = ExamsList.fromFile(true, context);
        }
        return list;
    }
}
