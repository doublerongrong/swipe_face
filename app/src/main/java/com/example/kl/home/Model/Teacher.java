package com.example.kl.home.Model;

import java.util.ArrayList;

public class Teacher {
    public Teacher() {

    }
    private String teacher_email;
    private String teacher_name;
    private String teacher_office;
    private ArrayList<String> class_id = new ArrayList<String>();
    private ArrayList<String> teacher_officetime = new ArrayList<String>();

    public String getTeacher_email() {
        return teacher_email;
    }

    public void setTeacher_email(String teacher_email) {
        this.teacher_email = teacher_email;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTeacher_office() {
        return teacher_office;
    }

    public void setTeacher_office(String teacher_office) {
        this.teacher_office = teacher_office;
    }

    public ArrayList<String> getClass_id() {
        return class_id;
    }

    public void setClass_id(ArrayList<String> class_id) {
        this.class_id = class_id;
    }

    public ArrayList<String> getTeacher_officetime() {
        return teacher_officetime;
    }

    public void setTeacher_officetime(ArrayList<String> teacher_officetime) {
        this.teacher_officetime = teacher_officetime;
    }
}
