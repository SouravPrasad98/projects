package com.example.mail;

public class ModelOrderRetailer {
    String orderId ,orderTime , OderStatus, OrderCost, OrderBy ,OrderTo;

    public ModelOrderRetailer() {

    }

    public ModelOrderRetailer(String orderId, String orderTime, String oderStatus, String orderCost, String orderBy, String orderTo) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        OderStatus = oderStatus;
        OrderCost = orderCost;
        OrderBy = orderBy;
        OrderTo = orderTo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOderStatus() {
        return OderStatus;
    }

    public void setOderStatus(String oderStatus) {
        OderStatus = oderStatus;
    }

    public String getOrderCost() {
        return OrderCost;
    }

    public void setOrderCost(String orderCost) {
        OrderCost = orderCost;
    }

    public String getOrderBy() {
        return OrderBy;
    }

    public void setOrderBy(String orderBy) {
        OrderBy = orderBy;
    }

    public String getOrderTo() {
        return OrderTo;
    }

    public void setOrderTo(String orderTo) {
        OrderTo = orderTo;
    }
}
