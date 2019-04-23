package com.example.kl.home.Model;

import android.support.annotation.NonNull;

public class GroupId {
    public String groupId;

    public <T extends GroupId> T withId(@NonNull final  String id){
        this.groupId= id;
        return (T) this;
    }
}
