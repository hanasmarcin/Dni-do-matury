package hanas.dnidomatury.examActivity.SheetList;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.DecimalFormat;

import hanas.dnidomatury.R;
import hanas.dnidomatury.examActivity.ExamSpecificAdapterContextMenuListener;
import hanas.dnidomatury.examActivity.TaskList.TaskAdapter;
import hanas.dnidomatury.model.ExamSpecificList;
import hanas.dnidomatury.model.matura.Exam;
import hanas.dnidomatury.model.sheet.Sheet;
import hanas.dnidomatury.model.task.Task;

public class SheetAdapter extends RecyclerView.Adapter<SheetAdapter.SheetViewHolder> {

    private final static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private Fragment fragment;
    private Context context;
    private ExamSpecificList<Sheet> sheetsList;
    private CoordinatorLayout examCoordinator;

    public SheetAdapter(Fragment fragment, ExamSpecificList<Sheet> sheets, CoordinatorLayout examCoordinator) {
        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.sheetsList = sheets;
        this.examCoordinator = examCoordinator;
    }


    @NonNull
    @Override
    public SheetViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_sheet, viewGroup, false);

        // Create new view holder for each element
        return new SheetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SheetViewHolder viewHolder, final int sheetID) {
        // Find adapter's sheet and it's percent score
        final Sheet sheet = sheetsList.get(sheetID);
        final float percentScore = (float) (sheet.getPoints() / sheet.getMaxPoints()) * 100;

        // Set card properties
        viewHolder.setCardProperties(sheet, percentScore);

        // Set listener to the card
        viewHolder.mCardView.setOnClickListener(view -> {
            // Show snackbar with sheet's score in fragment's layout
            String msg = decimalFormat.format(sheet.getPoints()) + "/" + decimalFormat.format(sheet.getMaxPoints()) + " punktów";
            Snackbar.make(examCoordinator, msg, Snackbar.LENGTH_SHORT).show();
        });

    }


    @Override
    public long getItemId(int position) {
        return sheetsList.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return sheetsList.size();
    }


    public class SheetViewHolder extends RecyclerView.ViewHolder
            implements ExamSpecificAdapterContextMenuListener {

        private CardView mCardView;
        private TextView sheetName;
        private TextView sheetDate;
        private TextView percentage;
        private CircularProgressBar progressBar;


        public SheetViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.sheet_card_view);
            sheetName = itemView.findViewById(R.id.sheet_name);
            sheetDate = itemView.findViewById(R.id.sheet_date);
            progressBar = itemView.findViewById(R.id.circular_progress);
            percentage = itemView.findViewById(R.id.percentage);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public boolean onMenuDeleteClick(int adapterPosition) {
            sheetsList.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
            notifyDataSetChanged();
            return true;
        }

        @Override
        public boolean onMenuEditClick(int adapterPosition) {
            Intent intent = new Intent(context, AddSheetActivity.class);
            intent.putExtra("sheetName", sheetName.getText().toString());
            intent.putExtra("sheetDate", sheetDate.getText().toString());
            intent.putExtra("points", sheetsList.get(adapterPosition).getPoints());
            intent.putExtra("maxPoints", sheetsList.get(adapterPosition).getMaxPoints());
            intent.putExtra("sheetID", adapterPosition);

            fragment.startActivityForResult(intent, 5439);
            return true;
        }

        void setCardProperties(Sheet sheet, float percentScore) {

            String scoreString = decimalFormat.format(percentScore) + "%";
            sheetName.setText(sheet.getSheetName());
            sheetDate.setText(sheet.getSheetDateText());
            percentage.setText(scoreString);
            progressBar.setProgressWithAnimation(percentScore);
        }
    }
}