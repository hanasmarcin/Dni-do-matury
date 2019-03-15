package hanas.dnidomatury.examActivity.sheetList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import hanas.dnidomatury.R;
import hanas.dnidomatury.examActivity.AddDate;

import static android.app.Activity.RESULT_OK;

public class AddSheetFragment extends DialogFragment implements AddDate {

    private final static DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private EditText nameTextView;
    private TextView dateTextView;
    private EditText pointsTextView;
    private EditText maxPointsTextView;
    private Button addSheetButton;
    private boolean isNew;
    private int sheetID;


    public AddSheetFragment() {
        // Empty constructor is required for DialogFragment
    }

    static AddSheetFragment forEdit(int sheetID, String sheetName, String sheetDate, double points, double maxPoints) {
        // Create new instance of dialogFragment for sheet's editing
        Bundle args = new Bundle();
        args.putInt("sheetID", sheetID);
        args.putString("sheetName", sheetName);
        args.putString("sheetDateText", sheetDate);
        args.putDouble("points", points);
        args.putDouble("maxPoints", maxPoints);
        args.putBoolean("isNew", false);

        AddSheetFragment fragment = new AddSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    static AddSheetFragment forAdd() {
        // Create new instance of dialogFragment for adding new sheet
        Bundle args = new Bundle();
        args.putBoolean("isNew", true);

        AddSheetFragment fragment = new AddSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view,
                             Bundle savedInstanceState) {
        //Create fragment's view
        isNew = getArguments().getBoolean("isNew");
        getDialog().setTitle(isNew ? "Dodaj zadanie" : "Edytuj zadanie");

        return inflater.inflate(R.layout.dialog_fragment_add_sheet, view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get fields from view
        nameTextView = view.findViewById(R.id.sheet_name);
        dateTextView = view.findViewById(R.id.sheet_date);
        pointsTextView = view.findViewById(R.id.points);
        maxPointsTextView = view.findViewById(R.id.max_points);
        addSheetButton = view.findViewById(R.id.button_add_sheet);
        Button addDateButton = view.findViewById(R.id.button_add_date_to_sheet);
        Button exit = view.findViewById(R.id.button_clear_new_sheet);

        // Set fields' values
        if (!isNew) {
            Bundle args = getArguments();
            String sheetNameStr = args.getString("sheetName");
            String sheetDateStr = args.getString("sheetDateText");
            double points = args.getDouble("points");
            double maxPoints = args.getDouble("maxPoints");
            sheetID = args.getInt("sheetID");
            nameTextView.setText(sheetNameStr);
            dateTextView.setText(sheetDateStr);
            pointsTextView.setText(decimalFormat.format(points));
            maxPointsTextView.setText(decimalFormat.format(maxPoints));
        } else {
            sheetID = 1;
        }

        // Set buttons' listeners
        addSheetButton.setOnClickListener(v -> sendResult());
        exit.setOnClickListener(v -> dismiss());
        addDateButton.setOnClickListener(v -> addDate(dateTextView, getActivity()));

        // Set editTexts listeners
        PointsTextWatcher watcher = new PointsTextWatcher();
        pointsTextView.addTextChangedListener(watcher);
        maxPointsTextView.addTextChangedListener(watcher);


    }

    private void sendResult() {
        if (getTargetFragment() == null)
            return;
        try {
            double points = Double.parseDouble(pointsTextView.getText().toString());
            double maxPoints = Double.parseDouble(maxPointsTextView.getText().toString());
            if (points < 0 || maxPoints <= 0) throw new NumberFormatException();

            Intent intent = new Intent();
            intent.putExtra("sheetName", nameTextView.getText().toString());
            intent.putExtra("sheetDateText", dateTextView.getText().toString());
            intent.putExtra("points", points);
            intent.putExtra("maxPoints", maxPoints);
            intent.putExtra("sheetID", sheetID);
            getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
            dismiss();
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Niepoprawna liczba punktów", Toast.LENGTH_SHORT).show();
            pointsTextView.setText(null);
            maxPointsTextView.setText(null);

        }
    }

    class PointsTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (pointsTextView.getText().length() != 0 && maxPointsTextView.getText().length() != 0)
                addSheetButton.setEnabled(true);
        }
    }


}
