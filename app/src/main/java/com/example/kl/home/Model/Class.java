package com.example.kl.home.Model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class Class extends ClassId implements Parcelable {
    public Class() {

    }

    private String class_id;
    private String class_name;
    private Integer class_team_total;
    private Integer student_total;
    private String teacher_email;
    private String class_year;
    private ArrayList<String> student_id =new ArrayList<String>();
    private Integer class_totalpoints; //出席占學期總分
    private Integer class_totalteam;
    private Integer class_answerbonus; //點人回答(加分)
    private Integer class_rdanswerbonus; //隨機問題加分(加分)
    private Integer class_absenteeminus; //缺席扣分 (出席)
    private Integer class_lateminus; //遲到扣分(出席)
    private Integer class_ewtimes; //預警 次數達到
    private Integer class_ewpoints; //預警 分數低於
    private ArrayList<String> group_leader = new ArrayList<>();// 小組 組長列表
    private boolean group_state_go;//小組 分組狀態
    private boolean group_state;//小組 分組狀態
    private Integer group_num;//小組數量
    private Integer group_numHigh;//小組人數上限
    private Integer group_numLow;//小組人數下限
    private Date create_time;//小組創立時間
    private boolean question_state;
    private String rollcall_docId;

    public String getRollcall_docId() {
        return rollcall_docId;
    }

    public void setRollcall_docId(String rollcall_docId) {
        this.rollcall_docId = rollcall_docId;
    }

    public boolean isQuestion_state() {
        return question_state;
    }

    public void setQuestion_state(boolean question_state) {
        this.question_state = question_state;
    }

    public static Creator<Class> getCREATOR() {
        return CREATOR;
    }

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

    public String getClass_year() {

        return class_year;
    }

    public void setClass_year(String class_year) {
        this.class_year = class_year;
    }

    public ArrayList<String> getStudent_id() {
        return student_id;
    }

    public void setStudent_id(ArrayList<String> student_id) {
        this.student_id = student_id;
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

    public Integer getClass_totalpoints() {
        return class_totalpoints;
    }

    public void setClass_totalpoints(Integer class_totalpoints) {
        this.class_totalpoints = class_totalpoints;
    }

    public Integer getClass_totalteam() {
        return class_totalteam;
    }

    public void setClass_totalteam(Integer class_totalteam) {
        this.class_totalteam = class_totalteam;
    }

    public Integer getClass_answerbonus() {
        return class_answerbonus;
    }

    public void setClass_answerbonus(Integer class_answerbonus) {
        this.class_answerbonus = class_answerbonus;
    }

    public Integer getClass_rdanswerbonus() {
        return class_rdanswerbonus;
    }

    public void setClass_rdanswerbonus(Integer class_rdanswerbonus) {
        this.class_rdanswerbonus = class_rdanswerbonus;
    }

    public Integer getClass_absenteeminus() {
        return class_absenteeminus;
    }

    public void setClass_absenteeminus(Integer class_absenteeminus) {
        this.class_absenteeminus = class_absenteeminus;
    }

    public Integer getClass_lateminus() {
        return class_lateminus;
    }

    public void setClass_lateminus(Integer class_lateminus) {
        this.class_lateminus = class_lateminus;
    }

    public Integer getClass_ewtimes() {
        return class_ewtimes;
    }

    public void setClass_ewtimes(Integer class_ewtimes) {
        this.class_ewtimes = class_ewtimes;
    }

    public Integer getClass_ewpoints() {
        return class_ewpoints;
    }

    public void setClass_ewpoints(Integer class_ewpoints) {
        this.class_ewpoints = class_ewpoints;
    }

    public boolean isGroup_state() {
        return group_state;
    }

    public void setGroup_state(boolean group_state) {
        this.group_state = group_state;
    }

    public Integer getGroup_numLow() {
        return group_numLow;
    }

    public void setGroup_numLow(Integer group_numLow) {
        this.group_numLow = group_numLow;
    }

    public Integer getGroup_numHigh() {
        return group_numHigh;
    }

    public void setGroup_numHigh(Integer group_numHigh) {
        this.group_numHigh = group_numHigh;
    }

    public ArrayList<String> getGroup_leader() {
        return group_leader;
    }

    public void setGroup_leader(ArrayList<String> group_leader) {
        this.group_leader = group_leader;
    }

    public boolean isGroup_state_go() {
        return group_state_go;
    }

    public void setGroup_state_go(boolean group_state_go) {
        this.group_state_go = group_state_go;
    }

    public Integer getGroup_num() {
        return group_num;
    }

    public void setGroup_num(Integer group_num) {
        this.group_num = group_num;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.class_id);
        dest.writeString(this.class_name);
        dest.writeValue(this.class_team_total);
        dest.writeValue(this.student_total);
        dest.writeString(this.teacher_email);
        dest.writeString(this.class_year);
        dest.writeStringList(this.student_id);
        dest.writeValue(this.class_totalpoints);
        dest.writeValue(this.class_totalteam);
        dest.writeValue(this.class_answerbonus);
        dest.writeValue(this.class_rdanswerbonus);
        dest.writeValue(this.class_absenteeminus);
        dest.writeValue(this.class_lateminus);
        dest.writeValue(this.class_ewtimes);
        dest.writeValue(this.class_ewpoints);
        dest.writeStringList(this.group_leader);
        dest.writeByte(this.group_state_go ? (byte) 1 : (byte) 0);
        dest.writeByte(this.group_state ? (byte) 1 : (byte) 0);
        dest.writeValue(this.group_num);
        dest.writeValue(this.group_numHigh);
        dest.writeValue(this.group_numLow);
        dest.writeLong(this.create_time != null ? this.create_time.getTime() : -1);
    }

    protected Class(Parcel in) {
        this.class_id = in.readString();
        this.class_name = in.readString();
        this.class_team_total = (Integer) in.readValue(Integer.class.getClassLoader());
        this.student_total = (Integer) in.readValue(Integer.class.getClassLoader());
        this.teacher_email = in.readString();
        this.class_year = in.readString();
        this.student_id = in.createStringArrayList();
        this.class_totalpoints = (Integer) in.readValue(Integer.class.getClassLoader());
        this.class_totalteam = (Integer) in.readValue(Integer.class.getClassLoader());
        this.class_answerbonus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.class_rdanswerbonus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.class_absenteeminus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.class_lateminus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.class_ewtimes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.class_ewpoints = (Integer) in.readValue(Integer.class.getClassLoader());
        this.group_leader = in.createStringArrayList();
        this.group_state_go = in.readByte() != 0;
        this.group_state = in.readByte() != 0;
        this.group_num = (Integer) in.readValue(Integer.class.getClassLoader());
        this.group_numHigh = (Integer) in.readValue(Integer.class.getClassLoader());
        this.group_numLow = (Integer) in.readValue(Integer.class.getClassLoader());
        long tmpCreate_time = in.readLong();
        this.create_time = tmpCreate_time == -1 ? null : new Date(tmpCreate_time);
    }

    public static final Parcelable.Creator<Class> CREATOR = new Parcelable.Creator<Class>() {
        @Override
        public Class createFromParcel(Parcel source) {
            return new Class(source);
        }

        @Override
        public Class[] newArray(int size) {
            return new Class[size];
        }
    };
}

