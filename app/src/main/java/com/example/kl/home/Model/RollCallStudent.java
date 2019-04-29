package com.example.kl.home.Model;

public class RollCallStudent {

    public RollCallStudent(String student_name, String student_id, String student_department, String image_url){
        this.student_name  = student_name;
        this.student_id = student_id;
        this.student_department = student_department;
        this.image_url = image_url;
    }

    String student_name;
    String student_id;
    String student_department;
    String image_url;

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_department() {
        return student_department;
    }

    public void setStudent_department(String student_department) {
        this.student_department = student_department;
    }


}
