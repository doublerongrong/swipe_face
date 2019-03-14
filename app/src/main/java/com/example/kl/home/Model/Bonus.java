package com.example.kl.home.Model;

import java.util.Date;

public class Bonus {
    private String bonus_reason;
    private String class_id;
    private String student_id;
    private Date bonus_time;

    public Bonus() {
    }

    public Bonus(String bonus_reason, String class_id, String student_id, Date bouns_time) {
        this.bonus_reason = bonus_reason;
        this.class_id = class_id;
        this.student_id = student_id;
        this.bonus_time = bouns_time;
    }

    public String getBonus_reason() {
        return bonus_reason;
    }

    public void setBonus_reason(String bonus_reason) {
        this.bonus_reason = bonus_reason;
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

    public Date getBonus_time() {
        return bonus_time;
    }

    public void setBonus_time(Date bonus_time) {
        this.bonus_time = bonus_time;
    }
}
