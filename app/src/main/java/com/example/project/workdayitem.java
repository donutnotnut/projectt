package com.example.project;

import java.util.ArrayList;

public class workdayitem {
    String day;
    ArrayList<String> names;
    public workdayitem(String day, ArrayList<String> names) {
        this.day = day;
        this.names = names;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }
}
