package com.example.kl.home.Model;

import java.util.ArrayList;
import java.util.Date;

public class Question {
    String Answer;
    ArrayList<String> A =new ArrayList<>();
    ArrayList<String> B =new ArrayList<>();
    ArrayList<String> C =new ArrayList<>();
    ArrayList<String> D =new ArrayList<>();
    Date create_time;

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public ArrayList<String> getA() {
        return A;
    }

    public void setA(ArrayList<String> a) {
        A = a;
    }

    public ArrayList<String> getB() {
        return B;
    }

    public void setB(ArrayList<String> b) {
        B = b;
    }

    public ArrayList<String> getC() {
        return C;
    }

    public void setC(ArrayList<String> c) {
        C = c;
    }

    public ArrayList<String> getD() {
        return D;
    }

    public void setD(ArrayList<String> d) {
        D = d;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
