package com.example.blooddonation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<DataClass> itemList;
    private OnItemClickListener clickListener;
    public RecyclerViewAdapter(List<DataClass> itemList, OnItemClickListener clickListener)
    {
        this.itemList = itemList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataClass item = itemList.get(position);
        holder.Name.setText(item.getDataName());
        holder.Blood_g.setText(item.getDataBlood());
        holder.Phone_no.setText(item.getDataPhone());
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        TextView Blood_g;
        TextView Phone_no;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.recName);
            Blood_g = itemView.findViewById(R.id.recBlood);
            Phone_no = itemView.findViewById(R.id.recPhoneNo);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    clickListener.onItemClick(position); // Pass the position to the listener
                }
            });
        }
    }
}
