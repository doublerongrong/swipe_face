package com.example.kl.home.Model;

import android.support.annotation.NonNull;

public class StudentId {
    public String StudentId;

    public <Y extends com.example.kl.home.Model.StudentId> Y withId(@NonNull final String id){
        this.StudentId = id;
        return (Y) this;
    }
}
