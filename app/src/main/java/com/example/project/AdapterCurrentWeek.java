package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Adapter for displaying workdayitem objects in a RecyclerView for the current week.
 */
public class AdapterCurrentWeek extends RecyclerView.Adapter<AdapterCurrentWeek.ViewHolder> {
    ArrayList<workdayitem> array;
    Context context;

    /**
     * Constructs an AdapterCurrentWeek with the provided data array and context.
     *
     * @param array   The ArrayList of workdayitem objects to be displayed
     * @param context The context used to access resources and system services
     */
    public AdapterCurrentWeek(ArrayList<workdayitem> array, Context context) {
        this.array = array;
        this.context = context;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position
     * @param viewType The view type of the new View
     * @return A new ViewHolder that holds a View of the given view type
     */
    @NonNull
    @Override
    public AdapterCurrentWeek.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.currentweekshiftlayout, parent, false);
        return new AdapterCurrentWeek.ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position
     * @param position The position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(@NonNull AdapterCurrentWeek.ViewHolder holder, int position) {
        holder.DayName.setText(array.get(position).getDay());
        for (int i = 0; i < array.get(position).getNames().size(); i++) {
            if (i == array.get(position).getNames().size() - 1) {
                holder.Works.append(array.get(position).getNames().get(i));
            } else {
                holder.Works.append(array.get(position).getNames().get(i) + " , ");
            }
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter
     */
    @Override
    public int getItemCount() {
        return array.size();
    }

    /**
     * ViewHolder for caching View components of the item layout.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView DayName;
        TextView Works;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            DayName = itemView.findViewById(R.id.DayOfWeek);
            Works = itemView.findViewById(R.id.WorkingWithText);
        }
    }
}
