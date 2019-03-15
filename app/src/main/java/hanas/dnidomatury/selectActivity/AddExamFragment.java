package hanas.dnidomatury.selectActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import hanas.dnidomatury.R;
import hanas.dnidomatury.model.exam.Exam;
import hanas.dnidomatury.model.exam.ExamsFileSupportedList;
import hanas.dnidomatury.model.exam.ExamsList;
import hanas.dnidomatury.model.exam.SelectedExamsList;

public class AddExamFragment extends DialogFragment {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy H:mm", Locale.getDefault());

    private int[] examColors;
    private String[] colorNames;

    private Spinner spinnerName;
    private Spinner spinnerLevel;
    private Spinner spinnerType;
    private TextView examDate;
    private Button addExamButton;
    private Button addDateButton;
    private FloatingActionButton fabPrimary;
    private FloatingActionButton fabDark;
    private ExamsList allExams;
    private boolean isNew;
    private int examPOS;
    private String examColorStr;
    private ExamDialogListener examDialogListener;
    private Exam editedExam;


    public AddExamFragment() {
        // Empty constructor is required for DialogFragment
    }

    static AddExamFragment forEdit(int examPOS) {
        // Create new instance of dialogFragment for sheet's editing
        Bundle args = new Bundle();
        args.putInt("examPOS", examPOS);
        args.putBoolean("isNew", false);

        AddExamFragment fragment = new AddExamFragment();
        fragment.setStyle(STYLE_NORMAL, R.style.DialogWithTitle);
        fragment.setArguments(args);
        return fragment;
    }

    static AddExamFragment forAdd() {
        // Create new instance of dialogFragment for adding new sheet
        Bundle args = new Bundle();
        args.putBoolean("isNew", true);

        AddExamFragment fragment = new AddExamFragment();
        fragment.setStyle(STYLE_NORMAL, R.style.DialogWithTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view,
                             Bundle savedInstanceState) {
        //Create fragment's view
        isNew = getArguments().getBoolean("isNew");
        getDialog().setTitle(isNew ? getString(R.string.add_exam) : getString(R.string.edit_exam));

        return inflater.inflate(R.layout.dialog_fragment_add_exam, view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        examColors = getResources().getIntArray(R.array.exam_colors);
        colorNames = getResources().getStringArray(R.array.exam_color_names);

        // Get fields from view
        spinnerName = view.findViewById(R.id.spinner);
        spinnerLevel = view.findViewById(R.id.spinnerLevel);
        spinnerType = view.findViewById(R.id.spinner3);
        examDate = view.findViewById(R.id.exam_date);
        addExamButton = view.findViewById(R.id.button_add_task);
        addDateButton = view.findViewById(R.id.button_add_date_to_task);
        LinearLayout colorLayout = view.findViewById(R.id.colorLayout);
        fabPrimary = view.findViewById(R.id.fab);
        fabDark = view.findViewById(R.id.fab2);
        Button exit = view.findViewById(R.id.button_clear_new_task);

        // Set fields' values, if editedExam is being edited
        if (!isNew) {
            Bundle args = getArguments();
            examPOS = args.getInt("examPOS");
            editedExam = SelectedExamsList.getInstance(getActivity()).get(examPOS);
            addDateButton.setText(R.string.edit_date);
            addExamButton.setText(R.string.submit);

            setSpinnersForEdition(editedExam.getName(), editedExam.getType(), editedExam.getLevel());
        } else {
            allExams = ExamsFileSupportedList.fromFile(false, getActivity());
            setTypeSpinner();
            setNameSpinner();
            setLevelSpinner();
        }

        // Set buttons' listeners
        colorLayout.setOnClickListener(view1 -> getColorDialog().show(getFragmentManager(), "dialog_demo_1"));
        addExamButton.setOnClickListener(v -> sendResult());
        exit.setOnClickListener(v -> dismiss());
        addDateButton.setOnClickListener(v -> addTimeDate());

    }

    private void setTypeSpinner() {
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    addAdaptersForWrittenExam();
                    addDateButton.setEnabled(false);
                    addExamButton.setEnabled(true);

                } else {
                    addAdapterForOralExam();
                    addDateButton.setEnabled(true);
                    addExamButton.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setNameSpinner() {
        spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Exam exam = allExams.findExam(spinnerName.getSelectedItem().toString(),
                        spinnerLevel.getSelectedItem().toString(),
                        spinnerType.getSelectedItem().toString());
                setExamData(exam, fabPrimary, fabDark, examDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setLevelSpinner() {
        spinnerLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Exam exam = allExams.findExam(spinnerName.getSelectedItem().toString(),
                        spinnerLevel.getSelectedItem().toString(),
                        spinnerType.getSelectedItem().toString());
                setExamData(exam, fabPrimary, fabDark, examDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnersForEdition(String examNameStr, String examTypeStr, String examLevelStr) {
        // Set editedExam's type
        spinnerType.setSelection(getStringIndexInSpinner(spinnerType, examTypeStr.substring(0, examTypeStr.length() - 1) + "a"));
        spinnerType.setEnabled(false);
        if (spinnerType.getSelectedItem().equals(getString(R.string.written))) {
            // The editedExam is written
            addAdaptersForWrittenExam();
            addDateButton.setEnabled(false);
            addExamButton.setEnabled(true);

        } else {
            // The editedExam is oral
            addAdapterForOralExam();
            addDateButton.setEnabled(true);
        }
        spinnerName.setSelection(getStringIndexInSpinner(spinnerName, examNameStr));
        spinnerName.setEnabled(false);
        spinnerLevel.setSelection(getStringIndexInSpinner(spinnerLevel, examLevelStr.substring(0, examLevelStr.length() - 1) + "a"));
        spinnerLevel.setEnabled(false);
        setExamData(editedExam, fabPrimary, fabDark, examDate);
    }


    private void addAdapterForOralExam() {
        // Set list of available exams' names for "matura ustna"
        ArrayAdapter<CharSequence> nameAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.przedmiot_ustny, android.R.layout.simple_spinner_item);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerName.setAdapter(nameAdapter);

        // Set list of available exams' levels for "matura ustna"
        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.poziom_ustny_dj, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(levelAdapter);
    }

    private static int getStringIndexInSpinner(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    private void addAdaptersForWrittenExam() {
        // Set list of available exams' names for "matura pisemna"
        ArrayAdapter<CharSequence> nameAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.przedmiot_pisemny, android.R.layout.simple_spinner_item);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerName.setAdapter(nameAdapter);

        // Set list of available exams' levels for "matura pisemna"
        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.poziom_pisemny_dj, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(levelAdapter);
    }

    private void sendResult() {
        if (isNew) examDialogListener.addExam(spinnerName.getSelectedItem().toString(),
                spinnerType.getSelectedItem().toString(),
                spinnerLevel.getSelectedItem().toString(),
                examColorStr,
                examDate.getText().toString());
        else examDialogListener.editExam(spinnerName.getSelectedItem().toString(),
                spinnerType.getSelectedItem().toString(),
                spinnerLevel.getSelectedItem().toString(),
                examColorStr,
                examDate.getText().toString(),
                examPOS);
        dismiss();
//        if (getTargetFragment() == null)
//            return;
//        Intent intent = new Intent();
//
//        intent.putExtra("examName", spinnerName.getSelectedItem().toString());
//        intent.putExtra("examType", spinnerType.getSelectedItem().toString());
//        intent.putExtra("examLevel", spinnerLevel.getSelectedItem().toString());
//        intent.putExtra("examColor", examColorStr);
//        intent.putExtra("isNew", isNew);
//        intent.putExtra("examPOS", examPOS);
//        getActivity().sta
//        getParentFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
//        dismiss();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        examDialogListener = (ExamDialogListener) context;
    }

    public interface ExamDialogListener {
        void addExam(String name, String type, String level, String color, String examDateText);
        void editExam(String name, String type, String level, String color, String examDateText, int examPOS);
    }

    private void setExamData(Exam exam, FloatingActionButton fabPrimary, FloatingActionButton fabDark, TextView examDate) {
        // Set date and colors in fragment's fields
        if (exam != null) {
            // Exam is written, it exists on written exams' list
            fabPrimary.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), exam.getPrimaryColorID(getActivity()))));
            fabDark.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), exam.getDarkColorID(getActivity()))));
            examDate.setText(dateFormat.format(exam.getDate().getTime()));
        } else {
            // Exam is oral, it doesn't exit on written exams' list
            int examColorPrimary = R.color.Green;
            int darkExamColor = R.color.GreenDark;
            fabPrimary.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), examColorPrimary)));
            fabDark.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), darkExamColor)));
            examDate.setText(R.string.no_date);
        }
    }

    private void addTimeDate() {
        // Get current values
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        final TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (timePicker, hour, minutes) -> {
            // If time was picked, set it to the calendar and enable adding button
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minutes);
            c.set(Calendar.SECOND, 0);
            addExamButton.setEnabled(true);
        }, mHourOfDay, mMinute, true);
        timePickerDialog.setTitle(getString(R.string.choose_exam_hour));

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            // If date was picked, set the date and show timePickerDialog
            c.set(year, monthOfYear, dayOfMonth);
            examDate.setText(dateFormat.format(c.getTime()));
            timePickerDialog.show();
        }, mYear, mMonth, mDay);
        datePickerDialog.setTitle(getString(R.string.choose_exam_date));

        datePickerDialog.show();
    }


    private SpectrumDialog getColorDialog() {
        final SpectrumDialog.Builder colorDialogBuilder = new SpectrumDialog.Builder(getActivity())
                .setColors(R.array.exam_colors)
                .setDismissOnColorSelected(false)
                .setOnColorSelectedListener((positiveResult, color) -> {
                    if (positiveResult) {
                        int colorPosition = 0;
                        for (int i = 0; i < examColors.length; i++) {
                            if (color == examColors[i]) colorPosition = i;
                        }
                        examColorStr = colorNames[colorPosition];
                        fabPrimary.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), getResources().getIdentifier(examColorStr, "color", getActivity().getPackageName()))));
                        fabDark.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), getResources().getIdentifier(examColorStr + "Dark", "color", getActivity().getPackageName()))));

                    }
                });
        return colorDialogBuilder.build();
    }
}

