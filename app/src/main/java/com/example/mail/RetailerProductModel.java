package com.example.mail;

import com.example.mail.Retailer.WholesellerListItem;

import java.util.List;
import java.util.Map;

public class RetailerProductModel {

    private String productTitle,productdescription,productcategory,discountAvailable,discountnote,
            discountprice,productIcon, uid;

    private Map<String, WholesellerListItem> wholesellerList;
    private Map<String, WholesellerListItem> RetailerList;


    public RetailerProductModel() {
    }

    public RetailerProductModel(String productTitle, String productdescription, String productcategory, String discountAvailable, String discountnote, String discountprice, String productIcon, String uid, Map<String, WholesellerListItem> wholesellerList, Map<String, WholesellerListItem> retailerList) {
        this.productTitle = productTitle;
        this.productdescription = productdescription;
        this.productcategory = productcategory;
        this.discountAvailable = discountAvailable;
        this.discountnote = discountnote;
        this.discountprice = discountprice;
        this.productIcon = productIcon;
        this.uid = uid;
        this.wholesellerList = wholesellerList;
        RetailerList = retailerList;
    }

    public Map<String, WholesellerListItem> getRetailerList() {
        return RetailerList;
    }

    public void setRetailerList(Map<String, WholesellerListItem> retailerList) {
        RetailerList = retailerList;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription;
    }

    public String getProductcategory() {
        return productcategory;
    }

    public void setProductcategory(String productcategory) {
        this.productcategory = productcategory;
    }

    public String getDiscountAvailable() {
        return discountAvailable;
    }

    public void setDiscountAvailable(String discountAvailable) {
        this.discountAvailable = discountAvailable;
    }

    public String getDiscountnote() {
        return discountnote;
    }

    public void setDiscountnote(String discountnote) {
        this.discountnote = discountnote;
    }

    public String getDiscountprice() {
        return discountprice;
    }

    public void setDiscountprice(String discountprice) {
        this.discountprice = discountprice;
    }

    public String getProductIcon() {
        return productIcon;
    }

    public void setProductIcon(String productIcon) {
        this.productIcon = productIcon;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, WholesellerListItem> getWholesellerList() {
        return wholesellerList;
    }

    public void setWholesellerList(Map<String, WholesellerListItem> wholesellerList) {
        this.wholesellerList = wholesellerList;
    }
}
