package com.example.kl.home.Model;

import java.util.ArrayList;

public class Performance {
    private String class_id;
    private String student_id;
    private int performance_totalAttendance;
    private int performance_totalBonus;
    private int performance_absenceTimes;
//    private Bonus bonus;
//    private ArrayList<Bonus> bonusList;


    public int getPerformance_absenceTimes() {
        return performance_absenceTimes;
    }

    public void setPerformance_absenceTimes(int performance_absenceTimes) {
        this.performance_absenceTimes = performance_absenceTimes;
    }

    public Performance() {
    }

    public Performance(String class_id, String student_id, int performance_totalAttendance, int performance_totalBonus, Bonus bonus, ArrayList<Bonus> bonusList) {
        this.class_id = class_id;
        this.student_id = student_id;
        this.performance_totalAttendance = performance_totalAttendance;
        this.performance_totalBonus = performance_totalBonus;
//        this.bonus = bonus;
//        this.bonusList = bonusList;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public int getPerformance_totalAttendance() {
        return performance_totalAttendance;
    }

    public void setPerformance_totalAttendance(int performance_totalAttendance) {
        this.performance_totalAttendance = performance_totalAttendance;
    }

    public int getPerformance_totalBonus() {
        return performance_totalBonus;
    }

    public void setPerformance_totalBonus(int performance_totalBonus) {
        this.performance_totalBonus = performance_totalBonus;
    }

//    public Bonus getBonus() {
//        return bonus;
//    }
//
//    public void setBonus(Bonus bonus) {
//        this.bonus = bonus;
//    }
//
//    public ArrayList<Bonus> getBonusList() {
//        return bonusList;
//    }
//
//    public void setBonusList(ArrayList<Bonus> bonusList) {
//        this.bonusList = bonusList;
//    }
}
