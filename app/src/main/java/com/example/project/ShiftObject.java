package com.example.project;

import java.sql.Timestamp;

public class ShiftObject {

    String StartDate;
    String EndDate;
    int Workerid;
    int ShiftId;
    Timestamp StartTime;
    Timestamp EndTime;
    String Workername;

    // Constructor for ShiftObject
    public ShiftObject(String startDate, String endDate, int id, int shiftId, Timestamp startTime, Timestamp endTime, String workername) {
        StartDate = startDate;
        EndDate = endDate;
        Workerid = id;
        ShiftId = shiftId;
        StartTime = startTime;
        EndTime = endTime;
        Workername = workername;
    }

    // Getter and setter methods for Workername
    public String getWorkername() {
        return Workername;
    }

    public void setWorkername(String workername) {
        Workername = workername;
    }

    // Getter and setter methods for StartTime
    public Timestamp getStartTime() {
        return StartTime;
    }

    public void setStartTime(Timestamp startTime) {
        StartTime = startTime;
    }

    // Getter and setter methods for EndTime
    public Timestamp getEndTime() {
        return EndTime;
    }

    public void setEndTime(Timestamp endTime) {
        EndTime = endTime;
    }

    // Getter and setter methods for StartDate
    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    // Getter and setter methods for EndDate
    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    // Getter and setter methods for Workerid
    public int getWorkerid() {
        return Workerid;
    }

    public void setWorkerid(int workerid) {
        Workerid = workerid;
    }

    // Getter and setter methods for ShiftId
    public int getShiftId() {
        return ShiftId;
    }

    public void setShiftId(int shiftId) {
        ShiftId = shiftId;
    }
}
