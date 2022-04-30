package com.example.hotelmanagement.data.model;

public class HistoryModel {
    public String rooms;
    public String children;
    public String check_out;
    public String check_in;
    public String booked_on;
    public String amount;
    public String adult;
    public String shopImage;
    public String shopLocation;
    public String shopName;
    public String status;
    public String type;
    public String userid;
    private String roomType;

    public HistoryModel(String rooms, String children, String check_out, String check_in, String booked_on, String amount, String adult, String shopImage, String shopLocation, String shopName, String status, String type, String userid, String roomType) {
        this.rooms = rooms;
        this.children = children;
        this.check_out = check_out;
        this.check_in = check_in;
        this.booked_on = booked_on;
        this.amount = amount;
        this.adult = adult;
        this.shopImage = shopImage;
        this.shopLocation = shopLocation;
        this.shopName = shopName;
        this.status = status;
        this.type = type;
        this.userid = userid;
        this.roomType = roomType;
    }

    public HistoryModel(String rooms, String children, String check_out, String check_in, String booked_on,
                        String amount, String adult, String shopImage, String shopLocation, String shopName, String status, String type, String userid) {
        this.rooms = rooms;
        this.children = children;
        this.check_out = check_out;
        this.check_in = check_in;
        this.booked_on = booked_on;
        this.amount = amount;
        this.adult = adult;
        this.shopImage = shopImage;
        this.shopLocation = shopLocation;
        this.shopName = shopName;
        this.status = status;
        this.type = type;
        this.userid = userid;

    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public String getBooked_on() {
        return booked_on;
    }

    public void setBooked_on(String booked_on) {
        this.booked_on = booked_on;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


}
