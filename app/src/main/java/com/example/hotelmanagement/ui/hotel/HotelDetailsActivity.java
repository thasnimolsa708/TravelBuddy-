package com.example.hotelmanagement.ui.hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hotelmanagement.databinding.ActivityHotelDetailsBinding;
import com.example.hotelmanagement.ui.booking.BookingActivity;

public class HotelDetailsActivity extends AppCompatActivity {
    private ActivityHotelDetailsBinding binding;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setFromIntent();
        initView();
    }

    private void initView() {
        binding.ivBack.setOnClickListener(view -> {
            finish();
        });
        binding.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HotelDetailsActivity.this, BookingActivity.class);
                Bundle data = new Bundle();
                data.putString("name", b.getString("name"));
                data.putString("price", b.getString("price"));
                data.putString("location", b.getString("location"));
                data.putString("image", b.getString("image"));
                i.putExtras(data);
                startActivity(i);
            }
        });
    }

    private void setFromIntent() {
        b = getIntent().getExtras();
        Glide.with(this)
                .load(b.getString("image"))
                .into(binding.ivHotel);
        binding.tvHotelName.setText(b.getString("name"));
        binding.tvLocation.setText(b.getString("location"));
        binding.textHotelRating.setText("Rating: " + b.getString("rating"));
        binding.tvAbout.setText(b.getString("description"));
        binding.tvPrice.setText("Price: " + b.getString("price") + "£");
    }
}
