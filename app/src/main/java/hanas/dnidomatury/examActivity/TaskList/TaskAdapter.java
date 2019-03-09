package hanas.dnidomatury.examActivity.TaskList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import hanas.dnidomatury.R;
import hanas.dnidomatury.examActivity.ExamSpecificAdapterContextMenuListener;
import hanas.dnidomatury.model.ExamSpecificList;
import hanas.dnidomatury.model.task.Task;

import static hanas.dnidomatury.model.task.Task.TaskHeader.DONE;
import static hanas.dnidomatury.model.task.Task.TaskHeader.TODO;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Fragment fragment;
    private Context context;
    private ExamSpecificList<Task> tasksList;
    private final int darkColorID;


    public TaskAdapter(Fragment fragment, ExamSpecificList<Task> tasks, int darkColorID) {
        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.tasksList = tasks;
        this.darkColorID = darkColorID;
        tasks.sort();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_todo_task, viewGroup, false);

        // Create new view holder for each element
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder taskViewHolder, final int adapterId) {
        final Task task = tasksList.get(adapterId);
        // If task is a header and not a proper task
        if (task.getHeader().equals(TODO) || task.getHeader().equals(DONE)) {
            // Set card property for headers
            taskViewHolder.setCardPropertiesForHeader(adapterId);

        } else {
            // Set card properties for task
            taskViewHolder.setCardPropertiesForTask(task);

            //Set listener for the card
            taskViewHolder.mCardView.setOnClickListener(view -> {
                task.setDone(!task.isDone());
                tasksList.sort();
                notifyDataSetChanged();
            });


        }
    }

    @Override
    public long getItemId(int position) {
        return tasksList.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder
            implements ExamSpecificAdapterContextMenuListener {

        private CardView mCardView;
        private ImageView stateImage;
        private TextView taskName;
        private TextView taskDate;
        private TextView taskHeader;
        private ConstraintLayout layoutToHide;


        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.to_do_card_view);
            stateImage = itemView.findViewById(R.id.state_image);
            taskName = itemView.findViewById(R.id.task_name);
            taskDate = itemView.findViewById(R.id.task_date);
            taskHeader = itemView.findViewById(R.id.todo_task_header);
            layoutToHide = itemView.findViewById(R.id.task_to_hide);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public boolean onMenuEditClick(int viewHolderID) {
            Intent intent = new Intent(context, AddTaskActivity.class);
            intent.putExtra("taskName", taskName.getText().toString());
            intent.putExtra("taskDate", taskDate.getText().toString());
            intent.putExtra("taskID", viewHolderID);

            fragment.startActivityForResult(intent, 5636);

            return true;
        }

        @Override
        public boolean onMenuDeleteClick(int viewHolderID) {
            tasksList.remove(viewHolderID);
            notifyItemRemoved(viewHolderID);
            notifyDataSetChanged();
            return true;
        }

        private void setCardPropertiesForHeader(int adapterId) {
            layoutToHide.setVisibility(View.GONE);
            mCardView.setCardBackgroundColor(View.INVISIBLE);
            mCardView.setCardElevation(0);
            taskHeader.setVisibility(View.VISIBLE);
            taskHeader.setText(adapterId == 0 ? "Do zrobienia" : "Zrobione");
            mCardView.setEnabled(false);
        }

        private void setCardPropertiesForTask(Task task) {
            taskHeader.setVisibility(View.GONE);
            stateImage.setImageResource(task.isDone() ? R.drawable.ic_clear : R.drawable.ic_confirm);
            taskName.setText(task.getTaskName());
            taskDate.setText(task.getTaskDateText());
            if (task.isDone()) {
                // Set card properties for task that is done
                stateImage.setColorFilter(Color.GRAY);
                taskName.setTextColor(Color.GRAY);
                taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                // Set card properties for task that isn't done
                stateImage.setColorFilter(ContextCompat.getColor(context, darkColorID));
                taskName.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
                taskName.setPaintFlags(taskName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }

    }
}
