package hanas.dnidomatury.examActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import hanas.dnidomatury.R;
import hanas.dnidomatury.examActivity.examInfo.ExamInfoFragment;
import hanas.dnidomatury.examActivity.sheetList.SheetListFragment;
import hanas.dnidomatury.model.exam.ExamsList;
import hanas.dnidomatury.model.exam.Exam;
import hanas.dnidomatury.examActivity.taskList.TaskListFragment;
import hanas.dnidomatury.model.exam.ExamTimer;
import hanas.dnidomatury.model.exam.SelectedExamsList;


public class ExamActivity extends AppCompatActivity {

    private Exam mSelectedExam;
    private int selectedExamPOS;

    private DataViewModel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get data from previous activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedExamPOS = bundle.getInt("selectedExamPOS");
            ExamsList listOfExam = SelectedExamsList.getInstance(this);
            mSelectedExam = listOfExam.get(selectedExamPOS);
        } else return;


        // Set activity view's properties
        setTheme(mSelectedExam.getStyleID(this));
        setTitle(mSelectedExam.getName());
        setContentView(R.layout.activity_exam);
        Toolbar toolbar = findViewById(R.id.toolbar_exam);
        toolbar.setSubtitle(mSelectedExam.getType() + " " + mSelectedExam.getLevel());
        setSupportActionBar(toolbar);

        // Get data's viewModel, which is an object, that is available for this activity and it's fragments
        // and then read exam's data from file to the viewModel using AsyncTask
        data = ViewModelProviders.of(this).get(DataViewModel.class);
        data.setColorID(mSelectedExam.getDarkColorID(this));
        new ReadDataAsyncTask(this, selectedExamPOS).execute(data);

        // Set activity date to activity's views
        setDatesOnView();
    }

    private void setDatesOnView() {
        TextView daysTimer = findViewById(R.id.days_timer_exam_activity);
        TextView daysTitle = findViewById(R.id.days_title_exam);
        TextView hmsTimer = findViewById(R.id.hms_timer_exam_activity);
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
        dayView.setText(String.format(Locale.getDefault(), "%d", mSelectedExam.getDate().get(Calendar.DAY_OF_MONTH)));
        dayOfWeekView.setText(dayOfWeek);
        hourView.setText(hour);

        new ExamTimer().startExamTimer(mSelectedExam, daysTimer, hmsTimer);

    }

    // For fragment's onActivityResult to work properly
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    static class ReadDataAsyncTask extends AsyncTask<DataViewModel, Void, SectionsPagerAdapter> {
        private final WeakReference<ExamActivity> activityReference;
        final int selectedExamPOS;

        // Only retain a weak reference to the activity
        ReadDataAsyncTask(ExamActivity context, int selectedExamPOS) {
            this.activityReference = new WeakReference<>(context);
            this.selectedExamPOS = selectedExamPOS;
        }

        @Override
        protected void onPreExecute() {
            ExamActivity a = activityReference.get();
            if (a == null || a.isFinishing()) return;
            ViewPager mViewPager = a.findViewById(R.id.container);
            ConstraintLayout preDataLayout = a.findViewById(R.id.pre_data_view);
            preDataLayout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.INVISIBLE);
        }

        @Override
        protected SectionsPagerAdapter doInBackground(DataViewModel... dataViewModels) {
            // get a reference to the activity if it is still there
            DataViewModel data = dataViewModels[0];
            ExamActivity a = activityReference.get();
            if (a == null || a.isFinishing()) return null;

            Exam selectedExam = SelectedExamsList.getInstance(a).get(selectedExamPOS);
            data.setFromFile(a, selectedExam);
            ViewPager mViewPager = a.findViewById(R.id.container);
            data.loadingState.addObserver((observable, isViewPagerLoaded) -> {
                if ((boolean) isViewPagerLoaded) mViewPager.setVisibility(View.VISIBLE);
                ConstraintLayout preDataLayout = a.findViewById(R.id.pre_data_view);
                preDataLayout.setVisibility(View.GONE);
            });
            SectionsPagerAdapter newAdapter = new SectionsPagerAdapter(a.getSupportFragmentManager(), selectedExamPOS);
            return newAdapter;
        }

        @Override
        protected void onPostExecute(SectionsPagerAdapter newAdapter) {
            //super.onPostExecute(aVoid);
            ExamActivity a = activityReference.get();
            if (a == null || a.isFinishing()) return;
            ViewPager mViewPager = a.findViewById(R.id.container);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    TextView daysTimer = a.findViewById(R.id.days_timer_exam_activity);
                    TextView daysTitle = a.findViewById(R.id.days_title_exam);
                    TextView hmsTimer = a.findViewById(R.id.hms_timer_exam_activity);
                    TextView listHeader = a.findViewById(R.id.list_header_exam);
                    switch (position) {
                        case 0 : {
                            daysTimer.setVisibility(View.GONE);
                            daysTitle.setVisibility(View.GONE);
                            hmsTimer.setVisibility(View.GONE);
                            listHeader.setVisibility(View.VISIBLE);
                            listHeader.setText("Lista arkuszy");
                            break;
                        }
                        case 1 : {
                            daysTimer.setVisibility(View.VISIBLE);
                            daysTitle.setVisibility(View.VISIBLE);
                            hmsTimer.setVisibility(View.VISIBLE);
                            listHeader.setVisibility(View.GONE);
                            break;
                        }
                        case 2 : {
                            daysTimer.setVisibility(View.GONE);
                            daysTitle.setVisibility(View.GONE);
                            hmsTimer.setVisibility(View.GONE);
                            listHeader.setVisibility(View.VISIBLE);
                            listHeader.setText("Lista zadań");
                            break;
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            mViewPager.setOffscreenPageLimit(2);
            mViewPager.setAdapter(newAdapter);
            mViewPager.setCurrentItem(1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        new SaveDataAsyncTask(this, mSelectedExam).execute();
        SelectedExamsList.getInstance(this).toFile(this, true);
        super.onPause();
    }


    static class SaveDataAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        private final WeakReference<ExamActivity> activityReference;
        private FileOutputStream[] fileOutputStreams;
        DataViewModel data;
        private final Exam exam;

        // only retain a weak reference to the activity
        SaveDataAsyncTask(ExamActivity activity, Exam exam) {
            this.activityReference = new WeakReference<>(activity);
            this.exam = exam;
        }

        @Override
        protected void onPreExecute() {
            ExamActivity activity = activityReference.get();
            if (activity == null) return;
            data = ViewModelProviders.of(activity).get(DataViewModel.class);
            fileOutputStreams = new FileOutputStream[3];
            fileOutputStreams[0] = activity.data.getSheets().getOutputStream(activity, exam);
            fileOutputStreams[1] = activity.data.getTasks().getOutputStream(activity, exam);
            fileOutputStreams[2] = activity.data.getInfo().getOutputStream(activity, exam);
        }

        @Override
        protected Integer doInBackground(Integer... voids) {
            data.getSheets().toFile(fileOutputStreams[0]);
            data.getTasks().toFile(fileOutputStreams[1]);
            data.getInfo().toFile(fileOutputStreams[2]);
            return null;
        }
    }

    // Custom FragmentPagerAdapter that returns a fragment corresponding to one of the tabs
    protected static class SectionsPagerAdapter extends FragmentPagerAdapter {

        final int selectedExamPOS;

        SectionsPagerAdapter(FragmentManager fm, int selectedExamPOS) {
            super(fm);
            this.selectedExamPOS = selectedExamPOS;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0: {
                    return SheetListFragment.newInstance();
                }
                case 1: {
                    return ExamInfoFragment.newInstance(selectedExamPOS);
                }
                case 2: {
                    return TaskListFragment.newInstance();
                }
            }
            return null;
        }


        @Override
        public int getCount() {
            return 3;
        }


    }

}