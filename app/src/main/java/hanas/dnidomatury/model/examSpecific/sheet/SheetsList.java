package hanas.dnidomatury.model.examSpecific.sheet;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import hanas.dnidomatury.model.examSpecific.ExamItemsList;
import hanas.dnidomatury.model.fileSupport.FileSupported;
import hanas.dnidomatury.model.exam.Exam;

public class SheetsList extends ArrayList<Sheet> implements ExamItemsList<Sheet>, Serializable {

    public static final String FILE_SUFFIX = "sheets";
    private transient SheetsAverage average;

    private SheetsList() {
    }

    @Override
    public String getFileSuffix() {
        return FILE_SUFFIX;
    }

    public static ExamItemsList<Sheet> fromFile(Context context, Exam exam) {
        String fileTitle = FileSupported.getFileTitle(exam, FILE_SUFFIX);
        SheetsList tmpList = FileSupported.fromFile(context, fileTitle);
        if (tmpList == null) tmpList = new SheetsList();
        tmpList.average = exam.getSheetsAverage();

        final SheetsList list = tmpList;
        for (Sheet sheet : list)
            sheet.addObserver((observable, percentages) -> list.average.updateAverage((double[]) percentages));
        return list;
    }

    @Override
    public void sort() {
        Collections.sort(this);
    }

    @Override
    public Sheet remove(int index) {
        Sheet sheet = super.remove(index);
        average.subtract(sheet.getPoints(), sheet.getMaxPoints());
        return sheet;
    }

    @Override
    public boolean remove(Object sheet) {
        if (super.remove(sheet)) {
            average.subtract(((Sheet) sheet).getPoints(), ((Sheet) sheet).getMaxPoints());
            return true;
        } else return false;
    }

    @Override
    public void add(int index, Sheet sheet) {
        super.add(index, sheet);
        sheet.addObserver((observable, percentages) -> this.average.updateAverage((double[]) percentages));
        average.add(sheet.getPoints(), sheet.getMaxPoints());
    }

    @Override
    public void clear() {
        super.clear();
        average.resetAverage();
    }

    @Override
    public Sheet get(int i) {
        return super.get(i);
    }

    @Override
    public int moveAndSort(int fromPosition, boolean downTheList) {
        Sheet element = super.remove(fromPosition);
        int toPosition;
        for (toPosition = fromPosition; toPosition < size(); toPosition++) {
            if (element.compareTo(get(toPosition)) <= 0) break;
        }
        super.add(toPosition, element);
        return toPosition;
    }
}


