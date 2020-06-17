package hanas.dnidomatury.model.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


@Entity(tableName = "exams")
public class ExamDB {
    @PrimaryKey
    @NonNull private Long id;
    private String name;
    private String level;
    private String type;

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setTasksCounter(Integer tasksCounter) {
        this.tasksCounter = tasksCounter;
    }

    public void setSheetsAverage(Double sheetsAverage) {
        this.sheetsAverage = sheetsAverage;
    }

    public void setSelectedOrder(Integer selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    private Long timestamp;
    private String primaryColor;
    private Integer tasksCounter;
    private Double sheetsAverage;
    private Integer selectedOrder;

    @NonNull
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public String getType() {
        return type;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public Integer getTasksCounter() {
        return tasksCounter;
    }

    public Double getSheetsAverage() {
        return sheetsAverage;
    }

    public Integer getSelectedOrder() {
        return selectedOrder;
    }

    @Ignore
    private final transient static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    public ExamDB(@NonNull Long id, String name, String level, String type, Long timestamp, String primaryColor, Integer tasksCounter, Double sheetsAverage, Integer selectedOrder) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.type = type;
        this.timestamp = timestamp;
        this.primaryColor = primaryColor;
        this.tasksCounter = tasksCounter;
        this.sheetsAverage = sheetsAverage;
        this.selectedOrder = selectedOrder;
    }

    @Ignore
    public ExamDB(String name, String level, String type, String dateText, String primaryColor) {
        this.name = name;
        if (name.contains("Język")) {
            this.level = level.substring(0, level.length() - 1) + "y";
            this.type = type.substring(0, type.length() - 1) + "y";
        } else {
            this.level = level;
            this.type = type;
        }
        this.primaryColor = primaryColor;
        try {
            Date date = sdf.parse(dateText);
            this.timestamp = date.getTime();
        } catch (ParseException ex) {
            this.timestamp = Calendar.getInstance().getTimeInMillis();
        }
    }

    @Ignore
    public int getPrimaryColorID(Context context) {
        return context.getResources().getIdentifier(this.primaryColor, "color", context.getPackageName());
    }

    @Ignore
    public int getDarkColorID(Context context) {
        return context.getResources().getIdentifier(this.primaryColor + "Dark", "color", context.getPackageName());
    }

    @Ignore
    public void setColorScheme(String color) {
        this.primaryColor = color;
    }

    @Ignore
    public Calendar getDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal;
    }

    @Ignore
    public void setDate(String dateInString) {
        try {
            timestamp = sdf.parse(dateInString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Ignore
    public void setDate(Calendar date) {
        timestamp = date.getTimeInMillis();
    }

    @Ignore
    public int getStyleID(Context context) {
        return context.getResources().getIdentifier(this.primaryColor, "style", context.getPackageName());
    }

    @Ignore
    public int compareTo(@NonNull ExamDB exam) {
        return this.getDate().compareTo(exam.getDate());
    }

    @Ignore
    public void setNewTasksCounter() {
        this.tasksCounter = 0;
    }

    @Ignore
    public void setNewSheetsAverage() {
        this.sheetsAverage = (double) 0;
    }
}
