package com.example.kl.home.Model;

import android.support.annotation.NonNull;

public class RollcallId {
        public String rollcallId;

        public <T extends RollcallId> T withId(@NonNull final  String id){
            this.rollcallId = id;
            return (T) this;
        }
}
