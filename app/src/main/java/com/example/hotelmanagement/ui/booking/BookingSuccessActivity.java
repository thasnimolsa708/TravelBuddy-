package com.example.hotelmanagement.ui.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.hotelmanagement.BaseActivity;
import com.example.hotelmanagement.R;
import com.example.hotelmanagement.data.prefrence.SessionManager;
import com.example.hotelmanagement.databinding.ActivityBookingDoneBinding;
import com.example.hotelmanagement.ui.hotel.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BookingSuccessActivity extends BaseActivity {
    private ActivityBookingDoneBinding binding;
    private String total;
    private String type;
    String checkIn, checkOut, image, location, name, roomType;
    int adult, children, room;
    private FirebaseFirestore fb;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingDoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        showLoadingLottie();
        getFromIntent();
        initView();
        addDataToFireBase();
    }

    private void addDataToFireBase() {
        Map<String, Object> bookingItem = new HashMap<>();
        bookingItem.put("amount", String.valueOf(total));
        bookingItem.put("booked_on", currentDate());
        bookingItem.put("check_in", checkIn);
        bookingItem.put("check_out", checkOut);
        bookingItem.put("adult", String.valueOf(adult));
        bookingItem.put("children", String.valueOf(children));
        bookingItem.put("rooms", String.valueOf(room));
        bookingItem.put("type", type);
        bookingItem.put("status", "in");
        bookingItem.put("userid", sessionManager.getUserId());
        bookingItem.put("shopName", name);
        bookingItem.put("shopLocation", location);
        bookingItem.put("shopImage", image);
        bookingItem.put("roomType", roomType);

        Log.d("orderData", bookingItem.toString());

        fb.collection("bookings")
                .add(bookingItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        showSuccessLottie();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast(BookingSuccessActivity.this, getString(R.string.error));
            }
        });
    }

    private void initView() {
        fb = getFireStoreInstance();
        sessionManager = new SessionManager(this);
        binding.btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingSuccessActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getFromIntent() {
        try {
            Bundle bundle = getIntent().getExtras();
            checkIn = bundle.getString("checkIn");
            checkOut = bundle.getString("checkOut");
            adult = bundle.getInt("adult");
            children = bundle.getInt("children");
            room = bundle.getInt("rooms");
            total = bundle.getString("total");
            type = bundle.getString("type");
            name = bundle.getString("name");
            image = bundle.getString("image");
            location = bundle.getString("location");
            roomType = bundle.getString("roomType");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLoadingLottie() {
        binding.lottie.setAnimation("loading.json");
        binding.lottie.loop(true);
        binding.lottie.playAnimation();
        binding.tvSuccess.setVisibility(View.GONE);
        binding.btnSummary.setVisibility(View.GONE);
    }

    private void showSuccessLottie() {
        binding.lottie.setAnimation("order_success.json");
        binding.lottie.loop(false);
        binding.lottie.playAnimation();
        binding.tvSuccess.setVisibility(View.VISIBLE);
        binding.btnSummary.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}