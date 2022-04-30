package com.example.hotelmanagement.ui.history;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hotelmanagement.BaseActivity;
import com.example.hotelmanagement.adapter.HistoryAdapter;
import com.example.hotelmanagement.data.model.HistoryModel;
import com.example.hotelmanagement.data.prefrence.SessionManager;
import com.example.hotelmanagement.databinding.ActivityHistoryBinding;
import com.example.hotelmanagement.utils.NetworkManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HistoryActivity extends BaseActivity {
    private ActivityHistoryBinding binding;
    private long mLastClickTime = 0;
    private FirebaseFirestore fb;
    private SessionManager sessionManager;
    HistoryAdapter historyAdapter;
    private final ArrayList<HistoryModel> historyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        showLoading(HistoryActivity.this);
        sessionManager = new SessionManager(HistoryActivity.this);
        fb = getFireStoreInstance();
        setupObserver();
        setupHistoryRecyclerView();

    }

    private void setupHistoryRecyclerView() {
        historyAdapter = new HistoryAdapter(this, historyList);
        binding.recyclerHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerHistory.setHasFixedSize(true);
        binding.recyclerHistory.setAdapter(historyAdapter);


        binding.ivBack.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        }));
    }

    private void setupObserver() {

        if (NetworkManager.isNetworkAvailable(HistoryActivity.this)) {
            binding.containerNoInternet.setVisibility(View.GONE);
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            fb.collection("bookings")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                hideLoading();
                                historyList.clear();
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.e("user id =", documentSnapshot.get("userid").toString());
                                    Log.e("user id3 =", String.valueOf((documentSnapshot.get("userid")).equals(sessionManager.getUserId())));
                                    Log.e("user id 1 =", sessionManager.getUserId());
                                    if ((documentSnapshot.get("userid")).equals(sessionManager.getUserId())) {

                                        historyList.add(
                                                new HistoryModel(
                                                        documentSnapshot.get("rooms").toString(),
                                                        documentSnapshot.get("children").toString(),
                                                        documentSnapshot.get("check_out").toString(),
                                                        documentSnapshot.get("check_in").toString(),
                                                        documentSnapshot.get("booked_on").toString(),
                                                        documentSnapshot.get("amount").toString(),
                                                        documentSnapshot.get("adult").toString(),
                                                        documentSnapshot.get("shopImage").toString(),
                                                        documentSnapshot.get("shopLocation").toString(),
                                                        documentSnapshot.get("shopName").toString(),
                                                        documentSnapshot.get("status").toString(),
                                                        documentSnapshot.get("type").toString(),
                                                        documentSnapshot.get("userid").toString(),
                                                        documentSnapshot.get("roomType").toString()
                                                )
                                        );
                                        Log.e("historyList=", String.valueOf(historyList));
                                    }
                                }
                                hideLoading();
                                if (historyList.size() > 0) {
                                    binding.recyclerHistory.setVisibility(View.VISIBLE);
                                    binding.ivNoData.setVisibility(View.GONE);
                                    historyAdapter.notifyDataSetChanged();
                                } else {
                                    binding.recyclerHistory.setVisibility(View.GONE);
                                    binding.ivNoData.setVisibility(View.VISIBLE);
                                    showToast(HistoryActivity.this, "Sorry, No History available");
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast(HistoryActivity.this, e.getMessage());
                }
            });
        } else {
            binding.containerNoInternet.setVisibility(View.VISIBLE);
        }
    }
}