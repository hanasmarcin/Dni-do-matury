package hanas.dnidomatury.settingsActivity.customPreferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

public class InfoPreference extends DialogPreference {


    public InfoPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InfoPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    protected View onCreateDialogView() {
//        return new View()
//    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
    }

    public void setValue(int value) {
    }
}
