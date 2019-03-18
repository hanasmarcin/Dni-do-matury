package hanas.dnidomatury.examListActivity;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hanas.dnidomatury.R;
import hanas.dnidomatury.model.exam.ExamsFileSupportedList;
import hanas.dnidomatury.model.exam.ExamsList;
import hanas.dnidomatury.model.exam.Exam;
import hanas.dnidomatury.model.exam.SelectedExamsList;
import hanas.dnidomatury.selectActivity.SelectExamAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    private static final String PREFERENCES_NAME = "calPref";
    private SharedPreferences preferences;
    private CompactCalendarView calendarView;
    private SelectExamAdapter adapter;
    private ExamsList selectedExams;
    private final ExamsList chosenDateExams = ExamsFileSupportedList.newObject();
    private Date chosenDate = new Date();


    public CalendarFragment() {
        // Required empty public constructor
    }

    static CalendarFragment newInstance() {

        return new CalendarFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        preferences = getActivity().getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);

        // Set calendarView
        final TextView monthText = rootView.findViewById(R.id.cal_month);
        SimpleDateFormat monthFormat = new SimpleDateFormat("LLLL", Locale.getDefault());
        calendarView = rootView.findViewById(R.id.compactcalendar_view);
        calendarView.setShouldShowMondayAsFirstDay(true);

//        RobotoCalendarView cal = new RobotoCalendarView(getActivity());
//        cal.setRobotoCalendarListener(new RobotoCalendarView.RobotoCalendarListener() {
//            @Override
//            public void onDayClick(Date date) {
//
//            }
//
//            @Override
//            public void onDayLongClick(Date date) {
//
//            }
//
//            @Override
//            public void onRightButtonClick() {
//
//            }
//
//            @Override
//            public void onLeftButtonClick() {
//
//            }
//        });
        Date currentDate = new Date(restoreData());
        monthText.setText(monthFormat.format(currentDate));
        calendarView.setCurrentDate(new Date(restoreData()));
        calendarView.shouldScrollMonth(false);
        calendarView.showCalendarWithAnimation();

        // Get list of selected exams
        selectedExams = SelectedExamsList.getInstance(getActivity());

        // Set recyclerView with exams for chosen date
        RecyclerView recyclerView = rootView.findViewById(R.id.calendar_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new CustomLayoutManager(getActivity()));
        adapter = new SelectExamAdapter(getActivity(), chosenDateExams, true);
        recyclerView.setAdapter(adapter);

        ImageView prevBtn = rootView.findViewById(R.id.img_before);
        ImageView nextBtn = rootView.findViewById(R.id.img_after);
        prevBtn.setOnClickListener(view -> {
            calendarView.showPreviousMonth();
            calendarView.showCalendarWithAnimation();
        });
        nextBtn.setOnClickListener(view -> {
            calendarView.showNextMonth();
            calendarView.showCalendarWithAnimation();
        });

        // define a listener for calendarView to receive callbacks when certain events happen.
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            // When user chooses the date
            @Override
            public void onDayClick(Date dateClicked) {
                refreshChosenDateExams(dateClicked);

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                calendarView.setCurrentDate(firstDayOfNewMonth);
                refreshChosenDateExams(firstDayOfNewMonth);
                saveData(firstDayOfNewMonth.getTime());
                monthText.setText(monthFormat.format(firstDayOfNewMonth));
            }
        });

        return rootView;
    }

    private void saveData(long startTime) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putLong("calStartTime", startTime);
        preferencesEditor.apply();
    }

    private long restoreData() {
        return preferences.getLong("calStartTime", 1556694000000L);
    }

    @Override
    public void onResume() {
        super.onResume();

        //Remove events, that were created previously, exams' properties might have been changed
        calendarView.removeAllEvents();

        // Add exams as events to the calendarView
        List<Event> examEvents = new ArrayList<>();
        for (Exam exam : selectedExams) {
            int primaryColorID = exam.getPrimaryColorID(getActivity());
            Event event = new Event(ContextCompat.getColor(getActivity(), primaryColorID), exam.getDate().getTimeInMillis(), exam);
            System.out.println("DATAEX "+exam.getDate().toString());
            examEvents.add(event);
        }
        calendarView.addEvents(examEvents);

        refreshChosenDateExams(chosenDate);
    }

    private void refreshChosenDateExams(Date dateClicked) {
        //Delete exams, that were added for the previeous date
        chosenDate = dateClicked;
        int listSize = chosenDateExams.size();
        chosenDateExams.clear();
        adapter.notifyItemRangeRemoved(0, listSize);

        List<Event> eventsForDate = calendarView.getEvents(dateClicked);
        for (Event event : eventsForDate) {
            chosenDateExams.add((Exam) event.getData());
        }

        chosenDateExams.sort();
        adapter.notifyItemRangeInserted(0, chosenDateExams.size());

    }


    // Custom layout manager for fancy animations
    private class CustomLayoutManager extends LinearLayoutManager {

        private CustomLayoutManager(Context context) {
            super(context);
        }

        @Override
        public boolean supportsPredictiveItemAnimations(){
            return true;
        }

        @Override
        public boolean canScrollVertically() {
            return true;
        }
    }
}
