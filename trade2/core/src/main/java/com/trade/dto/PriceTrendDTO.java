package com.trade.dto;

public class PriceTrendDTO {

    private Object[] xAxis;
    private Object[] yAxis;

    public PriceTrendDTO() {
    }

    public PriceTrendDTO(Object[] xAxis, Object[] yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public Object[] getxAxis() {
        return xAxis;
    }

    public void setxAxis(Object[] xAxis) {
        this.xAxis = xAxis;
    }

    public Object[] getyAxis() {
        return yAxis;
    }

    public void setyAxis(Object[] yAxis) {
        this.yAxis = yAxis;
    }
}
