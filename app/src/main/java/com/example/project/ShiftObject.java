package com.example.project;

public class ShiftObject {
    String StartDate;
    String EndDate;
    int id;
    int ShiftId;

    public ShiftObject(String startDate, String endDate, int id, int shiftId) {
        StartDate = startDate;
        EndDate = endDate;
        this.id = id;
        ShiftId = shiftId;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShiftId() {
        return ShiftId;
    }

    public void setShiftId(int shiftId) {
        ShiftId = shiftId;
    }
}
