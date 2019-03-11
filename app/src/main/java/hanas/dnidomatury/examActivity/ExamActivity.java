package hanas.dnidomatury.examActivity;

import androidx.annotation.RequiresPermission;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.ContactsContract;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import hanas.dnidomatury.R;
import hanas.dnidomatury.examActivity.SheetList.SheetListFragment;
import hanas.dnidomatury.model.ExamSpecificList;
import hanas.dnidomatury.model.ExamsFileList;
import hanas.dnidomatury.model.matura.Exam;
import hanas.dnidomatury.examActivity.ExamInfo.ExamInfoFragment;
import hanas.dnidomatury.examActivity.TaskList.TaskListFragment;
import hanas.dnidomatury.model.matura.ExamTimer;
import hanas.dnidomatury.model.matura.SelectedExamsList;
import hanas.dnidomatury.model.sheet.Sheet;
import hanas.dnidomatury.model.sheet.SheetsList;
import hanas.dnidomatury.model.task.Task;
import hanas.dnidomatury.model.task.TasksList;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class ExamActivity extends AppCompatActivity {

    Exam mSelectedExam;
    int selectedExamPOS;
    ExamsFileList mListOfExam;
    //ConstraintLayout preDataLayout;

    //private SectionsPagerAdapter mSectionsPagerAdapter;
    //private ViewPager mViewPager;
    //private ViewPager.OnPageChangeListener viewPagerListener;
    //private TaskListFragment mTaskListFragment;
    //private ExamInfoFragment mExamInfoFragment;
    //private SheetListFragment mSheetListFragment;
    private Toolbar toolbar;
    DataViewModel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedExamPOS = bundle.getInt("selectedExamPOS");
            System.out.println("Szukamy matury" + selectedExamPOS);
            mListOfExam = SelectedExamsList.getInstance(this);
            System.out.println("Zdobywamy maturę");
            mSelectedExam = mListOfExam.get(selectedExamPOS);
            System.out.println("ustawiamy theme" + mSelectedExam.getName());
            setTheme(mSelectedExam.getStyleID(this));
            setTitle(mSelectedExam.getName());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        toolbar = findViewById(R.id.toolbar_exam);
        toolbar.setSubtitle(mSelectedExam.getType() + " " + mSelectedExam.getLevel());
        setSupportActionBar(toolbar);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), false);

        // Set up the ViewPager with the sections adapter.
        //mViewPager.setAdapter(mSectionsPagerAdapter);
        //mViewPager.setCurrentItem(1);
        //mViewPager.getAdapter().notifyDataSetChanged();
//        mViewPager = findViewById(R.id.container);
        TextView daysTimer = findViewById(R.id.days_timer_exam_activity);
        TextView hmsTimer = findViewById(R.id.hms_timer_exam_activity);

        //preDataLayout = findViewById(R.id.pre_data_view);
        data = ViewModelProviders.of(this).get(DataViewModel.class);
        TextView monthView = findViewById(R.id.info_month_pre_data);
        TextView dayView = findViewById(R.id.into_day_pre_data);
        TextView dayOfWeekView = findViewById(R.id.info_day_of_week_pre_data);
        TextView hourView = findViewById(R.id.info_hour_pre_data);

        SimpleDateFormat monthFormat = new SimpleDateFormat("LLLL", Locale.getDefault());
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat hourFormat = new SimpleDateFormat("H:mm", Locale.getDefault());
        String month = monthFormat.format(mSelectedExam.getDate().getTime());
        String dayOfWeek = dayOfWeekFormat.format(mSelectedExam.getDate().getTime());
        String hour = hourFormat.format(mSelectedExam.getDate().getTime());
        monthView.setText(month);
        //monthView.setBackgroundColor(ContextCompat.getColor(getActivity(), darkColorID));
        dayView.setText(Integer.toString(mSelectedExam.getDate().get(Calendar.DAY_OF_MONTH)));
        dayOfWeekView.setText(dayOfWeek);
        hourView.setText(hour);

        new ExamTimer().startExamTimer(this, mSelectedExam, daysTimer, hmsTimer);
    }

    @Override
    public void onAttachedToWindow() {
        long a = Calendar.getInstance().getTimeInMillis();
        long b = Calendar.getInstance().getTimeInMillis();
        System.out.println("ROBISIEEEEE " + (b - a));
        //newAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), true);

//        long c = Calendar.getInstance().getTimeInMillis();
//        System.out.println(c-b);
//        mViewPager.setAdapter(newAdapter);
//        long d = Calendar.getInstance().getTimeInMillis();
//        System.out.println(d-c);
//        preDataLayout.setVisibility(View.GONE);
//        long e = Calendar.getInstance().getTimeInMillis();
//        System.out.println(e-d);
//        mViewPager.setCurrentItem(1);
//        long f = Calendar.getInstance().getTimeInMillis();
//        System.out.println((f-e)+"KONCZYMYYYY");
//        super.onAttachedToWindow();
    }

    // For fragment's onActivityResult to work properly
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    Context context;
    SectionsPagerAdapter newAdapter;

    @Override
    protected void onResume() {
        super.onResume();


//        mDisposable.add(Observable.fromCallable(() -> {
//            Thread.sleep(5000);
//            System.out.println("SPIMY");
//            data.setFromFile(context, mSelectedExam);
//            return data;
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe((result) -> {
//
//                    mViewPager.setAdapter(newAdapter);
//                    preDataLayout.setVisibility(View.GONE);
//                    mViewPager.setCurrentItem(1);
//                }));
        new ReadDataAsyncTask(this, selectedExamPOS).execute(mSelectedExam);
    }


    static class ReadDataAsyncTask extends AsyncTask<Exam, Void, Void> {
        private WeakReference<ExamActivity> activityReference;
        int selectedExamPOS;
        DataViewModel data;

        // only retain a weak reference to the activity
        ReadDataAsyncTask(ExamActivity context, int selectedExamPOS) {
            this.activityReference = new WeakReference<>(context);
            this.selectedExamPOS = selectedExamPOS;
        }

        @Override
        protected Void doInBackground(Exam... mSelectedExams) {
            // get a reference to the activity if it is still there
            Exam mSelectedExam = mSelectedExams[0];
            ExamActivity a = activityReference.get();
            if (a == null || a.isFinishing()) return null;
            System.out.println("Odczytano dane " + mSelectedExam.getName());
            data = ViewModelProviders.of(a).get(DataViewModel.class);
            data.setFromFile(a, mSelectedExam);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            ExamActivity a = activityReference.get();
            if (a == null || a.isFinishing()) return;
            ViewPager mViewPager = a.findViewById(R.id.container);
            ConstraintLayout preDataLayout = a.findViewById(R.id.pre_data_view);
            preDataLayout.setVisibility(View.GONE);
            SectionsPagerAdapter newAdapter = new SectionsPagerAdapter(a.getSupportFragmentManager(), true, selectedExamPOS);
            mViewPager.setAdapter(newAdapter);
            mViewPager.setCurrentItem(1);
        }
    }

    //private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onDestroy() {
        //mDisposable.dispose();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //final Context context = this;
        //data.toFile(this, mSelectedExam);
        // Declare global disposable variable

// Then add the disposable returned by subscribeWith to the disposable


//        mDisposable.add(Observable.fromCallable(() -> {
//            Thread.sleep(5000);
//            System.out.println("SPIMY");
//            data.toFile(context, mSelectedExam);
//            return null;
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe((result) -> {
//                    System.out.println("SKONCZYLEM");
//                }));
        new SaveDataAsyncTask(this, mSelectedExam).execute();
    }


    static class SaveDataAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        //private WeakReference<DataViewModel> dataReference;
        private WeakReference<ExamActivity> activityReference;
        private FileOutputStream[] fileOutputStreams;
        DataViewModel data;
        private Exam exam;

        // only retain a weak reference to the activity
        SaveDataAsyncTask(ExamActivity activity, Exam exam) {
            this.activityReference = new WeakReference<>(activity);
            this.exam = exam;
            //dataReference = new WeakReference<>(data);
        }

        @Override
        protected void onPreExecute() {
            ExamActivity activity = activityReference.get();
            data = ViewModelProviders.of(activity).get(DataViewModel.class);
            fileOutputStreams = new FileOutputStream[2];
            fileOutputStreams[0] = activity.data.getSheets().getOutputStream(activity, exam);
            fileOutputStreams[1] = activity.data.getTasks().getOutputStream(activity, exam);
            //System.out.println("KONIEEC W ONPRE");
        }

        @Override
        protected Integer doInBackground(Integer... voids) {
//            ExamActivity a = activityReference.get();
//            if (a == null || a.isFinishing()) return null;
            System.out.println("SPIMY");
            data.getSheets().toFile(fileOutputStreams[0]);
            data.getTasks().toFile(fileOutputStreams[1]);
            System.out.println("ZAPISANE");

            return null;
        }
    }

    // Custom FragmentPagerAdapter that returns a fragment corresponding to one of the tabs
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        boolean isDataLoaded;
        int selectedExamPOS;

        public SectionsPagerAdapter(FragmentManager fm, boolean isDataLoaded, int selectedExamPOS) {
            super(fm);
            this.selectedExamPOS = selectedExamPOS;
            this.isDataLoaded = isDataLoaded;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            System.out.println(position);
            if (!isDataLoaded) return ExamInfoFragment.newInstance(selectedExamPOS);

            switch (position) {
                case 0: {
                    return SheetListFragment.newInstance(selectedExamPOS);
                }
                case 1: {
                    return ExamInfoFragment.newInstance(selectedExamPOS);
                }
                case 2: {
                    return TaskListFragment.newInstance(selectedExamPOS);
                }
            }
            return null;
        }


        @Override
        public int getCount() {
            System.out.println("GETITEMCOUNT " + isDataLoaded);
            return isDataLoaded ? 3 : 1;
        }


    }

}