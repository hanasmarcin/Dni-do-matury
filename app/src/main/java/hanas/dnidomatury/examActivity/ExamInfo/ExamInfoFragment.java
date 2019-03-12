package hanas.dnidomatury.examActivity.ExamInfo;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


import androidx.fragment.app.Fragment;
import hanas.dnidomatury.R;
import hanas.dnidomatury.model.exam.ExamsList;
import hanas.dnidomatury.model.exam.Exam;
import hanas.dnidomatury.model.exam.ExamAdditionalInfo;
import hanas.dnidomatury.model.exam.SelectedExamsList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExamInfoFragment extends Fragment {

    FrameLayout background;
    ImageView foreground;
    TextView monthView;
    TextView dayView;
    TextView dayOfWeekView;
    TextView hourView;
    TextView timeInfo;
    TextView roomInfo;
    TextView personInfo;
    TextView extraInfo;
    TextView percentage;
    TextView tasksCounter;
    ProgressBar mProgressBar;
    ExamAdditionalInfo mExamAdditionalInfo;
    int primaryColorID;
    int darkColorID;

    Exam mSelectedExam;
    int selectedExamPOS;
    //ExamViewModel examViewModel;
    //ExamViewModel examViewModel;

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
            selectedExamPOS = bundle.getInt("selectedExamPOS");
            System.out.println(selectedExamPOS+" selectedexamPOS");
            //mSelectedExam = bundle.getParcelable("exam");
            ExamsList listOfExam = SelectedExamsList.getInstance(getActivity());
            mSelectedExam = listOfExam.get(selectedExamPOS);
            mExamAdditionalInfo = new ExamAdditionalInfo(mSelectedExam.getName(), mSelectedExam.getType(), mSelectedExam.getLevel());
            primaryColorID = mSelectedExam.getPrimaryColorID(getActivity());
            darkColorID = mSelectedExam.getDarkColorID(getActivity());
            mExamAdditionalInfo = mExamAdditionalInfo.readFromFile(getActivity());
            mSelectedExam.getTasksCounter().addObserver((observable, counter) -> {
                if (tasksCounter != null) tasksCounter.setText(counter.toString());
            });
            //examViewModel = ViewModelProviders.of(getActivity()).get(ExamViewModel.class);
            /*examViewModel.getCounter().addObserver((observable, object)-> {
                if (tasksCounter != null) tasksCounter.setText((long)object+"");
            });*/

        } else System.out.println("BUNDLENULL");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.fragment_exam_info, container, false);
//        examViewModel = ViewModelProviders.of(getActivity()).get(ExamViewModel.class);
//        examViewModel.getTasksCounter().addObserver(new Observer() {
//            @Override
//            public void update(Observable observable, Object o) {
//                System.out.println(o+" wtfff");
//                tasksCounter.setText(o+"");
//            }
//        });
        background = rootView.findViewById(R.id.img_background);
        foreground = rootView.findViewById(R.id.img_foreground);
        monthView = rootView.findViewById(R.id.info_month);
        dayView = rootView.findViewById(R.id.into_day);
        dayOfWeekView = rootView.findViewById(R.id.info_day_of_week);
        hourView = rootView.findViewById(R.id.info_hour);
        timeInfo = rootView.findViewById(R.id.timeInfo);
        roomInfo = rootView.findViewById(R.id.roomInfo);
        personInfo = rootView.findViewById(R.id.personInfo);
        extraInfo = rootView.findViewById(R.id.extraInfo);
        percentage = rootView.findViewById(R.id.percentage);
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        tasksCounter = rootView.findViewById(R.id.tasks_counter);

        //background.setBackgroundColor(ContextCompat.getColor(getActivity(), primaryColorID));
        //foreground.setColorFilter(ContextCompat.getColor(getActivity(), darkColorID));
//        if (mSelectedExam.getSheetsAverage() == -1) {
//            mProgressBar.setProgress(0);
//            percentage.setText("Brak");
//        } else {
//            mProgressBar.setProgress((int) Math.round(mSelectedExam.getSheetsAverage()));
//            DecimalFormat df = new DecimalFormat("#.##");
//            String percentageString = df.format(mSelectedExam.getSheetsAverage()) + "%";
//            percentage.setText(percentageString);
//        }

        //tasksCounter.setText(Long.toString(mSelectedExam.getTasksCounter().getCounter()));

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
        tasksCounter.setText(mSelectedExam.getTasksCounter().getCounter()+"");

        setInfo();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_info) {
            Intent intent = new Intent(getActivity(), AddExamInfoActivity.class);
            intent.putExtra("infoTime", timeInfo.getText().toString());
            intent.putExtra("infoRoom", roomInfo.getText().toString());
            intent.putExtra("infoPerson", personInfo.getText().toString());
            intent.putExtra("infoExtra", extraInfo.getText().toString());
            getActivity().startActivityForResult(intent, 4444);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 4444) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {

                    mExamAdditionalInfo.set(bundle.getString("infoTime"), bundle.getString("infoRoom"), bundle.getString("infoPerson"), bundle.getString("infoExtra"));
                    setInfo();
                    mExamAdditionalInfo.saveToFile(getActivity());
                }
            }
        }
    }

    private void setInfo() {
        timeInfo.setText(mExamAdditionalInfo.getTime());
        roomInfo.setText(mExamAdditionalInfo.getRoom());
        personInfo.setText(mExamAdditionalInfo.getPerson());
        extraInfo.setText(mExamAdditionalInfo.getExtra());

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


    public void updateSheetData(double sheetsAverage) {
        if (sheetsAverage == -1) {
            mProgressBar.setProgress(0);
            percentage.setText("Brak");
        } else {
            mProgressBar.setProgress((int) Math.round(sheetsAverage));
            DecimalFormat df = new DecimalFormat("#.##");
            String percentageString = df.format(sheetsAverage) + "%";
            percentage.setText(percentageString);
        }
        //Toast.makeText(getActivity(), mSelectedExam.getSheetsAverage()+ " szic awrydż", Toast.LENGTH_SHORT).show();
    }

    public void updateTaskData(long taskCounter) {
        tasksCounter.setText(Long.toString(taskCounter));
        //Toast.makeText(getActivity(), mSelectedExam.getSheetsAverage()+ " szic awrydż", Toast.LENGTH_SHORT).show();
    }

}
