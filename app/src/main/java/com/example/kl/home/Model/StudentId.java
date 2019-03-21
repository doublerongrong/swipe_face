package com.example.kl.home.Model;

import android.support.annotation.NonNull;

public class StudentId  {
    public String studentId;

    public <T extends StudentId> T withId(@NonNull final  String id){
        this.studentId= id;
        return (T) this;
    }
}
