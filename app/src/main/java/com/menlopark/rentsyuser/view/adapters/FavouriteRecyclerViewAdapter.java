package com.menlopark.rentsyuser.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.model.dummy.CategoryContent.CategoryItem;
import com.menlopark.rentsyuser.model.stores.Detail;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.ui.activitys.MainActivity;
import com.menlopark.rentsyuser.ui.fragments.FavouriteFragment.OnListFragmentInteractionListener;
import com.menlopark.rentsyuser.ui.activitys.ItemPageActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CategoryItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FavouriteRecyclerViewAdapter extends RecyclerView.Adapter<FavouriteRecyclerViewAdapter.ViewHolder> {

    private final List<Detail> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context context;


    public FavouriteRecyclerViewAdapter(List<Detail> items, OnListFragmentInteractionListener listener, Context cont) {
        mValues = items;
        mListener = listener;
        this.context = cont;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_category_list, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        if (mValues.get(position).getBusinessName() != null) {
            holder.name.setText(mValues.get(position).getBusinessName());
        }
        if (mValues.get(position).getBusinessCategoryName() != null) {
            holder.category.setText(mValues.get(position).getBusinessCategoryName());

        }
        holder.distance.setText("25km");
        if (mValues.get(position).getRating() != null) {
            holder.ratingBar.setRating(mValues.get(position).getRating());
        }
        Picasso.with(context).load(mValues.get(position).getImg_name()).placeholder(R.drawable.white_bg).into(holder.img_companylogo);
        final String token= PreferenceUtil.getPref(context).getString(Constants.pref_App_Token,"");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                    //commanFragmentCall(new ItemPageFragment().newInstance(mValues.get(position).getBusinessName(), String.valueOf(mValues.get(position).getStore_id()),token));

                    Intent itemPageScreen=new Intent(context, ItemPageActivity.class);
                    itemPageScreen.putExtra("storeName",mValues.get(position).getBusinessName());
                    itemPageScreen.putExtra("storeId",String.valueOf(mValues.get(position).getStore_id()));
                    context.startActivity(itemPageScreen);

                }
            }
        });
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemPageScreen=new Intent(context, ItemPageActivity.class);
                itemPageScreen.putExtra("storeName",mValues.get(position).getBusinessName());
                itemPageScreen.putExtra("storeId",String.valueOf(mValues.get(position).getStore_id()));
                context.startActivity(itemPageScreen);

                //commanFragmentCall(new ItemPageFragment().newInstance(mValues.get(position).getBusinessName(), String.valueOf(mValues.get(position).getStore_id()),token));

                /*bindingMain.appbar.contentMain.mainFragment.setVisibility(View.VISIBLE);
                bindingMain.appbar.contentMain.viewpager.setVisibility(View.GONE);
                bindingMain.appbar.tablayout.setVisibility(View.GONE);*/

            }
        });
    }

    public void commanFragmentCall(Fragment fragment) {


        Fragment cFragment = fragment;

        String backStateName = fragment.getClass().getName();
        if (cFragment != null) {
            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
            fragmentManager.popBackStack(backStateName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment, cFragment);
            fragmentTransaction.isAddToBackStackAllowed();
            fragmentTransaction.commit();
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name, distance, category;
        public final RatingBar ratingBar;
        public final ImageView img_companylogo;
        public final LinearLayout details;

        public Detail mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            details = (LinearLayout) view.findViewById(R.id.details);
            name = (TextView) view.findViewById(R.id.category_name);
            category = (TextView) view.findViewById(R.id.location_category);
            distance = (TextView) view.findViewById(R.id.location_distance);
            ratingBar = (RatingBar) view.findViewById(R.id.rating);
            img_companylogo = (ImageView) view.findViewById(R.id.img_companylogo);
        }
    }
}
