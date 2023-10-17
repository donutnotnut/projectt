package com.example.project;

import java.sql.Timestamp;

public class ShiftObject {
    String StartDate;
    String EndDate;
    int id;

    public Timestamp getStartTime() {
        return StartTime;
    }

    public void setStartTime(Timestamp startTime) {
        StartTime = startTime;
    }

    public Timestamp getEndTime() {
        return EndTime;
    }

    public void setEndTime(Timestamp endTime) {
        EndTime = endTime;
    }

    int ShiftId;
    Timestamp StartTime;
    Timestamp EndTime;

    public ShiftObject(String startDate, String endDate, int id, int shiftId, Timestamp startTime, Timestamp endTime) {
        StartDate = startDate;
        EndDate = endDate;
        this.id = id;
        ShiftId = shiftId;
        StartTime = startTime;
        EndTime = endTime;
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
