package com.bean;

public class Product {
    private String name;
    private int num;

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", num=" + num +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Product(String name, int num) {
        this.name = name;
        this.num = num;
    }
}
