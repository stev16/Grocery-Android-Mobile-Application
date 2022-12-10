package com.example.assignment.Models;

import java.io.Serializable;

public class itemModel implements Serializable {
    String name;
    String price;
    String imageURL;
    String desc;

    public itemModel(String name, String price, String imageURL, String desc) {
        this.name = name;
        this.price = price;
        this.imageURL = imageURL;
        this.desc = desc;
    }

    public itemModel(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
