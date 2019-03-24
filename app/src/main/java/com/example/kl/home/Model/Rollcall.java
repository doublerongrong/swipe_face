package com.example.kl.home.Model;

import java.util.ArrayList;
import java.util.Date;

public class Rollcall extends RollcallId{

    String class_id;
    Date rollcall_time;
    ArrayList<String> rollcall_absence;//缺席
    ArrayList<String> rollcall_late;//遲到
    ArrayList<String> rollcall_attend;//出席
    ArrayList<String> rollcall_casual;//事假
    ArrayList<String> rollcall_funeral;//喪假
    ArrayList<String> rollcall_offical;//公假
    ArrayList<String> rollcall_sick;//病假

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public Date getRollcall_time() {
        return rollcall_time;
    }

    public void setRollcall_time(Date rollcall_time) {
        this.rollcall_time = rollcall_time;
    }

    public ArrayList<String> getRollcall_absence() {
        return rollcall_absence;
    }

    public void setRollcall_absence(ArrayList<String> rollcall_absence) {
        this.rollcall_absence = rollcall_absence;
    }

    public ArrayList<String> getRollcall_attend() {
        return rollcall_attend;
    }

    public void setRollcall_attend(ArrayList<String> rollcall_attend) {
        this.rollcall_attend = rollcall_attend;
    }

    public ArrayList<String> getRollcall_casual() {
        return rollcall_casual;
    }

    public void setRollcall_casual(ArrayList<String> rollcall_casual) {
        this.rollcall_casual = rollcall_casual;
    }

    public ArrayList<String> getRollcall_funeral() {
        return rollcall_funeral;
    }

    public void setRollcall_funeral(ArrayList<String> rollcall_funeral) {
        this.rollcall_funeral = rollcall_funeral;
    }

    public ArrayList<String> getRollcall_offical() {
        return rollcall_offical;
    }

    public void setRollcall_offical(ArrayList<String> rollcall_offical) {
        this.rollcall_offical = rollcall_offical;
    }

    public ArrayList<String> getRollcall_sick() {
        return rollcall_sick;
    }

    public void setRollcall_sick(ArrayList<String> rollcall_sick) {
        this.rollcall_sick = rollcall_sick;
    }

    public ArrayList<String> getRollcall_late() {
        return rollcall_late;
    }

    public void setRollcall_late(ArrayList<String> rollcall_late) {
        this.rollcall_late = rollcall_late;
    }
}
