package com.menlopark.rentsyuser.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.databinding.AdapterPenddingBookingBinding;
import com.menlopark.rentsyuser.ui.fragments.Panding_B_Summary_Fragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PendingBookingAdapter extends RecyclerView.Adapter<PendingBookingAdapter.MyViewHolder> {

    private List<com.menlopark.rentsyuser.model.Pendding_Booking.View> mData;
    private LayoutInflater mInflater;
    CatalogAdapter.ItemClickListener mClickListener;
    private Context context;
    String item_id;


    public PendingBookingAdapter(FragmentActivity activity, List<com.menlopark.rentsyuser.model.Pendding_Booking.View> bookiLists) {
        this.context = activity;
        this.mData = bookiLists;
    }

    @Override
    public PendingBookingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pendding_booking, parent, false);
        return new MyViewHolder((AdapterPenddingBookingBinding) DataBindingUtil.bind(itemView));
    }

    @Override
    public void onBindViewHolder(PendingBookingAdapter.MyViewHolder holder, final int position) {

        holder.binding.tvCompanynamePbooking.setText(mData.get(position).getItemName());
        holder.binding.tvTimePbooking.setText(mData.get(position).getTime());
        holder.binding.tvDatePbooking.setText(mData.get(position).getDate());

        Picasso.with(context).load(mData.get(position).getCatalogueImg())
                .placeholder(R.drawable.white_bg)
                .into(holder.binding.imgPbooking);

        item_id = mData.get(position).getOrderId().toString();
        holder.binding.viewPbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Panding_B_Summary_Fragment.class);
                i.putExtra("order_id", mData.get(position).getOrderId().toString());
                context.startActivity(i);
            }
        });
        //image set on picasso
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterPenddingBookingBinding binding;

        public MyViewHolder(AdapterPenddingBookingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }
}
