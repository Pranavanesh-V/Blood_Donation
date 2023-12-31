package com.example.blooddonation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<DataClass> itemList;
    private OnItemClickListener clickListener;
    private Context context;
    public RecyclerViewAdapter(List<DataClass> itemList, OnItemClickListener clickListener,Context context)
    {
        this.itemList = itemList;
        this.clickListener = clickListener;
        this.context=context;
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
        holder.Location.setText(item.getDataLocation());
        if (!item.getDataURL().equals("No"))
        {
            System.out.println(item.getDataURL());
            Glide.with(context)
                    .load(item.getDataURL())
                    .into(holder.recImage);
        }
        else
        {
            holder.recImage.setImageResource(R.drawable.logo_blood);
        }

    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        TextView Blood_g;
        TextView Location;
        ImageView recImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.recName);
            Blood_g = itemView.findViewById(R.id.recBlood);
            Location = itemView.findViewById(R.id.recLocation);
            recImage=itemView.findViewById(R.id.recImage);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    clickListener.onItemClick(position); // Pass the position to the listener
                }
            });
        }
    }
}
