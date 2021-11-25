package com.menlopark.rentsyuser.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.databinding.AdapterCalenderDataBinding;
import com.menlopark.rentsyuser.ui.fragments.Panding_B_Summary_Fragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Calender_Adapter extends RecyclerView.Adapter<Calender_Adapter.MyViewHolder> {

    private List<com.menlopark.rentsyuser.model.Calendar_Data.View> mData;
    private LayoutInflater mInflater;
    private CatalogAdapter.ItemClickListener mClickListener;
    private Context context;
    String item_id;
    Date eDDte;
    String dynamicTime;
    ArrayList<String> arrayList_date;


    public Calender_Adapter(FragmentActivity activity, List<com.menlopark.rentsyuser.model.Calendar_Data.View> bookiLists, ArrayList<String> arrayList_date) {
        this.context = activity;
        this.mData = bookiLists;
        this.arrayList_date = arrayList_date;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_calender_data, parent, false);
        return new MyViewHolder((AdapterCalenderDataBinding) DataBindingUtil.bind(itemView));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#e8e7e9"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }

        holder.binding.tvStoreNameCal.setText(mData.get(position).getStoreName());
        holder.binding.tvItemNameCal.setText(mData.get(position).getItemName());
        holder.binding.tvTimeCal.setText(arrayList_date.get(position).toString());
        holder.binding.tvHrCal.setText(mData.get(position).getLength()+" "+mData.get(position).getBookingPeriod());

        Picasso.with(context).load(mData.get(position).getCatalogueImg())
                .placeholder(R.drawable.white_bg)
                .into(holder.binding.imgPbooking);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Panding_B_Summary_Fragment.class);
                i.putExtra("order_id", mData.get(position).getId() + "");
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterCalenderDataBinding binding;

        public MyViewHolder(AdapterCalenderDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

}
