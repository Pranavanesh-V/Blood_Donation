package com.example.blooddonation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


//RecyclerViewAdapter class for Requester lists
public class History_RecyclerViewAdapter extends RecyclerView.Adapter<History_RecyclerViewAdapter.ViewHolder> {
    private List<History_dataClass> itemList;
    private History_interface clickListener;
    private Context context;

    //Gets the details of the Requester from the home page
    //This is a constructor
    public History_RecyclerViewAdapter(List<History_dataClass> itemList, History_interface clickListener, Context context)
    {
        this.itemList = itemList;
        this.clickListener = clickListener;
        this.context=context;
    }

    //Creates a ViewHolder to Place the data
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History_dataClass dataC=itemList.get(position);
        holder.Name.setText(dataC.getName());
        holder.Blood_g.setText(dataC.getBlood_Group());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        TextView Blood_g;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.hisName);
            Blood_g = itemView.findViewById(R.id.hisBlood);

            //Gets the position to see which item is clicked
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    clickListener.ItemOnClick(position); // Pass the position to the listener
                }
            });
        }
    }
}
