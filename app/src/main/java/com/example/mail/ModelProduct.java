package com.example.mail;

public class ModelProduct {

    private String productId,productTitle,productdescription,productcategory,discountAvailable,discountnote,productprice,discountprice,productquantity,
            productIcon,timestamp,uid;

    public ModelProduct() {
    }

    public ModelProduct(String productId, String productTitle, String productdescription,
                        String productcategory, String discountAvailable, String discountnote, String productprice,
                        String discountprice, String productquantity, String productIcon, String timestamp, String uid) {
        this.productId = productId;
        this.productTitle = productTitle;
        this.productdescription = productdescription;
        this.productcategory = productcategory;
        this.discountAvailable = discountAvailable;
        this.discountnote = discountnote;
        this.productprice = productprice;
        this.discountprice = discountprice;
        this.productquantity = productquantity;
        this.productIcon = productIcon;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public String getDiscountprice() {
        return discountprice;
    }

    public void setDiscountprice(String discountprice) {
        this.discountprice = discountprice;
    }

    public String getProductquantity() {
        return productquantity;
    }

    public void setProductquantity(String productquantity) {
        this.productquantity = productquantity;
    }

    public String getProductIcon() {
        return productIcon;
    }

    public void setProductIcon(String productIcon) {
        this.productIcon = productIcon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
