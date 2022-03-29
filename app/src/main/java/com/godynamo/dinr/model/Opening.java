package com.godynamo.dinr.model;

import java.util.Date;

/**
 * Created by dankovassev on 14-11-27.
 */
public class Opening {

    private int id;
    private Date start_time;
    private Date end_time;
    private String table_detail;
    private int min_seats;
    private int max_seats;
    private String table_type;
    private String state;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public String getTable_detail() {
        return table_detail;
    }

    public void setTable_detail(String table_detail) {
        this.table_detail = table_detail;
    }

    public int getMin_seats() {
        return min_seats;
    }

    public void setMin_seats(int min_seats) {
        this.min_seats = min_seats;
    }

    public int getMax_seats() {
        return max_seats;
    }

    public void setMax_seats(int max_seats) {
        this.max_seats = max_seats;
    }

    public String getTable_type() {
        return table_type;
    }

    public void setTable_type(String table_type) {
        this.table_type = table_type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Opening{" +
                "id=" + id +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", table_detail='" + table_detail + '\'' +
                ", min_seats=" + min_seats +
                ", max_seats=" + max_seats +
                ", table_type='" + table_type + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
