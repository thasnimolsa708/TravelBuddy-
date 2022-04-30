package com.example.hotelmanagement.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hotelmanagement.data.model.HotelModel;
import com.example.hotelmanagement.databinding.HotelListBinding;
import com.example.hotelmanagement.utils.OnItemClickListener;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {
    private Context context;
    private OnItemClickListener listener;
    private List<HotelModel> list;


    public HotelAdapter(Context context, List<HotelModel> list, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.list = list;
    }

    @NonNull
    @Override
    public HotelAdapter.HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HotelListBinding binding = HotelListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HotelViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelAdapter.HotelViewHolder holder, int position) {
        HotelModel model = list.get(holder.getAdapterPosition());
        holder.binding.textHotelName.setText(model.getName());
        Log.e("name",""+model.getName());
        holder.binding.textHotelLocation.setText(model.getlocation());
        holder.binding.textHotelRating.setText("Rating: "+model.getRating());
        Glide.with(context)
                .load(model.Image)
                .circleCrop()
                .into(holder.binding.imageHotel);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        private HotelListBinding binding;

        public HotelViewHolder(@NonNull HotelListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
