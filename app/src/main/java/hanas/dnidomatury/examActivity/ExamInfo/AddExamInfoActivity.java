package hanas.dnidomatury.examActivity.ExamInfo;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hanas.dnidomatury.R;

public class AddExamInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam_info);
        final EditText editTime = findViewById(R.id.edit_time);
        final EditText editRoom = findViewById(R.id.edit_room);
        final EditText editPerson = findViewById(R.id.edit_person);
        final EditText editExtra = findViewById(R.id.edit_extra);

        Button confirm = findViewById(R.id.confirm_info);
        Button exit = findViewById(R.id.clear_info);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            editTime.setText(bundle.getString("infoTime"));
            editRoom.setText(bundle.getString("infoRoom"));
            editPerson.setText(bundle.getString("infoPerson"));
            editExtra.setText(bundle.getString("infoExtra"));
        }

        confirm.setOnClickListener(view -> {
                Intent intent = new Intent();
                intent.putExtra("infoTime", editTime.getText().toString());
                intent.putExtra("infoRoom", editRoom.getText().toString());
                intent.putExtra("infoPerson", editPerson.getText().toString());
                intent.putExtra("infoExtra", editExtra.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
        });

        exit.setOnClickListener(view -> finish() );
    }
}
