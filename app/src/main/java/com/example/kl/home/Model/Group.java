package com.example.kl.home.Model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Group extends GroupId {
    String group_bonus;
    String group_num;
    String group_leader;
    List<String> student_id;

    public String getGroup_num() {
        return group_num;
    }

    public void setGroup_num(String group_num) {
        this.group_num = group_num;
    }


    public String getGroup_bonus() {
        return group_bonus;
    }

    public void setGroup_bonus(String group_bonus) {
        this.group_bonus = group_bonus;
    }

    public String getGroup_leader() {
        return group_leader;
    }

    public void setGroup_leader(String group_leader) {
        this.group_leader = group_leader;
    }

    public List<String> getStudent_id() {
        return student_id;
    }

    public void setStudent_id(List<String> student_id) {
        this.student_id = student_id;
    }
}

