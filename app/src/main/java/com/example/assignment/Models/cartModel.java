package com.example.assignment.Models;

public class cartModel {
    String name;
    String quantity;
    String imageURL;
    Double TPcart;
    String price;

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    String cartID;

    public cartModel(){

    }

    public cartModel(String name, String quantity, String imageURL, Double TPcart, String price) {
        this.name = name;
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.TPcart = TPcart;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Double getTPcart() {
        return TPcart;
    }

    public void setTPcart(Double TPcart) {
        this.TPcart = TPcart;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
