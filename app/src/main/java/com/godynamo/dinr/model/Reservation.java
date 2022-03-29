package com.godynamo.dinr.model;

import java.util.Date;

/**
 * Created by dankovassev on 14-11-27.
 */
public class Reservation {

    private int id;
    private Date startTime;
    private Date endTime;
    private String table_detail;
    private String state;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTable_detail() {
        return table_detail;
    }

    public void setTable_detail(String table_detail) {
        this.table_detail = table_detail;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", table_detail='" + table_detail + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
