package com.example.hotelmanagement.ui.auth;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import com.example.hotelmanagement.BaseActivity;
import com.example.hotelmanagement.R;
import com.example.hotelmanagement.data.prefrence.SessionManager;
import com.example.hotelmanagement.databinding.ActivityLoginBinding;
import com.example.hotelmanagement.ui.hotel.HomeActivity;
import com.example.hotelmanagement.utils.NetworkManager;
import com.example.hotelmanagement.utils.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private long mLastClickTime = 0;
    private final FirebaseFirestore fb = getFireStoreInstance();
    private boolean isMatch = false;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(LoginActivity.this);
        initView();
    }

    private void initView() {
        binding.tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation.isValidEmail(binding.txtEmail.getText().toString())) {
                    if (!binding.txtPass.getText().toString().isEmpty()) {
                        login(binding.txtEmail.getText().toString(),binding.txtPass.getText().toString());
                    } else
                        binding.txtPass.setError("Please enter valid password");
                } else
                    binding.txtEmail.setError("Please enter valid email");
            }
        });
    }

    private void login(String userName, String passWord) {
        if (NetworkManager.isNetworkAvailable(LoginActivity.this)) {
            binding.containerNoInternet.setVisibility(View.GONE);
            showLoading(this);
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            fb.collection("User")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                hideLoading();
                                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                    if (Objects.requireNonNull(documentSnapshot.get("email")).toString().equals(userName) &&
                                            Objects.requireNonNull(documentSnapshot.get("password")).toString().equals(passWord)) {

                                        isMatch = true;
                                        sessionManager.setUserId(documentSnapshot.get("userid").toString());
                                        sessionManager.setDocumentId(documentSnapshot.getId());
                                        sessionManager.setUserName(documentSnapshot.get("username").toString());
                                        sessionManager.setLogin(true);
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        showToast(LoginActivity.this, "Login Successfully");
                                        startActivity(intent);
                                        finishAffinity();
                                    }
                                }

                                if (!isMatch)
                                    showToast(LoginActivity.this, "Enter valid user details");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideLoading();
                    showToast(LoginActivity.this, getString(R.string.error));
                }
            });
        } else
            binding.containerNoInternet.setVisibility(View.VISIBLE);
        // showSnackBar(binding.getRoot(), getString(R.string.check_internet));
    }
}