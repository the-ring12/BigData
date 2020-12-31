package com.bean;

public class Condition {
    private String time;
    private double temperature;
    private int power;
    private double irradiation;

    public Condition() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public Condition(String time, double temperature, int power, double irradiation) {
        this.time = time;
        this.temperature = temperature;
        this.power = power;
        this.irradiation = irradiation;
    }

    public double getIrradiation() {
        return irradiation;
    }

    public void setIrradiation(double irradiation) {
        this.irradiation = irradiation;
    }
}
