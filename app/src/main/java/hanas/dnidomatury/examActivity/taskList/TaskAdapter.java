package hanas.dnidomatury.examActivity.taskList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import hanas.dnidomatury.R;
import hanas.dnidomatury.examActivity.ExamSpecificAdapterContextMenuListener;
import hanas.dnidomatury.model.examSpecific.ExamItemsList;
import hanas.dnidomatury.model.examSpecific.task.Task;
import hanas.dnidomatury.model.examSpecific.task.TasksList;

import static hanas.dnidomatury.examActivity.taskList.TaskListFragment.EDIT_TASK_REQUEST_CODE;
import static hanas.dnidomatury.model.examSpecific.task.Task.TaskHeader.DONE;
import static hanas.dnidomatury.model.examSpecific.task.Task.TaskHeader.TODO;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final Fragment fragment;
    private final Context context;
    private final ExamItemsList<Task> tasksList;
    private final int darkColorID;


    TaskAdapter(Fragment fragment, ExamItemsList<Task> tasks, @ColorRes int darkColorID) {
        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.tasksList = tasks;
        this.darkColorID = darkColorID;
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
            taskViewHolder.cardView.setOnClickListener(view ->
                    new MyAsync(this, taskViewHolder.getAdapterPosition(), task).execute((TasksList) tasksList));
        }
    }

    static class MyAsync extends AsyncTask<TasksList, Void, int[]> {

        final WeakReference<TaskAdapter> adapterReference;
        final int adapterPosition;
        final Task task;

        MyAsync(TaskAdapter adapter, int adapterPosition, Task task) {
            this.adapterReference = new WeakReference<>(adapter);
            this.adapterPosition = adapterPosition;
            this.task = task;
        }

        @Override
        protected int[] doInBackground(TasksList... lists) {
            int oldPosition = adapterPosition;
            task.setDone(!task.isDone());
            int newPosition = lists[0].moveAndSort(adapterPosition, task.isDone());
            return new int[]{oldPosition, newPosition};
        }

        @Override
        protected void onPostExecute(int[] positions) {
            TaskAdapter adapter = adapterReference.get();
            if (adapter == null) return;
            adapter.notifyItemMoved(positions[0], positions[1]);
            adapter.notifyItemChanged(positions[1]);
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

        private final CardView cardView;
        private final ImageView stateImage;
        private final TextView taskName;
        private final TextView taskDate;
        private final TextView taskHeader;
        private final ConstraintLayout layoutToHide;


        TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.to_do_card_view);
            stateImage = itemView.findViewById(R.id.state_image);
            taskName = itemView.findViewById(R.id.task_name);
            taskDate = itemView.findViewById(R.id.task_date);
            taskHeader = itemView.findViewById(R.id.todo_task_header);
            layoutToHide = itemView.findViewById(R.id.task_to_hide);

            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public boolean onMenuEditClick(int viewHolderID) {
            // Create new dialogFragment responsible for editing task and show it
            AddTaskFragment addTaskDialog = AddTaskFragment.forEdit(getAdapterPosition(), taskName.getText().toString(), taskDate.getText().toString());
            addTaskDialog.setTargetFragment(fragment, EDIT_TASK_REQUEST_CODE);
            addTaskDialog.show(fragment.getFragmentManager(), "WTF2");

            return true;
        }

        @Override
        public boolean onMenuDeleteClick(int viewHolderID) {
            tasksList.remove(viewHolderID);
            notifyItemRemoved(viewHolderID);
            return true;
        }

        private void setCardPropertiesForHeader(int adapterId) {
            layoutToHide.setVisibility(View.GONE);
            cardView.setCardBackgroundColor(View.INVISIBLE);
            cardView.setCardElevation(0);
            taskHeader.setVisibility(View.VISIBLE);
            taskHeader.setText(adapterId == 0 ? "Do zrobienia" : "Zrobione");
            cardView.setEnabled(false);
        }

        private void setCardPropertiesForTask(Task task) {
            taskHeader.setVisibility(View.GONE);
            layoutToHide.setVisibility(View.VISIBLE);
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.design_default_color_surface));
            cardView.setCardElevation(5.25F);
            cardView.setEnabled(true);
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
