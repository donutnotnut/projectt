package com.example.project;

import android.content.Context;
import android.content.Intent;
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

public class AdapterForAdminWorkers extends RecyclerView.Adapter<AdapterForAdminWorkers.WorkersViewHolder> {
    Context context;
    ArrayList<WorkerItem> array;
    public AdapterForAdminWorkers(Context context, ArrayList<WorkerItem> array) {
        this.context = context;
        this.array = array;
    }
    @NonNull
    @Override
    public AdapterForAdminWorkers.WorkersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.workeritem, parent, false);
        return new AdapterForAdminWorkers.WorkersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForAdminWorkers.WorkersViewHolder holder, int position) {
        holder.WorkerName.setText(array.get(holder.getAdapterPosition()).getName());
        holder.EditWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewWorker.class);
                intent.putExtra("id", array.get(holder.getAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });
        holder.DeleteWorker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this worker?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    Connection con = new ConnectionHelper().connectionclass();
                    try {
                        PreparedStatement ps = con.prepareStatement("DELETE FROM info WHERE ID=?");
                        ps.setInt(1, array.get(holder.getAdapterPosition()).getId());
                        ps.execute();
                        array.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    } catch (SQLException e) {
                        Log.e("error", e.getMessage());
                    }
                });
                builder.setNegativeButton("No", (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public static class WorkersViewHolder extends RecyclerView.ViewHolder {
        Button EditWorker;
        Button DeleteWorker;
        TextView WorkerName;
        public WorkersViewHolder(@NonNull View itemView) {
            super(itemView);
            EditWorker=itemView.findViewById(R.id.EditWorkerButtonWorkerItem);
            DeleteWorker=itemView.findViewById(R.id.DeleteWorkerButtonWorkerItem);
            WorkerName=itemView.findViewById(R.id.WorkerNameTextWorkersItem);

        }
    }
}
