package com.example.hotelmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelmanagement.data.model.HistoryModel;
import com.example.hotelmanagement.databinding.BookingHistoryItemBinding;

import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HotelViewHolder> {
    private Context context;

    private List<HistoryModel> list;
    String newline = System.getProperty("line.separator");

    public HistoryAdapter(Context context, List<HistoryModel> list) {
        this.context = context;

        this.list = list;
    }

    @NonNull
    @Override
    public HistoryAdapter.HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BookingHistoryItemBinding binding = BookingHistoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HistoryAdapter.HotelViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HotelViewHolder holder, int position) {
        HistoryModel model = list.get(holder.getAdapterPosition());
        holder.binding.tvHotelName.setText(model.getShopName());
        holder.binding.tvBookedDate.setText(model.getBooked_on());
        holder.binding.tvCheckIn.setText(model.getCheck_in());
        holder.binding.tvCheckOut.setText(model.getCheck_out());
        holder.binding.tvRooms.setText(model.getRoomType() + " X " + model.getRooms());
        holder.binding.tvHotelLocation.setText(model.getShopLocation());
        holder.binding.tvPayment.setText(model.getType());
        holder.binding.tvAmount.setText(model.getAmount() + "£");

        StringBuilder builder = new StringBuilder();
        builder.append("Adults  " + "X" + model.adult + newline);
        builder.append("Children  " + "X" + model.children);
        holder.binding.tvGuests.setText(builder);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        private BookingHistoryItemBinding binding;

        public HotelViewHolder(@NonNull BookingHistoryItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
