package com.example.kl.home.Model;

import java.util.Date;

public class Bonus {
    private String bonus_reason;
    private Date bonus_time;

    public String getBonus_reason() {
        return bonus_reason;
    }

    public void setBonus_reason(String bonus_reason) {
        this.bonus_reason = bonus_reason;
    }

    public Date getBonus_time() {
        return bonus_time;
    }

    public void setBonus_time(Date bonus_time) {
        this.bonus_time = bonus_time;
    }
}
