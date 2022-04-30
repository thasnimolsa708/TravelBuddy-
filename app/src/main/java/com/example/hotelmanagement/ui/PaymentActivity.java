package com.example.hotelmanagement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagement.databinding.ActivityPaymentBinding;
import com.example.hotelmanagement.ui.booking.BookingSuccessActivity;
import com.example.hotelmanagement.utils.NetworkManager;

public class PaymentActivity extends AppCompatActivity {
    private ActivityPaymentBinding binding;
    private String type;
    String checkIn, checkOut, total, image, location, name, roomType;
    int adult, children, room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getFromIntent();
        initView();
    }

    private void initView() {

        binding.radioDebit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.cardDebitDetails.setVisibility(View.VISIBLE);
                    binding.buttonConfirmOrder.setVisibility(View.GONE);
                    binding.radioCod.setChecked(false);
                } else {
                    binding.cardDebitDetails.setVisibility(View.GONE);
                }
            }
        });

        binding.radioCod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.radioDebit.setChecked(false);
                    binding.buttonConfirmOrder.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.buttonDebitPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.etCard.getText().toString().isEmpty()) {
                    if (!binding.etExpiry.getText().toString().isEmpty()) {
                        if (!binding.etCvv.getText().toString().isEmpty()) {
                            type = "card";
                            if (NetworkManager.isNetworkAvailable(PaymentActivity.this)) {
                                goToOrderSuccess();
                            } else {
                                binding.containerNoInternet.setVisibility(View.VISIBLE);
                            }
                        } else binding.etCvv.setError("Please enter CVV");
                    } else binding.etExpiry.setError("Please enter expiry");
                } else binding.etCard.setError("Please enter card no");
            }
        });

        binding.buttonConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "direct";
                if (NetworkManager.isNetworkAvailable(PaymentActivity.this)) {
                    goToOrderSuccess();
                } else {
                    binding.containerNoInternet.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void goToOrderSuccess() {
        Intent intent = new Intent(this, BookingSuccessActivity.class);
        intent.putExtra("checkIn", checkIn);
        intent.putExtra("checkOut", checkOut);
        intent.putExtra("adult", adult);
        intent.putExtra("children", children);
        intent.putExtra("rooms", room);
        intent.putExtra("total", total);
        intent.putExtra("type", type);
        intent.putExtra("name", name);
        intent.putExtra("location", location);
        intent.putExtra("image", image);
        intent.putExtra("roomType", roomType);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
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
            name = bundle.getString("name");
            image = bundle.getString("image");
            location = bundle.getString("location");
            roomType = bundle.getString("roomType");
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.tvCash.setText("Amount to be paid : £" + total);
    }
}