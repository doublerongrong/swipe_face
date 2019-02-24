package com.example.kl.home;

import java.util.ArrayList;

public class Class extends  ClassId{
    public Class() {

    }

    private String class_id;
    private String class_name;
    private Integer class_team_total;
    private Integer student_total;
    private String teacher_email;

    private String class_schoolyear;
    private ArrayList<String> student_id =new ArrayList<String>();
    private ArrayList<ClassRecallRecord> classRecallRecordList;
    private ArrayList<ClassTeam> classTeamList;

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public Integer getClass_team_total() {
        return class_team_total;
    }

    public void setClass_team_total(Integer class_team_total) {
        this.class_team_total = class_team_total;
    }

    public String getTeacher_email() {
        return teacher_email;
    }

    public void setTeacher_email(String teacher_email) {
        this.teacher_email = teacher_email;
    }

    public String getClass_schoolyear() {

        return class_schoolyear;
    }

    public void setClass_schoolyear(String class_schoolyear) {
        this.class_schoolyear = class_schoolyear;
    }

    public ArrayList<String> getStudent_id() {
        return student_id;
    }

    public void setStudent_id(ArrayList<String> student_id) {
        this.student_id = student_id;
    }

    public ArrayList<ClassRecallRecord> getClassRecallRecordList() {
        return classRecallRecordList;
    }

    public void setClassRecallRecordList(ArrayList<ClassRecallRecord> classRecallRecordList) {
        this.classRecallRecordList = classRecallRecordList;
    }

    public ArrayList<ClassTeam> getClassTeamList() {
        return classTeamList;
    }

    public void setClassTeamList(ArrayList<ClassTeam> classTeamList) {
        this.classTeamList = classTeamList;
    }

    public Integer getStudent_total() {
        if (!student_id.isEmpty()) {
            student_total = student_id.size();
        }
        else{
            student_total=0;
        }
        return student_total;
    }

    public void setStudent_total(Integer student_total) {
        this.student_total = student_total;
    }
}
