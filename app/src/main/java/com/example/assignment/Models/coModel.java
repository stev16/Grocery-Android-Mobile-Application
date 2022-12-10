package com.example.assignment.Models;

public class coModel {
    String name, totalQuantity;
    Double totalPrice;

    public coModel(){

    }

    public coModel(String name, String totalQuantity, Double totalPrice) {
        this.name = name;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
