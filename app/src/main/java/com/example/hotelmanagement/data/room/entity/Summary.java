package com.example.hotelmanagement.data.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "summary")
public class Summary {

    @PrimaryKey(autoGenerate = true)
    public int orderId;

    @ColumnInfo(name = "rooms")
    public String rooms;

    @ColumnInfo(name = "children")
    public String children;

    @ColumnInfo(name = "check_out")
    public String check_out;

    @ColumnInfo(name = "check_in")
    public String check_in;

    @ColumnInfo(name = "total")
    public String total;

    @ColumnInfo(name = "adult")
    public String adult;

    @ColumnInfo(name = "hotel_image")
    public String hotelImage;

    @ColumnInfo(name = "hotel_location")
    public String hotelLocation;

    @ColumnInfo(name = "hotel_name")
    public String hotelName;

}
