package com.example.tasimwithyouapp.hadas;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasimwithyouapp.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataSet;

    public CustomAdapter(ArrayList<DataModel> dataSet) {

        this.dataSet = dataSet;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int listPosition) {
        DataModel model = dataSet.get(listPosition);
        TextView textViewName = viewHolder.textViewName;
        TextView textViewVersion = viewHolder.textViewVersion;
        ImageView imageView = viewHolder.imageViewIcon;
        CardView cardView = viewHolder.cardView;
        textViewName.setText(dataSet.get(listPosition).getName());
        imageView.setImageResource(dataSet.get(listPosition).getImage());
        if (model.onDataModelClick != null)
            cardView.setOnClickListener((v) -> model.onDataModelClick.onClick());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}


