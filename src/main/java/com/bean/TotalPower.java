package com.bean;

public class TotalPower {
    private String time;
    private int power;

    public TotalPower(String time, int power) {
        this.time = time;
        this.power = power;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
