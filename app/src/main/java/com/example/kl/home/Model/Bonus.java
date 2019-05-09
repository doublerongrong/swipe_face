package com.example.kl.home.Model;

import java.util.Date;

public class Bonus {
    private String bonus_reason;
    private String class_id;
    private String student_id;
    private Date bonus_time;
    private String RDanswerBonus;
    private String answerBonus;

    public Bonus() {
    }

    public Bonus(String bonus_reason, String class_id, String student_id, Date bouns_time, String RDanswerBonus, String answerBonus) {
        this.bonus_reason = bonus_reason;
        this.class_id = class_id;
        this.student_id = student_id;
        this.bonus_time = bouns_time;
        this.answerBonus = answerBonus;
        this.RDanswerBonus = RDanswerBonus;
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

    public String getRDanswerBonus() {
        return RDanswerBonus;
    }

    public void setRDanswerBonus(String RDanswerBonus) {
        this.RDanswerBonus = RDanswerBonus;
    }

    public String getAnswerBonus() {
        return answerBonus;
    }

    public void setAnswerBonus(String answerBonus) {
        this.answerBonus = answerBonus;
    }
}
