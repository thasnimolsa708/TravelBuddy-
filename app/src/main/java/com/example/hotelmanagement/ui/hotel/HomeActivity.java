package com.example.hotelmanagement.ui.hotel;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hotelmanagement.BaseActivity;
import com.example.hotelmanagement.R;
import com.example.hotelmanagement.adapter.HotelAdapter;
import com.example.hotelmanagement.data.model.HotelModel;
import com.example.hotelmanagement.data.prefrence.SessionManager;
import com.example.hotelmanagement.databinding.ActivityHomeBinding;
import com.example.hotelmanagement.ui.auth.LoginActivity;
import com.example.hotelmanagement.ui.history.HistoryActivity;
import com.example.hotelmanagement.ui.profile.ProfileActivity;
import com.example.hotelmanagement.utils.NetworkManager;
import com.example.hotelmanagement.utils.OnItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends BaseActivity implements OnItemClickListener {
    private ActivityHomeBinding binding;
    private long mLastClickTime = 0;
    private FirebaseFirestore fb;
    private LocationManager locationManager;
    private String currentLocation;
    private String city;
    HotelAdapter hotelAdapter;
    private SessionManager sessionManager;
    private final ArrayList<HotelModel> hotelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(HomeActivity.this);
        fb = getFireStoreInstance();
        requestLocationPermission();
        setupRestaurantsRecyclerView();
        setupObserver();
        btnClick();
    }


    private void btnClick() {
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogOutPress();
            }
        });
        binding.history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, HistoryActivity.class));
            }
        });
        binding.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setupObserver();
            }
        });

    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getLocation();
        }
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull List<Location> locations) {
                }

                @Override
                public void onFlushComplete(int requestCode) {
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                }

                @Override
                public void onLocationChanged(@NonNull Location location) {
                    Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
                    List<Address> addresses;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (!addresses.isEmpty()) {
                            currentLocation = addresses.get(0).getLocality();
                            city = addresses.get(0).getSubAdminArea();
                            setCurrentLocation();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentLocation() {
        binding.tvMainLocation.setText(city);
        binding.tvSubLocation.setText(city);
    }

    private void setupObserver() {
        showShimmer();
        if (!binding.swipeRefreshLayout.isRefreshing()) {
            binding.layoutStates.setVisibility(View.VISIBLE);
            binding.tvNoInternet.setVisibility(View.GONE);
        }
        if (NetworkManager.isNetworkAvailable(HomeActivity.this)) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            fb.collection("hotels")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                hotelList.clear();
                                binding.swipeRefreshLayout.setRefreshing(false);
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    hotelList.add(
                                            new HotelModel(
                                                    documentSnapshot.get("name").toString(),
                                                    documentSnapshot.get("image").toString(),
                                                    documentSnapshot.get("location").toString(),
                                                    documentSnapshot.get("rating").toString(),
                                                    documentSnapshot.get("price").toString(),
                                                    documentSnapshot.get("description").toString()
                                            )
                                    );
                                }
//                                updateRecyclerView();
                                hideLoading();
                                if (hotelList.size() > 0) {
                                    binding.recyclerHotels.setVisibility(View.VISIBLE);
//                                    binding.ivNoData.setVisibility(View.GONE);
                                    hotelAdapter.notifyDataSetChanged();
                                } else {
                                    binding.recyclerHotels.setVisibility(View.GONE);
//                                    binding.ivNoData.setVisibility(View.VISIBLE);
                                    showToast(HomeActivity.this, getString(R.string.no_items));
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    stopShimmer();
                    binding.swipeRefreshLayout.setRefreshing(false);
                    showToast(HomeActivity.this, e.getMessage());
                }
            });
        } else {
            stopShimmer();
            binding.recyclerHotels.setVisibility(View.GONE);
            binding.swipeRefreshLayout.setRefreshing(false);
            binding.tvNoInternet.setVisibility(View.VISIBLE);

        }
    }

    private void showShimmer() {
//        binding.swipe_refresh_layout
        binding.layoutStates.setVisibility(View.VISIBLE);
        binding.recyclerHotels.setVisibility(View.GONE);
        binding.tvNoInternet.setVisibility(View.GONE);
        binding.layoutStates.startShimmer();
    }

    private void stopShimmer() {
        binding.layoutStates.setVisibility(View.GONE);
        binding.recyclerHotels.setVisibility(View.VISIBLE);
        binding.layoutStates.stopShimmer();
    }

    //    private void updateRecyclerView() {
//        stopShimmer();
//        if (hotelList != null && !hotelList.isEmpty()) {
//            hotelAdapter.notifyDataSetChanged();
//        } else {
//            binding.recyclerHotels.setVisibility(View.GONE);
//            showSnackBar(binding.getRoot(), getString(R.string.no_items));
//        }
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    showToast(HomeActivity.this, "Permission to Location is denied");
                }
                return;
            }
        }
    }

    public void onLogOutPress() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(HomeActivity.this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure want to Logout ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sessionManager.clear();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
                dialogInterface.dismiss();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();

    }

    private void setupRestaurantsRecyclerView() {
        hotelAdapter = new HotelAdapter(this, hotelList, this);
        binding.recyclerHotels.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerHotels.setHasFixedSize(true);
        binding.recyclerHotels.setAdapter(hotelAdapter);
    }

    @Override
    public void onItemClick(Integer position) {
        HotelModel model = hotelList.get(position);
        Intent i = new Intent(this, HotelDetailsActivity.class);
        Bundle data = new Bundle();
        data.putString("name", model.getName());
        data.putString("location", model.getlocation());
        data.putString("rating", model.getRating());
        data.putString("image", model.getImage());
        data.putString("price", model.getPrice());
        data.putString("description", model.getDescription());
        i.putExtras(data);
        startActivity(i);
    }
}
