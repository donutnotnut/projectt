package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterAdminNextWeek extends RecyclerView.Adapter<AdapterAdminNextWeek.ViewHolder> {
    ArrayList<NextWeekScheduleItem> array;
    Context context;
    public AdapterAdminNextWeek(ArrayList<NextWeekScheduleItem> array, Context context) {
        this.array = array;
        this.context = context;
    }
    @NonNull
    @Override
    public AdapterAdminNextWeek.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.workernextweekitem, parent, false);
        return new AdapterAdminNextWeek.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAdminNextWeek.ViewHolder holder, int position) {
        holder.name.setText(array.get(position).getName());
        holder.Sunday.setChecked(array.get(position).getSunday());
        holder.Monday.setChecked(array.get(position).getMonday());
        holder.Tuesday.setChecked(array.get(position).getTuesday());
        holder.Wednesday.setChecked(array.get(position).getWednesday());
        holder.Thursday.setChecked(array.get(position).getThursday());
        holder.Friday.setChecked(array.get(position).getFriday());
        holder.Saturday.setChecked(array.get(position).getSaturday());
        holder.Sunday.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                array.get(position).setSunday(isChecked);
            }
        }));
        holder.Monday.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                array.get(position).setMonday(isChecked);
            }
        }));
        holder.Tuesday.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                array.get(position).setTuesday(isChecked);
            }
        }));
        holder.Wednesday.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                array.get(position).setWednesday(isChecked);
            }
        }));
        holder.Thursday.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                array.get(position).setThursday(isChecked);
            }
        }));
        holder.Friday.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                array.get(position).setFriday(isChecked);
            }
        }));
        holder.Saturday.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                array.get(position).setSaturday(isChecked);
            }
        }));

    }

    @Override
    public int getItemCount() {
        return array.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox Sunday;
        CheckBox Monday;
        CheckBox Tuesday;
        CheckBox Wednesday;
        CheckBox Thursday;
        CheckBox Friday;
        CheckBox Saturday;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Sunday=itemView.findViewById(R.id.Sundaycheckbox);
            Monday=itemView.findViewById(R.id.Mondaycheckbox);
            Tuesday=itemView.findViewById(R.id.Tuesdaycheckbox);
            Wednesday=itemView.findViewById(R.id.Wednesdaycheckbox);
            Thursday=itemView.findViewById(R.id.Thursdaycheckbox);
            Friday=itemView.findViewById(R.id.Fridaycheckbox);
            Saturday=itemView.findViewById(R.id.Saturdaycheckbox);
            name=itemView.findViewById(R.id.NameHolder);
        }
    }

}
