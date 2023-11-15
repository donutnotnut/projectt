package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterCurrentWeek extends RecyclerView.Adapter<AdapterCurrentWeek.ViewHolder> {
    ArrayList<workdayitem> array;
    Context context;
    public AdapterCurrentWeek(ArrayList<workdayitem> array, Context context) {
        this.array = array;
        this.context = context;
    }
    @NonNull
    @Override
    public AdapterCurrentWeek.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.currentweekshiftlayout, parent, false);
        return new AdapterCurrentWeek.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCurrentWeek.ViewHolder holder, int position) {
        holder.DayName.setText(array.get(position).getDay());
        for (int i =0; i<array.get(position).getNames().size();i++){
            if (i== array.size()-2) {
                holder.Works.append(array.get(position).getNames().get(i));
            }
            else
                holder.Works.append(array.get(position).getNames().get(i)+" , ");
        }
    }

    @Override
    public int getItemCount() {
        return array.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView DayName;
        TextView Works;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            DayName=itemView.findViewById(R.id.DayOfWeek);
            Works=itemView.findViewById(R.id.WorkingWithText);
        }
    }
}
