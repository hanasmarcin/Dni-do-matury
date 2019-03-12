package hanas.dnidomatury.examActivity.recyclerViews;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import hanas.dnidomatury.examActivity.taskList.TaskAdapter;
import hanas.dnidomatury.examActivity.sheetList.SheetAdapter;
import hanas.dnidomatury.model.examSpecific.ExamItemsList;
import hanas.dnidomatury.model.examSpecific.ExamItem;
import hanas.dnidomatury.model.examSpecific.sheet.SheetsList;
import hanas.dnidomatury.model.examSpecific.task.TasksList;

public class SetRecyclerView<T extends ExamItem> extends AsyncTask<RecyclerView, Void, RecyclerView> {

    private WeakReference<Fragment> fragmentReference;
    private WeakReference<CoordinatorLayout> coordinatorReference;
    private ExamItemsList<T> tasks;

    public SetRecyclerView(Fragment fragment, CoordinatorLayout coordinator, ExamItemsList<T> tasks) {
        this.fragmentReference = new WeakReference<>(fragment);
        this.coordinatorReference = new WeakReference<>(coordinator);
        this.tasks = tasks;
    }

    @Override
    protected RecyclerView doInBackground(RecyclerView... recyclerViews) {
        RecyclerView recyclerView = recyclerViews[0];
        Fragment fragment = fragmentReference.get();
        if (fragment == null) return null;

        CustomLayoutManager customLayoutManager = new CustomLayoutManager(fragment.getActivity());
        recyclerView.setLayoutManager(customLayoutManager);
        return recyclerView;
    }

    @Override
    protected void onPostExecute(RecyclerView recyclerView) {
        // Get fragment and view and check, whether they are available
        // If they aren't, don't do tasks
        Fragment fragment = fragmentReference.get();
        CoordinatorLayout coordinator = coordinatorReference.get();
        if (fragment == null || coordinator == null) return;

        // Set recyclerView's properties, adapter and layoutManager
        //recyclerView.setVisibility(View.VISIBLE);
        RecyclerView.Adapter adapter = tasks instanceof TasksList ?
                new TaskAdapter(fragment, (TasksList) tasks) : new SheetAdapter(fragment, (SheetsList) tasks, coordinator);

        recyclerView.setAdapter(adapter);
    }
}