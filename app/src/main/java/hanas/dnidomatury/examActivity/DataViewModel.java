package hanas.dnidomatury.examActivity;

import android.content.Context;

import java.util.Calendar;

import androidx.lifecycle.ViewModel;
import hanas.dnidomatury.model.ExamSpecificList;
import hanas.dnidomatury.model.matura.Exam;
import hanas.dnidomatury.model.sheet.Sheet;
import hanas.dnidomatury.model.sheet.SheetsList;
import hanas.dnidomatury.model.task.Task;
import hanas.dnidomatury.model.task.TasksList;

public class DataViewModel extends ViewModel {

    ExamSpecificList<Task> tasks;
    ExamSpecificList<Sheet> sheets;

    public ExamSpecificList<Task> getTasks() {
        return tasks;
    }

    public ExamSpecificList<Sheet> getSheets() {
        return sheets;
    }

    public void setTasksFromFile(Context context, Exam exam) {
        this.tasks = TasksList.fromFile(context, exam);
    }

    public void setSheetsFromFile(Context context, Exam exam) {
        this.sheets = SheetsList.fromFile(context, exam);
    }

    public void setFromFile(Context context, Exam exam) {
        this.tasks = TasksList.fromFile(context, exam);
        this.sheets = SheetsList.fromFile(context, exam);
    }

    public void toFile(Context context, Exam exam) {
        this.tasks.toFile(context, exam);
        this.sheets.toFile(context, exam);
    }
}
