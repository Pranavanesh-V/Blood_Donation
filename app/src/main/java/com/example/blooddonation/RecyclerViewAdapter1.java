package com.example.blooddonation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewAdapter1.ViewHolder> {
    private List<DataClass2> itemList;
    private OnItemClickListener clickListener;
    public RecyclerViewAdapter1(List<DataClass2> itemList, OnItemClickListener clickListener)
    {
        this.itemList = itemList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_lay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataClass2 item = itemList.get(position);
        holder.Name.setText(item.getDataName());
        holder.Blood_g.setText(item.getDataBlood());
        holder.Location.setText(item.getDataLocation());
        holder.txt.setText(item.getTxt());
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        TextView Blood_g;
        TextView Location;
        TextView txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.recName);
            Blood_g = itemView.findViewById(R.id.recBlood);
            Location = itemView.findViewById(R.id.recLocation);
            txt=itemView.findViewById(R.id.recTxt);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    clickListener.onItemClick(position); // Pass the position to the listener
                }
            });
        }
    }
}
