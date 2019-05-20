package com.trade.model;

public class UpanddownHelper {

    private String name;
    private double upanddown;
    private int status;

    public UpanddownHelper(String name, double upanddown){
        this.name = name;
        this.upanddown = upanddown;
        if(upanddown > 0){
            status = 1;
        }else if(upanddown < 0){
            status = 2;
        }else {
            status = 3;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUpanddown() {
        return upanddown;
    }

    public void setUpanddown(double upanddown) {
        this.upanddown = upanddown;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
