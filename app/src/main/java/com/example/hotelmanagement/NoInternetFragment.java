package com.example.hotelmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.hotelmanagement.databinding.FragmentNooInternetBinding;
import com.example.hotelmanagement.utils.NetworkManager;
import com.google.android.material.snackbar.Snackbar;

public class NoInternetFragment extends Fragment {
    private FragmentNooInternetBinding binding;
    Snackbar sb;

    public NoInternetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNooInternetBinding.inflate(getLayoutInflater());
        binding.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkManager.isNetworkAvailable(getContext())) {
                    binding.mainView.setVisibility(View.GONE);
                } else {
                    sb = Snackbar.make(view, getContext().getString(R.string.check_internet), Snackbar.LENGTH_SHORT);
                    sb.show();
                }
            }

        });
        return binding.getRoot();
    }
}