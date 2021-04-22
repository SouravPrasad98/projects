package com.example.mail.Retailer;

import java.io.Serializable;

public class WholesellerListItem implements Serializable {
    String address, bussinessname, email,deliveryfee, latitude, longitude,online,shopopen, phonenumber, price,
            quantity, uid,discountprice,icon,discountnote,perunitCost;
        Boolean    discountAvailable1;


    public WholesellerListItem() {
    }

    public WholesellerListItem(String address, String bussinessname, String email,String deliveryfee, String discountprice, Boolean discountAvailable1, String icon,
                               String discountnote, String latitude, String longitude, String phonenumber,String perunitCost, String price, String quantity, String uid, String online, String shopopen) {
        this.address = address;
        this.bussinessname = bussinessname;
        this.email = email;
        this.latitude = latitude;
        this.perunitCost =perunitCost;
        this.longitude = longitude;
        this.phonenumber = phonenumber;
        this.price = price;
        this.deliveryfee = deliveryfee;
        this.quantity = quantity;
        this.uid = uid;
        this.discountprice= discountprice;
        this.discountAvailable1 = discountAvailable1;
        this.icon = icon;
        this.discountnote = discountnote;
        this.online =online;
        this.shopopen = shopopen;
    }

    public String getDeliveryfee() {
        return deliveryfee;
    }

    public void setDeliveryfee(String deliveryfee) {
        this.deliveryfee = deliveryfee;
    }

    public String getPerunitCost() {
        return perunitCost;
    }

    public void setPerunitCost(String perunitCost) {
        this.perunitCost = perunitCost;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getShopopen() {
        return shopopen;
    }

    public void setShopopen(String shopopen) {
        this.shopopen = shopopen;
    }

    public String getDiscountprice() {
        return discountprice;
    }

    public void setDiscountprice(String discountprice) {
        this.discountprice = discountprice;
    }

    public Boolean getDiscountAvailable1() {
        return discountAvailable1;
    }

    public void setDiscountAvailable1(Boolean discountAvailable1) {
        this.discountAvailable1 = discountAvailable1;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDiscountnote() {
        return discountnote;
    }

    public void setDiscountnote(String discountnote) {
        this.discountnote = discountnote;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBussinessname() {
        return bussinessname;
    }

    public void setBussinessname(String bussinessname) {
        this.bussinessname = bussinessname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
