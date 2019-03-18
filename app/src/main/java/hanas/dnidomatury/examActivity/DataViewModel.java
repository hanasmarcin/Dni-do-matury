package hanas.dnidomatury.examActivity;

import android.content.Context;

import java.util.Observable;

import androidx.annotation.ColorRes;
import androidx.lifecycle.ViewModel;
import hanas.dnidomatury.model.exam.ExamAdditionalInfo;
import hanas.dnidomatury.model.examSpecific.ExamItemsList;
import hanas.dnidomatury.model.exam.Exam;
import hanas.dnidomatury.model.examSpecific.sheet.Sheet;
import hanas.dnidomatury.model.examSpecific.sheet.SheetsList;
import hanas.dnidomatury.model.examSpecific.task.Task;
import hanas.dnidomatury.model.examSpecific.task.TasksList;

public class DataViewModel extends ViewModel {

    private int colorID;
    private ExamItemsList<Task> tasks;
    private ExamItemsList<Sheet> sheets;
    private ExamAdditionalInfo info;
    public final ViewPagerLoadingState loadingState = new ViewPagerLoadingState();

    @ColorRes
    public int getColorID() {
        return colorID;
    }

    void setColorID(@ColorRes int colorID) {
        this.colorID = colorID;
    }

    public static class ViewPagerLoadingState extends Observable {
        boolean isTaskListFragmentLoaded = false;
        boolean isSheetListFragmentLoaded = false;

        public void setTaskListFragmentLoaded(boolean taskListFragmentLoaded) {
            isTaskListFragmentLoaded = taskListFragmentLoaded;
            boolean isViewPagerLoaded = isTaskListFragmentLoaded && isSheetListFragmentLoaded;
            setChanged();
            notifyObservers(isViewPagerLoaded);
        }

        public void setSheetListFragmentLoaded(boolean sheetListFragmentLoaded) {
            isSheetListFragmentLoaded = sheetListFragmentLoaded;
            boolean isViewPagerLoaded = isTaskListFragmentLoaded && isSheetListFragmentLoaded;
            setChanged();
            notifyObservers(isViewPagerLoaded);
        }

    }

    public ExamItemsList<Task> getTasks() {
        return tasks;
    }

    public ExamItemsList<Sheet> getSheets() {
        return sheets;
    }

    public ExamAdditionalInfo getInfo() {
        return info;
    }

    void setFromFile(Context context, Exam exam) {
        this.tasks = TasksList.fromFile(context, exam);
        this.sheets = SheetsList.fromFile(context, exam);
        this.info = ExamAdditionalInfo.fromFile(context, exam);
    }
}
