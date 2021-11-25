package com.menlopark.rentsyuser.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.model.add_to_cart.CartInfo;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CartItemListAdapter extends RecyclerView.Adapter<CartItemListAdapter.CartListHolder> {

    List<CartInfo> cartInfos;
    private Context mContext;


    public CartItemListAdapter(Context mContext, List<CartInfo> cartInfos) {
        this.mContext = mContext;
        this.cartInfos = cartInfos;

    }

    @Override
    public CartListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.list_item_cart_item, parent, false);
        return new CartListHolder(rootView);
    }

    @Override
    public void onBindViewHolder(CartListHolder holder, int position) {


        holder.item_name.setText(cartInfos.get(position).getCataloguename());
        holder.itemPrice.setText("$" + cartInfos.get(position).getAmount().toString());
        holder.date_time.setText(cartInfos.get(position).getDate() + " " + cartInfos.get(position).getTime());
        holder.item_time_duration.setText(cartInfos.get(position).getLength() + " " + (cartInfos.get(position).getLength()>1?cartInfos.get(position).getBookingPeriod()+"s":cartInfos.get(position).getBookingPeriod()));

        Picasso.with(mContext).load(cartInfos.get(position).getImages()).placeholder(R.drawable.white_bg).into(holder.image);
    }


    @Override
    public int getItemCount() {
        return cartInfos.size();
    }


    public class CartListHolder extends RecyclerView.ViewHolder {


        public TextView item_name, itemPrice, date_time, item_time_duration;
        ImageView image;

        public CartListHolder(View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            date_time = itemView.findViewById(R.id.date_time);
            item_time_duration = itemView.findViewById(R.id.item_time_duration);
            image = itemView.findViewById(R.id.image);
        }
    }


}
