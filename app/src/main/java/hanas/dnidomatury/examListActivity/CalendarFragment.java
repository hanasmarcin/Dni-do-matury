package hanas.dnidomatury.examListActivity;


import android.content.Context;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hanas.dnidomatury.R;
import hanas.dnidomatury.model.ExamsFileList;
import hanas.dnidomatury.model.matura.Exam;
import hanas.dnidomatury.model.matura.ExamsList;
import hanas.dnidomatury.model.matura.SelectedExamsList;
import hanas.dnidomatury.selectActivity.SelectExamAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    private CompactCalendarView calendarView;
    private SelectExamAdapter adapter;
    private ExamsFileList selectedExams;
    private ExamsFileList choosedDateExams = new ExamsList();
    private Calendar chosenDate = Calendar.getInstance();


    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance() {

        return new CalendarFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Set calendarView
        calendarView = rootView.findViewById(R.id.compactcalendar_view);
        calendarView.setShouldShowMondayAsFirstDay(true);
        chosenDate.set(2019, 4, 4, 9, 0, 0);
        calendarView.setCurrentDate(chosenDate.getTime());
        calendarView.shouldScrollMonth(false);

        // Get list of selected exams
        selectedExams = SelectedExamsList.getInstance(getActivity());

//        List<Event> examEvents = new ArrayList<>();
//        for (Exam exam : selectedExams) {
//            int primaryColorID = exam.getPrimaryColorID(getActivity());
//            Event event = new Event(ContextCompat.getColor(getActivity(), primaryColorID), exam.getDate().getTimeInMillis(), exam.getName()+" "+ exam.getType()+" "+ exam.getLevel());
//            examEvents.add(event);
//        }
//        calendarView.addEvents(examEvents);

        // Set recyclerView with exams for chosen date
        RecyclerView recyclerView = rootView.findViewById(R.id.calendar_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new CustomLayoutManager(getActivity()));
        adapter = new SelectExamAdapter(getActivity(), choosedDateExams, true, null);
        recyclerView.setAdapter(adapter);

        // define a listener for calendarView to receive callbacks when certain events happen.
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            // When user chooses the date
            @Override
            public void onDayClick(Date dateClicked) {
                // Create Calendar with chosen date
                chosenDate.setTime(dateClicked);
                Calendar calClicked = Calendar.getInstance();
                calClicked.setTime(dateClicked);

                refreshChosenDateExams(calClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                // If the month is accidentally scrolled
                calendarView.setCurrentDate(chosenDate.getTime());
            }
        });

        return rootView;
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
            Event event = new Event(ContextCompat.getColor(getActivity(), primaryColorID), exam.getDate().getTimeInMillis(), exam.getName()+" "+ exam.getType()+" "+ exam.getLevel());
            examEvents.add(event);
        }
        calendarView.addEvents(examEvents);

        Calendar calClicked = Calendar.getInstance();
        calClicked.setTime(chosenDate.getTime());
        refreshChosenDateExams(calClicked);
    }

    private void refreshChosenDateExams(Calendar calClicked) {
        //Delete exams, that were added for the previeous date
        int listSize = choosedDateExams.size();
        choosedDateExams.clear();
        adapter.notifyItemRangeRemoved(0, listSize);

        // Add new exams to the list of the exams for the chosen date
        for (Exam exam : selectedExams) {
            if (exam.getDate().get(Calendar.DAY_OF_MONTH) == calClicked.get(Calendar.DAY_OF_MONTH) && exam.getDate().get(Calendar.MONTH) == calClicked.get(Calendar.MONTH))
                choosedDateExams.add(exam);
        }
        adapter.notifyItemRangeInserted(0, choosedDateExams.size());

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
            return false;
        }
    }
}
