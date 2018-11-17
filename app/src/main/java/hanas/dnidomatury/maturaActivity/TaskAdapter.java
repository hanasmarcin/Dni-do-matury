package hanas.dnidomatury.maturaActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import hanas.dnidomatury.R;
import hanas.dnidomatury.matura.Matura;
import hanas.dnidomatury.matura.task.ListOfTasks;
import hanas.dnidomatury.matura.task.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private ListOfTasks toDoList;
    private Matura selectedMatura;
    private int doneHeaderID;

    public void incrementDoneHeaderID(){
        this.doneHeaderID++;
    }

    public void decrementDoneHeaderID(){
        this.doneHeaderID--;
    }

    public TaskAdapter(Context context, Matura selectedMatura, ListOfTasks toDoList) {
        this.context = context;
        this.selectedMatura = selectedMatura;
        this.toDoList = toDoList;
        toDoList.sort();

        for (int i=1; i<toDoList.sizeOfList(); i++){
            if (toDoList.getTask(i).isDoneHeader()){
                this.doneHeaderID=i;
    }
}
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_todo_task, viewGroup, false );
        return new TaskViewHolder(view, toDoList.sizeOfList()-1);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder taskViewHolder, final int taskID) {

        if(taskID == 0){
            taskViewHolder.setTaskIDHolder(taskID);
            taskViewHolder.layoutToHide.setVisibility(View.GONE);
            taskViewHolder.mCardView.setCardBackgroundColor(View.INVISIBLE);
            taskViewHolder.mCardView.setCardElevation(0);
            taskViewHolder.taskHeader.setVisibility(View.VISIBLE);
            taskViewHolder.taskHeader.setText("Do zrobienia");
            taskViewHolder.mCardView.setClickable(false);
        }
        else if(taskID == doneHeaderID){
            taskViewHolder.setTaskIDHolder(taskID);
            taskViewHolder.layoutToHide.setVisibility(View.GONE);
            taskViewHolder.mCardView.setCardBackgroundColor(View.INVISIBLE);
            taskViewHolder.mCardView.setCardElevation(0);
            taskViewHolder.taskHeader.setVisibility(View.VISIBLE);
            taskViewHolder.taskHeader.setText("Zrobione");
            taskViewHolder.mCardView.setClickable(false);
        }
        else{
            taskViewHolder.taskHeader.setVisibility(View.GONE);
            taskViewHolder.setTaskIDHolder(taskID);
            final Task task = toDoList.getTask(taskID);
            final int primaryColorID = selectedMatura.getPrimaryColorID(context);
            final int darkColorID = selectedMatura.getDarkColorID(context);
            taskViewHolder.stateImage.setImageResource(task.isDone() ? R.drawable.ic_clear : R.drawable.ic_confirm);
            taskViewHolder.taskName.setText(task.getTaskName());
            taskViewHolder.taskDate.setText(task.getTaskDateText());
            if (task.isDone()) {
                //TaskViewHolder.backgroundForIcon.setBackgroundColor(Color.GRAY);
                taskViewHolder.stateImage.setColorFilter(Color.GRAY);

                taskViewHolder.taskName.setTextColor(Color.GRAY);
                taskViewHolder.taskName.setPaintFlags(taskViewHolder.taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                //TaskViewHolder.backgroundForIcon.setBackgroundColor(ContextCompat.getColor(context, primaryColorID));
                taskViewHolder.stateImage.setColorFilter(ContextCompat.getColor(context, darkColorID));
                taskViewHolder.taskName.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
                taskViewHolder.taskName.setPaintFlags(taskViewHolder.taskName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }

            final TaskAdapter temp = this;

            taskViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    task.setDone(!task.isDone());
                    if (task.isDone()){
                        toDoList.deleteTask(task);
                        decrementDoneHeaderID();
                        selectedMatura.setTasksCounter(selectedMatura.getTasksCounter()-1);
                        toDoList.addTask(doneHeaderID+1, task);
                    }
                    else{
                        toDoList.deleteTask(task);
                        incrementDoneHeaderID();
                        selectedMatura.setTasksCounter(selectedMatura.getTasksCounter()+1);
                        toDoList.addTask(1, task);
                    }
                    notifyDataSetChanged();
                    toDoList.sort();
                }
            });


        }
    }

    @Override
    public long getItemId(int position) {
        return toDoList.getTask(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return toDoList.sizeOfList();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder
        implements View.OnCreateContextMenuListener{

        int taskIDHolder;
        CardView mCardView;
        ConstraintLayout backgroundForIcon;
        ImageView stateImage;
        TextView taskName;
        TextView taskDate;
        TextView taskHeader;
        ConstraintLayout layoutToHide;
        ImageView upButton;
        ImageView downButton;

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            if(taskIDHolder!=0 && taskIDHolder!=doneHeaderID){
                MenuItem deleteMenuItem = menu.add(Menu.NONE, 1, 1, "Usuń");
                MenuItem editMenuItem = menu.add(Menu.NONE, 2, 2, "Edytuj");

                deleteMenuItem.setOnMenuItemClickListener(onEditMenu);
                editMenuItem.setOnMenuItemClickListener(onEditMenu);
            }
        }


        public TaskViewHolder(@NonNull View itemView, int taskID) {
            super(itemView);
            this.taskIDHolder = taskID;
            //Toast.makeText(, "IDVH "+taskIDHolder, Toast.LENGTH_SHORT).show();
            mCardView = itemView.findViewById(R.id.to_do_card_view);
            //taskDeleteButton = itemView.findViewById(R.id.task_delete_button);
            //taskEditButton = itemView.findViewById(R.id.task_edit_button);
            stateImage = itemView.findViewById(R.id.state_image);
            taskName = itemView.findViewById(R.id.task_name);
            taskDate = itemView.findViewById(R.id.task_date);
            backgroundForIcon = itemView.findViewById(R.id.background_for_icon);
            taskHeader = itemView.findViewById(R.id.todo_task_header);
            layoutToHide = itemView.findViewById(R.id.task_to_hide);
            itemView.setOnCreateContextMenuListener(this);

        }

        public void setTaskIDHolder(int taskIDHolder) {
            this.taskIDHolder = taskIDHolder;
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1: {
                        //long tmpID = getItemCount();
                        //Toast.makeText(context, "ID "+tmpID, Toast.LENGTH_SHORT).show();
                        //
                        if(!toDoList.getTask(taskIDHolder).isDone()) {
                            decrementDoneHeaderID();
                            selectedMatura.setTasksCounter(selectedMatura.getTasksCounter()-1);
                        }

                        toDoList.deleteTask(toDoList.getTask(taskIDHolder));

                        notifyItemRemoved(taskIDHolder);
                        notifyDataSetChanged();
                        toDoList.sort();
                        break;
                    }
                    case 2:{
                        Toast.makeText(context, "ID " + taskIDHolder, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, AddTaskActivity.class);
                        intent.putExtra("taskName", taskName.getText().toString());
                        intent.putExtra("taskDate", taskDate.getText().toString());
                        intent.putExtra("taskID", taskIDHolder);
                        ((Activity) context).startActivityForResult(intent, 5636);

                        break;
                    }

                }
                return true;
            }
        };
    }
}