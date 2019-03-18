package hanas.dnidomatury.examActivity.examInfo;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import hanas.dnidomatury.R;
import hanas.dnidomatury.examActivity.DataViewModel;
import hanas.dnidomatury.model.exam.ExamsList;
import hanas.dnidomatury.model.exam.Exam;
import hanas.dnidomatury.model.exam.ExamAdditionalInfo;
import hanas.dnidomatury.model.exam.SelectedExamsList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExamInfoFragment extends Fragment {

    private static final int ADD_INFO_REQUEST_CODE = 8978;
    private TextView timeInfo;
    private TextView roomInfo;
    private TextView personInfo;
    private TextView extraInfo;
    private TextView percentage;
    private TextView tasksCounter;
    private ProgressBar mProgressBar;
    private ExamAdditionalInfo mExamAdditionalInfo;
    private Exam mSelectedExam;

    public ExamInfoFragment() {
        // Required empty public constructor
    }

    public static ExamInfoFragment newInstance(int selectedExamPOS) {

        Bundle args = new Bundle();
        args.putInt("selectedExamPOS", selectedExamPOS);
        ExamInfoFragment fragment = new ExamInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            // Find selected exam
            int selectedExamPOS = bundle.getInt("selectedExamPOS");
            ExamsList listOfExam = SelectedExamsList.getInstance(getActivity());
            mSelectedExam = listOfExam.get(selectedExamPOS);

            mExamAdditionalInfo = ViewModelProviders.of(getActivity()).get(DataViewModel.class).getInfo();
            mExamAdditionalInfo.addObserver((observable, o) -> setInfoToView());
            mSelectedExam.getTasksCounter().addObserver((observable, counter) -> {
                if (tasksCounter != null) tasksCounter.setText(counter.toString());
            });
            mSelectedExam.getSheetsAverage().addObserver((observable, average) -> {
                if (tasksCounter != null) updateSheetData((double) average);
            });

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_exam_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timeInfo = view.findViewById(R.id.timeInfo);
        roomInfo = view.findViewById(R.id.roomInfo);
        personInfo = view.findViewById(R.id.personInfo);
        extraInfo = view.findViewById(R.id.extraInfo);
        percentage = view.findViewById(R.id.percentage);
        mProgressBar = view.findViewById(R.id.progress_bar);
        tasksCounter = view.findViewById(R.id.tasks_counter);

        setDateToView(view);
        updateTaskData(mSelectedExam.getTasksCounter().getCounter());
        updateSheetData(mSelectedExam.getSheetsAverage().getAverage());
        setInfoToView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_info) {
            AddExamInfoFragment addExamInfoFragment = AddExamInfoFragment.newInstance();
            addExamInfoFragment.setTargetFragment(this, ADD_INFO_REQUEST_CODE);
            addExamInfoFragment.show(getFragmentManager(), "WTF4");
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDateToView(View view) {
        // Find elements in the view
        TextView monthView = view.findViewById(R.id.info_month);
        TextView dayView = view.findViewById(R.id.into_day);
        TextView dayOfWeekView = view.findViewById(R.id.info_day_of_week);
        TextView hourView = view.findViewById(R.id.info_hour);

        // Find date values and set them to the view
        SimpleDateFormat monthFormat = new SimpleDateFormat("LLLL", Locale.getDefault());
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat hourFormat = new SimpleDateFormat("H:mm", Locale.getDefault());
        String month = monthFormat.format(mSelectedExam.getDate().getTime());
        String dayOfWeek = dayOfWeekFormat.format(mSelectedExam.getDate().getTime());
        String hour = hourFormat.format(mSelectedExam.getDate().getTime());
        dayView.setText(String.format(Locale.getDefault(), "%d", mSelectedExam.getDate().get(Calendar.DAY_OF_MONTH)));

        monthView.setText(month);
        dayOfWeekView.setText(dayOfWeek);
        hourView.setText(hour);
    }

    private void setInfoToView() {
        // Set additional exam info to view
        timeInfo.setText(mExamAdditionalInfo.getTime());
        roomInfo.setText(mExamAdditionalInfo.getRoom());
        personInfo.setText(mExamAdditionalInfo.getPerson());
        extraInfo.setText(mExamAdditionalInfo.getExtra());

        // If one field is empty, don't show it
        if (mExamAdditionalInfo.getTime() != null && !mExamAdditionalInfo.getTime().isEmpty()) {
            timeInfo.setVisibility(View.VISIBLE);
        } else timeInfo.setVisibility(View.GONE);
        if (mExamAdditionalInfo.getRoom() != null && !mExamAdditionalInfo.getRoom().isEmpty()) {
            roomInfo.setVisibility(View.VISIBLE);
        } else roomInfo.setVisibility(View.GONE);
        if (mExamAdditionalInfo.getPerson() != null && !mExamAdditionalInfo.getPerson().isEmpty()) {
            personInfo.setVisibility(View.VISIBLE);
        } else personInfo.setVisibility(View.GONE);
        if (mExamAdditionalInfo.getExtra() != null && !mExamAdditionalInfo.getExtra().isEmpty()) {
            extraInfo.setVisibility(View.VISIBLE);
        } else extraInfo.setVisibility(View.GONE);
    }


    private void updateSheetData(double sheetsAverage) {
        if (Double.isNaN(sheetsAverage)) {
            mProgressBar.setProgress(0);
            percentage.setText(R.string.empty);
        } else {
            mProgressBar.setProgress((int) Math.round(sheetsAverage));
            DecimalFormat df = new DecimalFormat("#.##");
            String percentageString = df.format(sheetsAverage) + "%";
            percentage.setText(percentageString);
        }
    }

    private void updateTaskData(long taskCounter) {
        tasksCounter.setText(String.format(Locale.getDefault(), "%d", taskCounter));
    }

}
