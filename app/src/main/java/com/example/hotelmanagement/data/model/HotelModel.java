package com.example.hotelmanagement.data.model;

public class HotelModel {
    public String name;
    public String Image;
    public String location;
    public String rating;
    public String price;
    public String description;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HotelModel(String name, String image, String location, String rating, String price, String description) {
        this.name = name;
        Image = image;
        this.location = location;
        this.rating = rating;
        this.price = price;
        this.description = description;
    }

    public HotelModel(String name, String image, String location, String rating) {
        this.name = name;
        Image = image;
        this.location = location;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getlocation() {
        return location;
    }

    public void setlocation(String location) {
        this.location = location;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}