package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShiftsAdapter extends RecyclerView.Adapter<ShiftsAdapter.ShiftsViewHolder> {
    Context context;
    ArrayList<ShiftObject> array;
    public ShiftsAdapter(Context context, ArrayList<ShiftObject> array) {
        this.context = context;
        this.array = array;
    }
    @NonNull
    @Override
    public ShiftsAdapter.ShiftsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.shiftitem, parent, false);
        return new ShiftsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftsAdapter.ShiftsViewHolder holder, int position) {
        holder.StartTime.setText(array.get(holder.getAdapterPosition()).getStartDate());
        holder.EndTime.setText(array.get(holder.getAdapterPosition()).getEndDate());
        holder.WorkerNameTextShiftItem.setText(array.get(holder.getAdapterPosition()).getWorkername());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Editingshift.class);
                intent.putExtra("id", array.get(holder.getAdapterPosition()).getWorkerid());
                intent.putExtra("ShiftID", array.get(holder.getAdapterPosition()).getShiftId());
                intent.putExtra("StartTime", array.get(holder.getAdapterPosition()).getStartTime().getTime());
                intent.putExtra("EndTime", array.get(holder.getAdapterPosition()).getEndTime().getTime());
                context.startActivity(intent);
                Activity activity = (Activity) context;
                activity.finish();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connection = new ConnectionHelper().connectionclass();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM shifthistory WHERE ShiftID = ?");
                    preparedStatement.setInt(1, array.get(holder.getAdapterPosition()).getShiftId());
                    preparedStatement.executeUpdate();
                    connection.close();
                    array.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                } catch (SQLException e) {
                    Log.e("error while deleting", e.getMessage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }
    public static class ShiftsViewHolder extends RecyclerView.ViewHolder {
        TextView StartTime;
        TextView EndTime;
        TextView WorkerNameTextShiftItem;
        Button button;
        Button delete;
        public ShiftsViewHolder(@NonNull View itemView) {
            super(itemView);
            WorkerNameTextShiftItem = itemView.findViewById(R.id.WorkerNameTextShiftItem);
            StartTime = itemView.findViewById(R.id.StartTimeShiftHystory);
            EndTime = itemView.findViewById(R.id.EndTimeShiftHistory);
            button = itemView.findViewById(R.id.EditWorkerButtonWorkerItem);
            delete=itemView.findViewById(R.id.DeleteWorkerButtonWorkerItem);
        }
    }
}
