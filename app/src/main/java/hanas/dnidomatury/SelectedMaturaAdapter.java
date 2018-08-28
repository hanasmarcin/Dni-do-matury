package hanas.dnidomatury;

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
import android.widget.Toast;

import java.util.List;

public class SelectedMaturaAdapter extends RecyclerView.Adapter<SelectedMaturaAdapter.SelectedMaturaViewHolder> {

    private Context context;
    private List<Matura> maturaList;
    private MaturaTimer maturaTimer = new MaturaTimer();

    public SelectedMaturaAdapter(Context context, List<Matura> maturaList) {
        this.context = context;
        this.maturaList = maturaList;
    }

    @NonNull
    @Override
    public SelectedMaturaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_main_card, viewGroup, false );
        return new SelectedMaturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectedMaturaViewHolder maturaViewHolder, final int i) {

        final Matura mMatura = maturaList.get(i);
        int darkColorID = mMatura.getDarkColorID(context);
        int primaryColorID = mMatura.getPrimaryColorID(context);

        maturaViewHolder.maturaNameTextView.setText(mMatura.getName());
        maturaViewHolder.maturaLevelTypeTextView.setText(mMatura.getLevel() + " " + mMatura.getType());
        maturaViewHolder.tasksCounter.setText(""+mMatura.getTasksCounter());
        if(darkColorID!=0) maturaViewHolder.darkColorField.setBackgroundColor(ContextCompat.getColor(context, darkColorID));
        if (primaryColorID!=0) maturaViewHolder.primaryColorField.setBackgroundColor(ContextCompat.getColor(context, primaryColorID));
        maturaTimer.startMaturaTimer(context, mMatura, maturaViewHolder.daysTextView, maturaViewHolder.hoursTextVIew);

        maturaViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MaturaActivity.class);
                intent.putExtra("selectedMaturaID", mMatura.getMaturaID());
                ((Activity)view.getContext()).startActivityForResult(intent, MainActivity.getMainToMaturaRequestCode());

            }
        });
    }

    @Override
    public int getItemCount() {
        return maturaList.size();
    }

    public class SelectedMaturaViewHolder extends RecyclerView.ViewHolder {

        CardView mCardView;
        LinearLayout darkColorField;
        LinearLayout primaryColorField;
        TextView daysTextView;
        TextView minutesTextView;
        TextView hoursTextVIew;
        TextView maturaNameTextView;
        TextView maturaLevelTypeTextView;
        TextView tasksCounter;

        public SelectedMaturaViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.cardView);
            darkColorField = itemView.findViewById(R.id.dark_color_field);
            primaryColorField = itemView.findViewById(R.id.primary_color_field);
            daysTextView = itemView.findViewById(R.id.dni_counter_list);
            minutesTextView = itemView.findViewById(R.id.minuty_list);
            hoursTextVIew = itemView.findViewById(R.id.godziny_list);
            maturaNameTextView = itemView.findViewById(R.id.matura_list);
            maturaLevelTypeTextView = itemView.findViewById(R.id.poziom_typ_list);
            tasksCounter = itemView.findViewById(R.id.matura_task_counter);
        }
    }
}
