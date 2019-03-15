package hanas.dnidomatury.selectActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import hanas.dnidomatury.R;
import hanas.dnidomatury.model.exam.ExamAdditionalInfo;
import hanas.dnidomatury.model.exam.ExamsList;
import hanas.dnidomatury.model.exam.Exam;
import hanas.dnidomatury.examActivity.ExamActivity;
import hanas.dnidomatury.model.exam.SelectedExamsList;
import hanas.dnidomatury.model.examSpecific.sheet.SheetsList;
import hanas.dnidomatury.model.examSpecific.task.TasksList;
import hanas.dnidomatury.model.fileSupport.FileSupported;
import hanas.dnidomatury.touchHelper.ItemTouchHelperAdapter;
import hanas.dnidomatury.touchHelper.ItemTouchHelperViewHolder;

public class SelectExamAdapter extends RecyclerView.Adapter<SelectExamAdapter.FullListExamViewHolder>
        implements ItemTouchHelperAdapter {

    private FragmentActivity selectActivity;
    private ExamsList mFullExamList;
    private boolean isClickable;

    public SelectExamAdapter(FragmentActivity selectActivity, ExamsList fullExamList, boolean isClickable) {
        this.selectActivity = selectActivity;
        this.mFullExamList = fullExamList;
        this.isClickable = isClickable;

    }

    @NonNull
    @Override
    public FullListExamViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(selectActivity);
        View view = inflater.inflate(R.layout.card_select, viewGroup, false);
        return new FullListExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FullListExamViewHolder fullListExamViewHolder, int i) {
        final Exam mExam = mFullExamList.get(i);
        final int primaryColorID = mExam.getPrimaryColorID(selectActivity);

        fullListExamViewHolder.everyListTitle.setText(mExam.getName());
        fullListExamViewHolder.everyListPoziom.setText(String.format("%s %s", mExam.getType(), mExam.getLevel()));

        if (primaryColorID != 0)
            fullListExamViewHolder.primaryColorField.setBackgroundColor(ContextCompat.getColor(selectActivity, primaryColorID));

        fullListExamViewHolder.everyListCardView.setOnClickListener(view -> {
            if (isClickable) {
                Intent intent = new Intent(selectActivity, ExamActivity.class);
                int examPos = 0;
                ExamsList everyExam = SelectedExamsList.getInstance(selectActivity);
                for (int i1 = 0; i1 < everyExam.size(); i1++) {
                    Exam exam = everyExam.get(i1);
                    if (mExam.getName().equals(exam.getName()) && mExam.getLevel().equals(exam.getLevel()) && mExam.getType().equals(exam.getType())) {
                        examPos = i1;
                        break;
                    }
                }
                intent.putExtra("selectedExamPOS", examPos);
                if (isClickable)
                    selectActivity.startActivityForResult(intent, 15);
            } else {
                AddExamFragment addExamDialog = AddExamFragment.forEdit(fullListExamViewHolder.getAdapterPosition());

                addExamDialog.show(selectActivity.getSupportFragmentManager(), "WTF4");

            }
        });

    }

    @Override
    public int getItemCount() {
        return mFullExamList.size();
    }

    @Override
    public void onItemDismiss(final int position) {
        final Exam deletedExam = mFullExamList.remove(position);
        notifyItemRemoved(position);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(selectActivity);
        dialogBuilder.setMessage("Usunięto maturę. Czy usunąć również dane z nią powiązane (listę zadań, arkuszy, dodatkowe informacje");
        dialogBuilder.setPositiveButton("USUŃ DANE", (dialogInterface, i) -> {
            FileSupported.deleteFile(selectActivity, deletedExam, TasksList.FILE_SUFFIX);
            FileSupported.deleteFile(selectActivity, deletedExam, SheetsList.FILE_SUFFIX);
            FileSupported.deleteFile(selectActivity, deletedExam, ExamAdditionalInfo.FILE_SUFFIX);
        });
        dialogBuilder.setNeutralButton("ANULUJ", (dialogInterface, i) -> {});
        dialogBuilder.setNegativeButton("POZOSTAW DANE", (dialogInterface, i) -> {
            mFullExamList.add(position, deletedExam);
            notifyItemInserted(position);
        });
        dialogBuilder.create().show();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        mFullExamList.swap(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public class FullListExamViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        TextView everyListTitle;
        TextView everyListPoziom;
        CardView everyListCardView;
        LinearLayout primaryColorField;

        FullListExamViewHolder(@NonNull View itemView) {
            super(itemView);

            primaryColorField = itemView.findViewById(R.id.primary_color_full_list);
            everyListCardView = itemView.findViewById(R.id.everyListCardView);
            everyListTitle = itemView.findViewById(R.id.every_list_title);
            everyListPoziom = itemView.findViewById(R.id.every_list_poziom);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.WHITE);
        }
    }

}
