package com.bean;

import java.util.List;

public class InverterBean {

    private List<TotalPower> list;
    private String normal;
    private String abnormal;

    public List<TotalPower> getList() {
        return list;
    }

    public void setList(List<TotalPower> list) {
        this.list = list;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(String abnormal) {
        this.abnormal = abnormal;
    }

    public InverterBean(List<TotalPower> list, String normal, String abnormal) {
        this.list = list;
        this.normal = normal;
        this.abnormal = abnormal;
    }
}
