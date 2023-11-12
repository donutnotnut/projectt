package com.example.project;

public class NextWeekScheduleItem {
    String Name;
    Boolean Sunday;
    Boolean Monday;
    Boolean Tuesday;
    Boolean Wednesday;
    Boolean Thursday;
    Boolean Friday;
    Boolean Saturday;
    int id;
    public NextWeekScheduleItem(int id , String Name, Boolean Sunday, Boolean Monday, Boolean Tuesday, Boolean Wednesday, Boolean Thursday, Boolean Friday, Boolean Saturday){
        this.Name = Name;
        this.Sunday = Sunday;
        this.Monday = Monday;
        this.Tuesday = Tuesday;
        this.Wednesday = Wednesday;
        this.Thursday = Thursday;
        this.Friday = Friday;
        this.Saturday = Saturday;
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Boolean getSunday() {
        return Sunday;
    }

    public void setSunday(Boolean sunday) {
        Sunday = sunday;
    }

    public Boolean getMonday() {
        return Monday;
    }

    public void setMonday(Boolean monday) {
        Monday = monday;
    }

    public Boolean getTuesday() {
        return Tuesday;
    }

    public void setTuesday(Boolean tuesday) {
        Tuesday = tuesday;
    }

    public Boolean getWednesday() {
        return Wednesday;
    }

    public void setWednesday(Boolean wednesday) {
        Wednesday = wednesday;
    }

    public Boolean getThursday() {
        return Thursday;
    }

    public void setThursday(Boolean thursday) {
        Thursday = thursday;
    }

    public Boolean getFriday() {
        return Friday;
    }

    public void setFriday(Boolean friday) {
        Friday = friday;
    }

    public Boolean getSaturday() {
        return Saturday;
    }

    public void setSaturday(Boolean saturday) {
        Saturday = saturday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
