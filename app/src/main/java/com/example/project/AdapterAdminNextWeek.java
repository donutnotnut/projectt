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

/**
 * Adapter for displaying NextWeekScheduleItem objects in a RecyclerView.
 */
public class AdapterAdminNextWeek extends RecyclerView.Adapter<AdapterAdminNextWeek.ViewHolder> {
    ArrayList<NextWeekScheduleItem> array;
    Context context;

    /**
     * Constructs an AdapterAdminNextWeek with the provided data array and context.
     *
     * @param array   The ArrayList of NextWeekScheduleItem objects to be displayed
     * @param context The context used to access resources and system services
     */
    public AdapterAdminNextWeek(ArrayList<NextWeekScheduleItem> array, Context context) {
        this.array = array;
        this.context = context;
    }

    /**
    * Inflates the layout for each item in the RecyclerView.
    *
    * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position
    * @param viewType The view type of the new View
    * @return A new ViewHolder that holds a View of the given view type
    */
    @NonNull
    @Override
    public AdapterAdminNextWeek.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.workernextweekitem, parent, false);
        return new AdapterAdminNextWeek.ViewHolder(view);
    }

    /**
    * Binds the data to the views in the ViewHolder.
    *
    * @param holder    The ViewHolder to bind the data to
    * @param position  The position of the item in the data set
    */
    @Override
    public void onBindViewHolder(@NonNull AdapterAdminNextWeek.ViewHolder holder, int position) {
        // Set the name TextView to the name of the item at the given position
        holder.name.setText(array.get(position).getName());

        // Set the checked state of each day CheckBox according to the corresponding value in the data
        holder.Sunday.setChecked(array.get(position).getSunday());
        holder.Monday.setChecked(array.get(position).getMonday());
        holder.Tuesday.setChecked(array.get(position).getTuesday());
        holder.Wednesday.setChecked(array.get(position).getWednesday());
        holder.Thursday.setChecked(array.get(position).getThursday());
        holder.Friday.setChecked(array.get(position).getFriday());
        holder.Saturday.setChecked(array.get(position).getSaturday());

        // Set OnCheckedChangeListener for each day CheckBox to update the corresponding value in the data
        holder.Sunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                array.get(position).setSunday(isChecked);
            }
        });
        holder.Monday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                array.get(position).setMonday(isChecked);
            }
        });
        holder.Tuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                array.get(position).setTuesday(isChecked);
            }
        });
        holder.Wednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                array.get(position).setWednesday(isChecked);
            }
        });
        holder.Thursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                array.get(position).setThursday(isChecked);
            }
        });
        holder.Friday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                array.get(position).setFriday(isChecked);
            }
        });
        holder.Saturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                array.get(position).setSaturday(isChecked);
            }
        });
    }

    /**
     *
     * @return size Returns the length of the array
     */
    @Override
    public int getItemCount() {
        return array.size();
    }

    //This code defines a ViewHolder class for a RecyclerView in an Android app, managing views for days of the week checkboxes and a name text view.
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
