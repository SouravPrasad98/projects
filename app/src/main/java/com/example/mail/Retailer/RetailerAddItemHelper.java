package com.example.mail.Retailer;

public class RetailerAddItemHelper {
    int price,quantity,scale;
    String type;

    public RetailerAddItemHelper() {
    }

    public RetailerAddItemHelper(int price, int quantity, int scale, String type) {
        this.price = price;
        this.quantity = quantity;
        this.scale = scale;
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
