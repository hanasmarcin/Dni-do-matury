package hanas.dnidomatury;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hanas.dnidomatury.matura.Matura;

public class FullListMaturaAdapter extends RecyclerView.Adapter<FullListMaturaAdapter.FullListMaturaViewHolder> {

    private Context context;
    private List<Matura> fullMaturaList;

    public FullListMaturaAdapter(Context context, List<Matura> fullMaturaList) {
        this.context = context;
        this.fullMaturaList = fullMaturaList;
    }

    @NonNull
    @Override
    public FullListMaturaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_select, viewGroup, false );
        return new FullListMaturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FullListMaturaViewHolder fullListMaturaViewHolder, int i) {
        final Matura mMatura = fullMaturaList.get(i);
        final int primaryColorID = mMatura.getPrimaryColorID(context);
        final int darkColorID = mMatura.getDarkColorID(context);

        fullListMaturaViewHolder.everyListTitle.setText(mMatura.getName());
        fullListMaturaViewHolder.everyListPoziom.setText(mMatura.getType() + " " + mMatura.getLevel());
        //fullListMaturaViewHolder.everyListCardView.setCardBackgroundColor(mMatura.isSelected() ? Color.WHITE : ContextCompat.getColor(context, R.color.veryLightGrey));

        if(primaryColorID!=0) fullListMaturaViewHolder.primaryColorField.setBackgroundColor(mMatura.isSelected() ? ContextCompat.getColor(context, primaryColorID) : Color.WHITE);
        if(darkColorID!=0) fullListMaturaViewHolder.everyListEditButton.setTextColor(mMatura.isSelected() ? ContextCompat.getColor(context, darkColorID) : Color.GRAY);

        fullListMaturaViewHolder.everyListCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Wybrales " + mMatura.getName() , Toast.LENGTH_SHORT).show();
                mMatura.setSelected(!mMatura.isSelected());

                if(primaryColorID!=0) fullListMaturaViewHolder.primaryColorField.setBackgroundColor(mMatura.isSelected() ? ContextCompat.getColor(context, primaryColorID) : Color.WHITE);
                if(darkColorID!=0) fullListMaturaViewHolder.everyListEditButton.setTextColor(mMatura.isSelected() ? ContextCompat.getColor(context, darkColorID) : Color.GRAY);

               // fullListMaturaViewHolder.everyListCardView.setCardBackgroundColor(mMatura.isSelected() ? Color.WHITE : ContextCompat.getColor(context, R.color.veryLightGrey));
            }
        });

        fullListMaturaViewHolder.everyListEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return fullMaturaList.size();
    }

    public class FullListMaturaViewHolder extends RecyclerView.ViewHolder{

        TextView everyListTitle;
        TextView everyListPoziom;
        CardView everyListCardView;
        LinearLayout primaryColorField;
        Button everyListEditButton;

        public FullListMaturaViewHolder(@NonNull View itemView) {
            super(itemView);

            primaryColorField = itemView.findViewById(R.id.primary_color_full_list);
            everyListCardView = itemView.findViewById(R.id.everyListCardView);
            everyListTitle = itemView.findViewById(R.id.every_list_title);
            everyListPoziom = itemView.findViewById(R.id.every_list_poziom);
            everyListEditButton = itemView.findViewById(R.id.button_edit_full_list);
        }
    }
}
