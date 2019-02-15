package hanas.dnidomatury.maturaListActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import hanas.dnidomatury.maturaActivity.MaturaActivity;
import hanas.dnidomatury.matura.MaturaTimer;
import hanas.dnidomatury.R;
import hanas.dnidomatury.matura.Matura;

public class MaturaAdapter extends RecyclerView.Adapter<MaturaAdapter.MaturaViewHolder> {

    private Context context;
    private List<Matura> maturaList;

    public MaturaAdapter(Context context, List<Matura> maturaList) {
        this.context = context;
        this.maturaList = maturaList;
    }

    @NonNull
    @Override
    public MaturaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_matura, viewGroup, false );
        return new MaturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MaturaViewHolder maturaViewHolder, final int i) {

        final Matura mMatura = maturaList.get(i);
        //ListOfTasks lot = new ListOfTasks(mMatura.getName(), mMatura.getType(), mMatura.getLevel());
        //lot.readFromFile(context);
        int darkColorID = mMatura.getDarkColorID(context);
        int primaryColorID = mMatura.getPrimaryColorID(context);

        maturaViewHolder.maturaNameTextView.setText(mMatura.getName());
        maturaViewHolder.maturaLevelTypeTextView.setText(mMatura.getLevel() + " " + mMatura.getType());
        maturaViewHolder.tasksCounter.setText(""+mMatura.getTasksCounter());
        if(darkColorID!=0) maturaViewHolder.darkColorField.setBackgroundColor(ContextCompat.getColor(context, darkColorID));
        if(darkColorID!=0) maturaViewHolder.mCardView.setBackgroundColor(ContextCompat.getColor(context, darkColorID));
        if (primaryColorID!=0) maturaViewHolder.primaryColorField.setBackgroundColor(ContextCompat.getColor(context, primaryColorID));

        if (maturaViewHolder.maturaTimer.getTimer() != null) maturaViewHolder.maturaTimer.getTimer().cancel();
        maturaViewHolder.maturaTimer.startMaturaTimer(context, mMatura, maturaViewHolder.daysTextView, maturaViewHolder.hoursTextVIew);



        maturaViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MaturaActivity.class);
                intent.putExtra("selectedMaturaPOS", maturaViewHolder.getAdapterPosition());
                //intent.putExtra("selectedMaturaID", maturaList.get(maturaViewHolder.getAdapterPosition()).getMaturaID());
                ((Activity)view.getContext()).startActivityForResult(intent, MaturaListActivity.getMainToMaturaRequestCode());
            }
        });
    }

    @Override
    public int getItemCount() {
        return maturaList.size();
    }


    public class MaturaViewHolder extends RecyclerView.ViewHolder {

        CardView mCardView;
        LinearLayout darkColorField;
        LinearLayout primaryColorField;
        TextView daysTextView;
        //TextView minutesTextView;
        TextView hoursTextVIew;
        TextView maturaNameTextView;
        TextView maturaLevelTypeTextView;
        TextView tasksCounter;
        MaturaTimer maturaTimer;


        /*@Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {

                MenuItem deleteMenuItem = menu.add(Menu.NONE, 1, 1, "Usuń");
                MenuItem editMenuItem = menu.add(Menu.NONE, 2, 2, "Edytuj");

                deleteMenuItem.setOnMenuItemClickListener(onEditMenu);
                editMenuItem.setOnMenuItemClickListener(onEditMenu);

        }*/

        public MaturaViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.cardView);
            darkColorField = itemView.findViewById(R.id.dark_color_field);
            primaryColorField = itemView.findViewById(R.id.primary_color_field);
            daysTextView = itemView.findViewById(R.id.dni_counter_list);
            //minutesTextView = itemView.findViewById(R.id.minuty_list);
            hoursTextVIew = itemView.findViewById(R.id.godziny_list);
            maturaNameTextView = itemView.findViewById(R.id.matura_list);
            maturaLevelTypeTextView = itemView.findViewById(R.id.poziom_typ_list);
            tasksCounter = itemView.findViewById(R.id.matura_task_counter);
            maturaTimer = new MaturaTimer();

            //itemView.setOnCreateContextMenuListener(this);
        }
    }


        /*private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1: {
                        //long tmpID = getItemCount();
                        //Toast.makeText(context, "ID "+tmpID, Toast.LENGTH_SHORT).show();
                        //
                        maturaList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        //notifyDataSetChanged();
                        break;
                    }
                    case 2:{

                        break;
                    }

                }
                return true;
            }
        };*/


}
