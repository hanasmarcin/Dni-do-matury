package hanas.dnidomatury.examActivity.recyclerViews;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class CustomLayoutManager extends LinearLayoutManager {

    public CustomLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

}