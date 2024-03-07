package com.example.project;

/**
 * Represents a schedule item for the next week for a worker.
 */
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

    /**
     * Constructs a new NextWeekScheduleItem.
     *
     * @param id        The unique identifier of the worker.
     * @param Name      The name of the worker.
     * @param Sunday    Availability for Sunday.
     * @param Monday    Availability for Monday.
     * @param Tuesday   Availability for Tuesday.
     * @param Wednesday Availability for Wednesday.
     * @param Thursday  Availability for Thursday.
     * @param Friday    Availability for Friday.
     * @param Saturday  Availability for Saturday.
     */
    public NextWeekScheduleItem(int id, String Name, Boolean Sunday, Boolean Monday, Boolean Tuesday, Boolean Wednesday, Boolean Thursday, Boolean Friday, Boolean Saturday) {
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

    // Getters and setters
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
