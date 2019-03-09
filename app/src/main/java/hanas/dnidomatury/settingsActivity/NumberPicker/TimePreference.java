package hanas.dnidomatury.settingsActivity.NumberPicker;

import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class TimePreference extends DialogPreference {
    //private int lastHour=0;
    //private int lastMinute=0;
    private TimePicker picker = null;
    private Calendar lastCalendar;
    private DateFormat sdf = new SimpleDateFormat("H:mm", Locale.getDefault());

    public TimePreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);

        lastCalendar = Calendar.getInstance();
        lastCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Warsaw"));
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Warsaw"));
        //lastCalendar.set(Calendar.AM_PM, Calendar.AM);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        setDialogTitle("");
        return super.onCreateView(parent);
    }

    @Override
    protected View onCreateDialogView() {
        picker = new TimePicker(getContext());
        return (picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        picker.setIs24HourView(true);
        Toast.makeText(getContext(), lastCalendar.toString(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(), lastCalendar.get(Calendar.AM_PM)+" wtf", Toast.LENGTH_SHORT).show();
        picker.setCurrentHour(lastCalendar.get(Calendar.HOUR_OF_DAY));
        picker.setCurrentMinute(lastCalendar.get(Calendar.MINUTE));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            lastCalendar.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());
            lastCalendar.set(Calendar.MINUTE, picker.getCurrentMinute());
            //lastCalendar.set(Calendar.AM_PM, Calendar.AM);
            String time = sdf.format(lastCalendar.getTime());
            Toast.makeText(getContext(), time, Toast.LENGTH_SHORT).show();
            if (callChangeListener(time)) {
                persistString(time);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time = null;

        if (restoreValue) {
            if (defaultValue == null) {
                time = getPersistedString("0:00");
            } else {
                time = getPersistedString(defaultValue.toString());
                Toast.makeText(getContext(), getPersistedString(defaultValue.toString()) + "eh", Toast.LENGTH_SHORT).show();
            }
        } else {
            time = defaultValue.toString();
        }

        try {
            //lastCalendar.set(Calendar.AM_PM, Calendar.AM);
            lastCalendar.setTime(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public String getValue() {
        return sdf.format(lastCalendar.getTime());
    }
}
