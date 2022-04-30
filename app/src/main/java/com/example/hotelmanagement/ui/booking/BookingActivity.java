package com.example.hotelmanagement.ui.booking;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.example.hotelmanagement.BaseActivity;
import com.example.hotelmanagement.R;
import com.example.hotelmanagement.databinding.ActivityBookingBinding;
import com.example.hotelmanagement.ui.SummaryActivity;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookingActivity extends BaseActivity {
    private ActivityBookingBinding binding;
    Bundle b;
    String price, checkIn, checkOut, calculatedPrice, image, location, name;
    private int maxLimit = 0;
    private int roomType = 1;
    private int totalGuests = 0;
    private final int roomMaxLimit = 10;
    private int adult = 0;
    private int children = 0;
    private int room = 1;
    final Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setFromIntent();
        initView();
    }

    private void initView() {
        setupInitialDates();
        updateRoomLimit();
        binding.tvCheckIn.setOnClickListener(view -> showDatePicker("checkIn"));
        binding.tvCheckOut.setOnClickListener(view -> showDatePicker("checkOut"));
        binding.chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if (checkedId == R.id.chip_single) {
                    roomType = 1;
                    binding.layoutChildren.setVisibility(View.GONE);
                } else {
                    roomType = 3;
                    binding.layoutChildren.setVisibility(View.VISIBLE);
                }
                updateRoomLimit();
                refreshGuests();
            }
        });
        binding.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalGuests() > 0) {
                    Intent intent = new Intent(BookingActivity.this, SummaryActivity.class);
                    intent.putExtra("checkIn", checkIn);
                    intent.putExtra("checkOut", checkOut);
                    intent.putExtra("adult", adult);
                    intent.putExtra("children", children);
                    intent.putExtra("rooms", room);
                    intent.putExtra("total", calculatedPrice);
                    intent.putExtra("name", name);
                    intent.putExtra("location", location);
                    intent.putExtra("image", image);
                    intent.putExtra("roomType",roomType);
                    startActivity(intent);
                } else
                    showToast(BookingActivity.this, "Please add guests first");
            }
        });

        binding.adultAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adult < maxLimit) {
                    if (checkTotalGuests()) {
                        adult++;
                        binding.tvAdult.setText(String.valueOf(adult));
                    } else
                        showToast(BookingActivity.this, "Maximum number of guests added");
                } else
                    showToast(BookingActivity.this, "Can't add more people to this room");
            }
        });

        binding.adultMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adult != 0) {
                    adult--;
                    binding.tvAdult.setText(String.valueOf(adult));
                }
            }
        });

        binding.childAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (children < maxLimit) {
                    if (checkTotalGuests()) {
                        children++;
                        binding.tvChildren.setText(String.valueOf(children));
                    } else
                        showToast(BookingActivity.this, "Maximum number of guests added");
                } else
                    showToast(BookingActivity.this, "Can't add more people to this room");
            }
        });

        binding.childRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (children != 0) {
                    children--;
                    binding.tvChildren.setText(String.valueOf(children));
                }
            }
        });

        binding.roomAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (room <= roomMaxLimit) {
                    room++;
                    binding.tvRooms.setText(String.valueOf(room));
                    updateRoomLimit();
                } else
                    showToast(BookingActivity.this, "You cannot add more than 10 rooms");
            }
        });

        binding.roomMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (room > 1) {
                    room--;
                    binding.tvRooms.setText(String.valueOf(room));
                    updateRoomLimit();
                }
            }
        });
    }

    private void setupInitialDates() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        checkIn = dateFormat.format(today);
        binding.tvCheckIn.setText(dateFormat.format(today));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        checkOut = dateFormat.format(tomorrow);
        binding.tvCheckOut.setText(dateFormat.format(tomorrow));
    }

    private void updateRoomLimit() {
        calculatedPrice = price;
        int roomCount = Integer.parseInt(binding.tvRooms.getText().toString());
        maxLimit = roomCount * roomType;
        if (roomType == 1)
            calculatedPrice = String.valueOf(Integer.parseInt(calculatedPrice) * roomCount);
        else
            calculatedPrice = String.valueOf(Integer.parseInt(calculatedPrice) * roomCount * 2);
        binding.tvPrice.setText("Total amount: " + calculatedPrice + "£");
    }

    private void refreshGuests() {
        binding.tvAdult.setText(String.valueOf(0));
        binding.tvChildren.setText(String.valueOf(0));
        adult = 0;
        children = 0;
    }


    private boolean checkTotalGuests() {
        int adultCount = Integer.parseInt(binding.tvAdult.getText().toString());
        int childrenCount = Integer.parseInt(binding.tvChildren.getText().toString());
        totalGuests = adultCount + childrenCount;
        return totalGuests < maxLimit;
    }

    private int totalGuests() {
        int adultCount = Integer.parseInt(binding.tvAdult.getText().toString());
        int childrenCount = Integer.parseInt(binding.tvChildren.getText().toString());
        return totalGuests = adultCount + childrenCount;
    }

    private void setFromIntent() {
        b = getIntent().getExtras();
        binding.tvHotelName.setText(b.getString("name"));
        price = b.getString("price");
        calculatedPrice = price;
        name = b.getString("name");
        image = b.getString("image");
        location = b.getString("location");
    }

    private void showDatePicker(String from) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "dd-MMM-yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                if (from == "checkIn") {
                    checkIn = dateFormat.format(myCalendar.getTime());
                    binding.tvCheckIn.setText(checkIn);
                    myCalendar.add(Calendar.DAY_OF_YEAR, 1);
                    checkOut = dateFormat.format(myCalendar.getTime());
                    binding.tvCheckOut.setText(checkOut);
                } else {
                    String currentDate = dateFormat.format(myCalendar.getTime());
                    Date date1;
                    Date date2;
                    long differenceDates = 0;
                    SimpleDateFormat dates = new SimpleDateFormat("dd-MMM-yyyy");
                    try {
                        date1 = dates.parse(checkIn);
                        date2 = dates.parse(currentDate);
                        long difference = Math.abs(date1.getTime() - date2.getTime());
                        differenceDates = difference / (24 * 60 * 60 * 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (currentDate.equalsIgnoreCase(binding.tvCheckIn.getText().toString())) {
                        showToast(BookingActivity.this, "Please choose another date");
                    } else if (Integer.parseInt(String.valueOf(differenceDates)) > 10) {
                        showToast(BookingActivity.this, "Cannot book dates more than 10 days");
                    } else
                        binding.tvCheckOut.setText(dateFormat.format(myCalendar.getTime()));
                }
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(BookingActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        // dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

}