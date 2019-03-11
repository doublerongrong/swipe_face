package com.example.kl.home.Model;

import android.support.annotation.NonNull;

public class LeaveId {
    public String LeaveId;

    public <T extends LeaveId> T withId(@NonNull final String id){
        this.LeaveId = id;
        return (T) this;
    }
}
