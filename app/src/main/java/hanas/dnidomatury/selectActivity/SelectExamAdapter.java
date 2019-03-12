package hanas.dnidomatury.selectActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import hanas.dnidomatury.R;
import hanas.dnidomatury.model.exam.ExamsList;
import hanas.dnidomatury.model.exam.Exam;
import hanas.dnidomatury.examActivity.ExamActivity;
import hanas.dnidomatury.model.exam.SelectedExamsList;
import hanas.dnidomatury.touchHelper.ItemTouchHelperAdapter;
import hanas.dnidomatury.touchHelper.ItemTouchHelperViewHolder;

public class SelectExamAdapter extends RecyclerView.Adapter<SelectExamAdapter.FullListExamViewHolder>
        implements ItemTouchHelperAdapter {

    private Context context;
    private ExamsList mFullExamList;
    private boolean isClickable;
    private CoordinatorLayout coordinator;

    public SelectExamAdapter(Context context, ExamsList fullExamList, boolean isClickable, CoordinatorLayout coordinator) {
        this.context = context;
        this.mFullExamList = fullExamList;
        this.isClickable = isClickable;
        this.coordinator = coordinator;

    }

    @NonNull
    @Override
    public FullListExamViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_select, viewGroup, false);
        return new FullListExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FullListExamViewHolder fullListExamViewHolder, int i) {
        final Exam mExam = mFullExamList.get(i);
        final int primaryColorID = mExam.getPrimaryColorID(context);
        final int darkColorID = mExam.getDarkColorID(context);

        fullListExamViewHolder.everyListTitle.setText(mExam.getName());
        fullListExamViewHolder.everyListPoziom.setText(mExam.getType() + " " + mExam.getLevel());

        if (primaryColorID != 0)
            fullListExamViewHolder.primaryColorField.setBackgroundColor(ContextCompat.getColor(context, primaryColorID));

        fullListExamViewHolder.everyListCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = isClickable ? new Intent(context, ExamActivity.class) : new Intent(context, AddExamActivity.class);
                int examPos = 0;
                ExamsList everyExam = SelectedExamsList.getInstance(context);
                for (int i = 0; i < everyExam.size(); i++) {
                    Exam exam = everyExam.get(i);
                    if (mExam.getName().equals(exam.getName()) && mExam.getLevel().equals(exam.getLevel()) && mExam.getType().equals(exam.getType())) {
                        examPos = i;
                        break;
                    }
                }
                intent.putExtra("selectedExamPOS", examPos);
                if (isClickable)
                    ((Activity) view.getContext()).startActivityForResult(intent, 15);
                else ((Activity) view.getContext()).startActivityForResult(intent, 5320);

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
        if (coordinator != null) {
            final Snackbar deletedExamSnackbar = Snackbar.make(coordinator, "Usunięto maturę",
                    Snackbar.LENGTH_SHORT);
            deletedExamSnackbar.setAction("Cofnij", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFullExamList.add(position, deletedExam);
                    notifyItemInserted(position);
                }
            });
            deletedExamSnackbar.show();
        }
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

        public FullListExamViewHolder(@NonNull View itemView) {
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
