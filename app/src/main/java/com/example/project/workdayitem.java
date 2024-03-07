package com.example.project;

import java.util.ArrayList;

public class workdayitem {
    // Properties
    String day; // The day of the week
    ArrayList<String> names; // List of names associated with this day

    // Constructor
    public workdayitem(String day, ArrayList<String> names) {
        this.day = day;
        this.names = names;
    }

    // Getter for day
    public String getDay() {
        return day;
    }

    // Setter for day
    public void setDay(String day) {
        this.day = day;
    }

    // Getter for names
    public ArrayList<String> getNames() {
        return names;
    }

    // Setter for names
    public void setNames(ArrayList<String> names) {
        this.names = names;
    }
}
