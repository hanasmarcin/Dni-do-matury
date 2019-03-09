package hanas.dnidomatury.examActivity;

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

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

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

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener viewPagerListener;
    //private TaskListFragment mTaskListFragment;
    //private ExamInfoFragment mExamInfoFragment;
    //private SheetListFragment mSheetListFragment;
    private Toolbar toolbar;

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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.getAdapter().notifyDataSetChanged();

        TextView daysTimer = findViewById(R.id.days_timer_exam_activity);
        TextView hmsTimer = findViewById(R.id.hms_timer_exam_activity);
        new ExamTimer().startExamTimer(this, mSelectedExam, daysTimer, hmsTimer);
    }

    // For fragment's onActivityResult to work properly
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Custom FragmentPagerAdapter that returns a fragment corresponding to one of the tabs
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private FragmentActivity activity;

        public SectionsPagerAdapter(FragmentManager fm, FragmentActivity activity) {
            super(fm);
            this.activity = activity;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
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
            return 3;
        }


    }

}