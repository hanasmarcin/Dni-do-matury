package hanas.dnidomatury.examListActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import hanas.dnidomatury.examActivity.ExamActivity;
import hanas.dnidomatury.model.exam.ExamsList;
import hanas.dnidomatury.model.exam.Exam;
import hanas.dnidomatury.model.exam.ExamTimer;
import hanas.dnidomatury.R;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {

    private Context context;
    private ExamsList mExamList;

    public ExamAdapter(Context context, ExamsList selectedExamsList) {
        this.context = context;
        this.mExamList = selectedExamsList;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_exam, viewGroup, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExamViewHolder examViewHolder, final int i) {

        // Find the corresponding exam
        Exam exam = mExamList.get(i);
        final Exam mExam = exam;

        // Set card properties
        setCardProperties(examViewHolder, mExam);

        // If the timer for this card was set previously, reset it
        if (examViewHolder.examTimer.getTimer() != null)
            examViewHolder.examTimer.getTimer().cancel();
        examViewHolder.examTimer.startExamTimer(context, mExam, examViewHolder.daysTextView, examViewHolder.hoursTextVIew);


        examViewHolder.mCardView.setOnClickListener(view -> {
            // Start corresponding ExamActivity and pass exam's position on SelectedExamsList,
            // which is the same as position of this adapter in viewHolder
            Intent intent = new Intent(context, ExamActivity.class);
            intent.putExtra("selectedExamPOS", examViewHolder.getAdapterPosition());
            ((Activity) view.getContext()).startActivityForResult(intent, 15);
        });
    }

    private void setCardProperties(ExamViewHolder viewHolder, Exam exam) {
        // Find exam's colors
        int darkColorID = exam.getDarkColorID(context);
        int primaryColorID = exam.getPrimaryColorID(context);

        // Set card properties
        viewHolder.examNameTextView.setText(exam.getName());
        viewHolder.examLevelTypeTextView.setText(exam.getLevel() + " " + exam.getType());
        viewHolder.tasksCounter.setText(String.format(Locale.getDefault(), "%d", exam.getTasksCounter().getCounter()));
        viewHolder.darkColorField.setBackgroundColor(ContextCompat.getColor(context, darkColorID));
        viewHolder.mCardView.setBackgroundColor(ContextCompat.getColor(context, darkColorID));
        viewHolder.primaryColorField.setBackgroundColor(ContextCompat.getColor(context, primaryColorID));

    }

    @Override
    public int getItemCount() {
        return mExamList.size();
    }


    public class ExamViewHolder extends RecyclerView.ViewHolder {

        private final CardView mCardView;
        private final LinearLayout darkColorField;
        private final LinearLayout primaryColorField;
        private final TextView daysTextView;
        private final TextView hoursTextVIew;
        private final TextView examNameTextView;
        private final TextView examLevelTypeTextView;
        private final TextView tasksCounter;
        private final ExamTimer examTimer;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);

            // Create card's view
            mCardView = itemView.findViewById(R.id.cardView);
            darkColorField = itemView.findViewById(R.id.dark_color_field);
            primaryColorField = itemView.findViewById(R.id.primary_color_field);
            daysTextView = itemView.findViewById(R.id.dni_counter_list);
            hoursTextVIew = itemView.findViewById(R.id.godziny_list);
            examNameTextView = itemView.findViewById(R.id.exam_list);
            examLevelTypeTextView = itemView.findViewById(R.id.poziom_typ_list);
            tasksCounter = itemView.findViewById(R.id.exam_task_counter);
            examTimer = new ExamTimer();

        }
    }

}
