package com.example.kl.home.Model;

public class RollCallList {
    public RollCallList(String rollcall_id, String rollcall_department, String rollcall_name){
        this.rollcall_id = rollcall_id;
        this.rollcall_department = rollcall_department;
        this.rollcall_name = rollcall_name;
    }

    String rollcall_id;
    String rollcall_department;
    String rollcall_name;


    public String getRollcall_id() {
        return rollcall_id;
    }

    public void setRollcall_id(String rollcall_id) {
        this.rollcall_id = rollcall_id;
    }

    public String getRollcall_department() {
        return rollcall_department;
    }

    public void setRollcall_department(String rollcallt_department) {
        this.rollcall_department = rollcallt_department;
    }

    public String getRollcall_name() {
        return rollcall_name;
    }

    public void setRollcall_name(String rollcall_name) {
        this.rollcall_name = rollcall_name;
    }

}
