package com.example.kl.home.Model;

import android.support.annotation.NonNull;

public class ClassId {
    public String classId;

    public <T extends ClassId> T withId(@NonNull final  String id){
        this.classId = id;
        return (T) this;
    }
}
