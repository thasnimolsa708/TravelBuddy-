package com.example.hotelmanagement.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagement.data.room.SummaryDatabase;
import com.example.hotelmanagement.data.room.entity.Summary;
import com.example.hotelmanagement.databinding.ActivitySummaryBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SummaryActivity extends AppCompatActivity {

    String checkIn, checkOut, total, image, location, name, roomType;
    int adult, children, room;
    ActivitySummaryBinding binding;
    private SummaryDatabase db;
    String newline = System.getProperty("line.separator");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getFromIntent();
        initView();
    }

    private void initView() {
        StringBuilder builder = new StringBuilder();
        db = SummaryDatabase.getAppDatabase(this);
        builder.append("Adults  " + "X" + adult + newline);
        builder.append("Children  " + "X" + children);
        binding.tvCheckIn.setText(checkIn);
        binding.tvCheckOut.setText(checkOut);
        binding.tvRooms.setText(roomType + " X " + room);
        binding.tvGuests.setText(builder);
        binding.tvTotal.setText(String.valueOf(total) + "£");
        binding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDialog();
            }
        });
        binding.ivBack.setOnClickListener(view -> finish());
    }

    private void getFromIntent() {
        Bundle bundle = getIntent().getExtras();
        checkIn = bundle.getString("checkIn");
        checkOut = bundle.getString("checkOut");
        adult = bundle.getInt("adult");
        children = bundle.getInt("children");
        room = bundle.getInt("rooms");
        total = bundle.getString("total");
        name = bundle.getString("name");
        image = bundle.getString("image");
        location = bundle.getString("location");
        if (bundle.getInt("roomType") == 1) {
            roomType = "Single";
        } else
            roomType = "Double";


    }

    public void showConfirmDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SummaryActivity.this);
        builder.setTitle("Booking Confirmation");
        builder.setMessage("Are you sure to confirm the booking?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Summary summary = new Summary();
                summary.check_in = checkIn;
                summary.check_out = checkOut;
                summary.adult = String.valueOf(adult);
                summary.children = String.valueOf(adult);
                summary.rooms = String.valueOf(room);
                summary.total = total;
                summary.hotelName = name;
                summary.hotelLocation = location;
                summary.hotelImage = image;
                insertSummaryToRoomDb(summary);
                dialogInterface.dismiss();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();

    }

    private void insertSummaryToRoomDb(Summary summary) {
        db.orderDao().upsert(summary);
        Intent intent = new Intent(SummaryActivity.this, PaymentActivity.class);
        intent.putExtra("checkIn", checkIn);
        intent.putExtra("checkOut", checkOut);
        intent.putExtra("adult", adult);
        intent.putExtra("children", children);
        intent.putExtra("rooms", room);
        intent.putExtra("total", total);
        intent.putExtra("name", name);
        intent.putExtra("location", location);
        intent.putExtra("image", image);
        intent.putExtra("roomType", roomType);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SummaryDatabase.destroyInstance();
    }
}