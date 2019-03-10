package hanas.dnidomatury.examActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

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

import org.w3c.dom.Text;

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


public class ExamActivity extends AppCompatActivity {

    Exam mSelectedExam;
    int selectedExamPOS;
    ExamsFileList mListOfExam;
    ConstraintLayout preDataLayout;

    //private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener viewPagerListener;
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
        mViewPager = findViewById(R.id.container);
        TextView daysTimer = findViewById(R.id.days_timer_exam_activity);
        TextView hmsTimer = findViewById(R.id.hms_timer_exam_activity);

        preDataLayout = findViewById(R.id.pre_data_view);
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

    // For fragment's onActivityResult to work properly
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        data = ViewModelProviders.of(this).get(DataViewModel.class);
        Context context = this;

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                data.setFromFile(context, mSelectedExam);
                System.out.print("Odczytano dane");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //super.onPostExecute(aVoid);
                System.out.println("Nowy adapter");
                preDataLayout.setVisibility(View.GONE);
                SectionsPagerAdapter newAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), true);
                mViewPager.setAdapter(newAdapter);
                mViewPager.setCurrentItem(1);

            }
        }.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        final Context context = this;
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                data.toFile(context, mSelectedExam);
                return null;
            }
        }.execute();
    }

    // Custom FragmentPagerAdapter that returns a fragment corresponding to one of the tabs
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        boolean isDataLoaded;

        public SectionsPagerAdapter(FragmentManager fm, boolean isDataLoaded) {
            super(fm);
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
            System.out.println("GETITEMCOUNT "+isDataLoaded);
            return isDataLoaded ? 3 : 1;
        }


    }

}