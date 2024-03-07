package com.example.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Adapter for displaying WorkerItem objects in a RecyclerView for the admin interface.
 */
public class AdapterForAdminWorkers extends RecyclerView.Adapter<AdapterForAdminWorkers.WorkersViewHolder> {
    Context context;
    ArrayList<WorkerItem> array;

    /**
     * Constructs an AdapterForAdminWorkers with the provided context and data array.
     *
     * @param context The context used to access resources and system services
     * @param array   The ArrayList of WorkerItem objects to be displayed
     */
    public AdapterForAdminWorkers(Context context, ArrayList<WorkerItem> array) {
        this.context = context;
        this.array = array;
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
    public AdapterForAdminWorkers.WorkersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.workeritem, parent, false);
        return new AdapterForAdminWorkers.WorkersViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position
     * @param position The position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(@NonNull AdapterForAdminWorkers.WorkersViewHolder holder, int position) {
        holder.WorkerName.setText(array.get(holder.getAdapterPosition()).getName());

        // Set up OnClickListener for editing the worker
        holder.EditWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewWorker.class);
                intent.putExtra("id", array.get(holder.getAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });

        // Set up OnClickListener for deleting the worker
        holder.DeleteWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this worker?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    @SuppressLint("StaticFieldLeak") AsyncTask asyncTask = new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] objects) {
                            try {
                                Connection con = new ConnectionHelper().connectionclass();
                                PreparedStatement ps = con.prepareStatement("DELETE FROM info WHERE ID=?");
                                ps.setInt(1, array.get(holder.getAdapterPosition()).getId());
                                ps.execute();
                                ps = con.prepareStatement("DELETE FROM NextWeek WHERE WorkerID=?");
                                ps.setInt(1, array.get(holder.getAdapterPosition()).getId());
                                ps.execute();
                                ps = con.prepareStatement("DELETE FROM currentweek WHERE WorkerID=?");
                                ps.setInt(1, array.get(holder.getAdapterPosition()).getId());
                                ps.execute();
                                array.remove(holder.getAdapterPosition());
                            } catch (Exception e) {
                                Log.e("error", e.getMessage());
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            super.onPostExecute(o);
                            notifyDataSetChanged();
                        }
                    };
                    asyncTask.execute();
                });
                builder.setNegativeButton("No", (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
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
    public static class WorkersViewHolder extends RecyclerView.ViewHolder {
        Button EditWorker;
        Button DeleteWorker;
        TextView WorkerName;

        public WorkersViewHolder(@NonNull View itemView) {
            super(itemView);
            EditWorker = itemView.findViewById(R.id.EditWorkerButtonWorkerItem);
            DeleteWorker = itemView.findViewById(R.id.DeleteWorkerButtonWorkerItem);
            WorkerName = itemView.findViewById(R.id.WorkerNameTextWorkersItem);
        }
    }
}
