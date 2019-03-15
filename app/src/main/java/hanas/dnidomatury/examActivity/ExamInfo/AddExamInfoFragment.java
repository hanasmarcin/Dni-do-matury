package hanas.dnidomatury.examActivity.examInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import hanas.dnidomatury.R;
import hanas.dnidomatury.examActivity.DataViewModel;
import hanas.dnidomatury.model.exam.ExamAdditionalInfo;

import static android.app.Activity.RESULT_OK;

public class AddExamInfoFragment extends DialogFragment {

    private EditText editTime;
    private EditText editRoom;
    private EditText editPerson;
    private EditText editExtra;
    private ExamAdditionalInfo info;

    public AddExamInfoFragment() {
        // Empty constructor is required for DialogFragment
    }

    static AddExamInfoFragment newInstance() {
        // Create new instance of dialogFragment for task's editing
        Bundle args = new Bundle();

        AddExamInfoFragment fragment = new AddExamInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view,
                             Bundle savedInstanceState) {
        //Create fragment's view
        return inflater.inflate(R.layout.dialog_fragment_add_exam_info, view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get fields from view
        editTime = view.findViewById(R.id.edit_time);
        editRoom = view.findViewById(R.id.edit_room);
        editPerson = view.findViewById(R.id.edit_person);
        editExtra = view.findViewById(R.id.edit_extra);

        Button confirm = view.findViewById(R.id.confirm_info);
        Button exit = view.findViewById(R.id.clear_info);


        // Set fields' values
        DataViewModel data = ViewModelProviders.of(getActivity()).get(DataViewModel.class);
        info = data.getInfo();
        Bundle bundle = getArguments();
        if (bundle != null) {
            editTime.setText(info.getTime());
            editRoom.setText(info.getRoom());
            editPerson.setText(info.getPerson());
            editExtra.setText(info.getExtra());
        }

        // Set buttons' listeners
        confirm.setOnClickListener(v -> sendResult());
        exit.setOnClickListener(v -> dismiss());

    }

    private void sendResult() {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        info.set(editTime.getText().toString(),
                editRoom.getText().toString(),
                editPerson.getText().toString(),
                editExtra.getText().toString());
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
        dismiss();
    }

}
