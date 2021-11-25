package com.menlopark.rentsyuser.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.model.Notification.Detail;
import com.menlopark.rentsyuser.ui.fragments.RatingFragment;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Detail> mItems;
    private LayoutInflater mInflater;


    public NotificationAdapter(Context mContext, List<Detail> mItems) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mItems = mItems;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_setting_b, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.notification_title.setText(mItems.get(position).getmTitle());

        String sRebook_status = mItems.get(position).getmRebookStatus()+"";
        String sreview_status = mItems.get(position).getmReviewStatus()+"";
        String sextend_booking_status = mItems.get(position).getmExtendBookingStatus()+"";

        if (sRebook_status.equals("1")) {
            holder.notification_action.setVisibility(View.VISIBLE);
            holder.notification_action.setText(mItems.get(position).getmMessage());
            holder.notification_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RatingFragment fragmentB = new RatingFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("order_id", String.valueOf(mItems.get(position).getmOrderId()));
                    fragmentB.setArguments(bundle);


                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragmentB).addToBackStack(null).commit();

                    //Toast.makeText(mContext, "Redirect Rating Fragment WITH Order ID", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (sreview_status.equals("1")) {
            holder.notification_action.setVisibility(View.VISIBLE);
            holder.notification_action.setText(mItems.get(position).getmMessage());
        }

        if (sextend_booking_status.equals("1")) {
            holder.notification_action.setVisibility(View.VISIBLE);
            holder.notification_action.setText(mItems.get(position).getmMessage());
        }
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView notification_title, notification_action;

        public ViewHolder(View itemView) {
            super(itemView);
            notification_title = itemView.findViewById(R.id.notification_title);
            notification_action = itemView.findViewById(R.id.notification_action);
        }

    }
}