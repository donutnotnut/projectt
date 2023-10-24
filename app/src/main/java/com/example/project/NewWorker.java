package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewWorker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_worker);
        ImageView imageView = findViewById(R.id.imageView2);
        TextView textView12 = findViewById(R.id.textView12);
        TextView textView13 = findViewById(R.id.textView13);
        TextView textView14 = findViewById(R.id.textView14);
        TextView textView15 = findViewById(R.id.textView15);
        TextView textView16 = findViewById(R.id.textView16);
        TextView Name = findViewById(R.id.EditTextName);
        TextView Surname = findViewById(R.id.EditTextSurname);
        TextView Email = findViewById(R.id.editTextTextEmailAddress);
        TextView Password = findViewById(R.id.editTextTextPassword);
        TextView Salary = findViewById(R.id.EditTextSalary);
        CheckBox Admin = findViewById(R.id.AdminCheckBox);
        Button Save = findViewById(R.id.SaveButtonNewWorker);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.frombottomtotop);
        imageView.startAnimation(animation);
        textView12.startAnimation(animation);
        textView13.startAnimation(animation);
        textView14.startAnimation(animation);
        textView15.startAnimation(animation);
        textView16.startAnimation(animation);
        Name.startAnimation(animation);
        Surname.startAnimation(animation);
        Email.startAnimation(animation);
        Password.startAnimation(animation);
        Admin.startAnimation(animation);
        Salary.startAnimation(animation);
        Save.startAnimation(animation);
        int id = getIntent().getIntExtra("id", 0);
        if (id==0) {
            Save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Connection con = new ConnectionHelper().connectionclass();
                    try {
                        PreparedStatement ps = con.prepareStatement("INSERT INTO info(Name, Surname, Email, Password, Salary, Elevated) VALUES(?,?,?,?,?,?)");
                        ps.setString(1, Name.getText().toString());
                        ps.setString(2, Surname.getText().toString());
                        ps.setString(3, Email.getText().toString());
                        ps.setString(4, Password.getText().toString());
                        ps.setString(5, Salary.getText().toString());
                        ps.setBoolean(6, Admin.isChecked());
                        ps.executeUpdate();
                        con.close();
                        Toast.makeText(NewWorker.this, "Insert Success", Toast.LENGTH_SHORT).show();
                    } catch (SQLException e) {
                        Log.e("error", e.getMessage());
                    }
                }
            });
        }
        else {
            Connection connection=new ConnectionHelper().connectionclass();
            try {
                ResultSet rs=connection.createStatement().executeQuery("SELECT * FROM info WHERE ID="+id);
                while (rs.next()) {
                    Name.setText(rs.getString("Name"));
                    Surname.setText(rs.getString("Surname"));
                    Email.setText(rs.getString("Email"));
                    Password.setText(rs.getString("Password"));
                    Salary.setText(rs.getString("Salary"));
                    Admin.setChecked(rs.getBoolean("Elevated"));
                }
                connection.close();
            } catch (SQLException e) {
                Log.e("error", e.getMessage());
            }
            Save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Connection con = new ConnectionHelper().connectionclass();
                    try {
                        PreparedStatement ps = con.prepareStatement("UPDATE info SET Name=?, Surname=?, Email=?, Password=?, Salary=?, Admin=? WHERE ID=?");
                        ps.setString(1, Name.getText().toString());
                        ps.setString(2, Surname.getText().toString());
                        ps.setString(3, Email.getText().toString());
                        ps.setString(4, Password.getText().toString());
                        ps.setString(5, Salary.getText().toString());
                        ps.setBoolean(6, Admin.isChecked());
                        ps.setInt(7, id);
                        ps.executeUpdate();
                        con.close();
                        Toast.makeText(NewWorker.this, "Update Success", Toast.LENGTH_SHORT).show();
                    } catch (SQLException e) {
                        Log.e("error", e.getMessage());
                    }
                }
            });

        }

    }
}