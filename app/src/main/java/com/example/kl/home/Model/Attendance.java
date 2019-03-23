package com.example.kl.home.Model;

import java.util.Date;

public class Attendance extends RollcallId {

    private String student_id;
    private Date attendance_time;
    private String attendance_status;


    public Attendance() {
    }

    public String getRollcallId() {
        return rollcallId;
    }

    public void setRollcallId(String rollcallId) {
        this.rollcallId = rollcallId;
    }

    public Attendance(String student_id, Date attendance_time, String attendance_status, String rollcallId) {
        this.student_id = student_id;
        this.attendance_time = attendance_time;
        this.attendance_status = attendance_status;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public Date getAttendance_time() {
        return attendance_time;
    }

    public void setAttendance_time(Date attendance_time) {
        this.attendance_time = attendance_time;
    }

    public String getAttendance_status() {
        return attendance_status;
    }

    public void setAttendance_status(String attendance_status) {
        this.attendance_status = attendance_status;
    }
}
