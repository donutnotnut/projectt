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




    public ShiftObject(String startDate, String endDate, int id, int shiftId, Timestamp startTime, Timestamp endTime, String workername) {
        StartDate = startDate;
        EndDate = endDate;
        this.Workerid = id;
        ShiftId = shiftId;
        StartTime = startTime;
        EndTime = endTime;
        Workername = workername;
    }

    public String getWorkername() {
        return Workername;
    }

    public void setWorkername(String workername) {
        Workername = workername;
    }

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

    public int getWorkerid() {
        return Workerid;
    }

    public void setWorkerid(int workerid) {
        this.Workerid = workerid;
    }

    public int getShiftId() {
        return ShiftId;
    }

    public void setShiftId(int shiftId) {
        ShiftId = shiftId;
    }
}
