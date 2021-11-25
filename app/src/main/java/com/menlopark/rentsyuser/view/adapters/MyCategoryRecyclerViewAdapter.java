package com.menlopark.rentsyuser.view.adapters;

import static com.menlopark.rentsyuser.server_access.Constants.Current_Latitude;
import static com.menlopark.rentsyuser.server_access.Constants.Current_Longitude;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
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
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.ui.activitys.ItemPageActivity;
import com.menlopark.rentsyuser.ui.activitys.MainActivity;
import com.menlopark.rentsyuser.ui.fragments.CategoryFragment.OnListFragmentInteractionListener;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CategoryItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCategoryRecyclerViewAdapter extends RecyclerView.Adapter<MyCategoryRecyclerViewAdapter.ViewHolder> {

    private final List<com.menlopark.rentsyuser.model.stores.Detail> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context context;


    public MyCategoryRecyclerViewAdapter(List<com.menlopark.rentsyuser.model.stores.Detail> items, OnListFragmentInteractionListener listener, Context cont) {
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

        Log.d("listdata","check---"+mValues.get(position).getId()+" "+mValues.get(position).getLongitude()+" "+mValues.get(position).getBusinessCategoryName());


        if(mValues.get(position).getLatitude()!=null && mValues.get(position).getLongitude()!=null){
            if(!mValues.get(position).getLatitude().toString().equals("") && !mValues.get(position).getLongitude().equals("")){

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(final Void ... params ) {
                        // something you know that will take a few seconds
                        return distanceBetween(Double.parseDouble(mValues.get(position).getLatitude()),Double.parseDouble(mValues.get(position).getLongitude()),Current_Latitude,Current_Longitude)+"Km";
                    }

                    @Override
                    protected void onPostExecute( final String result ) {
                        holder.distance.setText(result);
                    }
                }.execute();



            }else{
                holder.distance.setText("");
            }
        }
        else{
            holder.distance.setText("");
        }


        if (mValues.get(position).getRating() != null) {
            holder.ratingBar.setRating(mValues.get(position).getRating());
        }
        Picasso.with(context).load(mValues.get(position).getImg_name()).placeholder(R.drawable.white_bg).into(holder.img_companylogo);
        final String token = PreferenceUtil.getPref(context).getString(Constants.pref_App_Token,"");

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    mListener.onListFragmentInteraction(holder.mItem);
//
//                    //commanFragmentCall(new ItemPageFragment().newInstance(mValues.get(position).getBusinessName(), String.valueOf(mValues.get(position).getId()), token));
//                    Intent itemPageScreen=new Intent(context, ItemPageActivity.class);
//                    itemPageScreen.putExtra("storeName",mValues.get(position).getBusinessName());
//                    itemPageScreen.putExtra("storeId",String.valueOf(mValues.get(position).getId()));
//
//                    Log.d("store_val_pass","1storeid-"+String.valueOf(mValues.get(position).getId())+
//                            "name-"+mValues.get(position).getBusinessName());
//                    context.startActivity(itemPageScreen);
//
//                }
//            }
//        });
        holder.mainrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("store_val_pass","1-storeid-"+mValues.get(position).getId()+
                        "name-"+mValues.get(position).getBusinessName());
                Intent itemPageScreen=new Intent(context, ItemPageActivity.class);
                itemPageScreen.putExtra("storeName",mValues.get(position).getBusinessName());
                itemPageScreen.putExtra("storeId",String.valueOf(mValues.get(position).getId()));
                context.startActivity(itemPageScreen);

            }
        });
//        holder.details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //commanFragmentCall(new ItemPageFragment().newInstance(mValues.get(position).getBusinessName(), String.valueOf(mValues.get(position).getId()), token));
//
//                Intent itemPageScreen=new Intent(context, ItemPageActivity.class);
//                itemPageScreen.putExtra("storeName",mValues.get(position).getBusinessName());
//                itemPageScreen.putExtra("storeId",String.valueOf(mValues.get(position).getId()));
//                context.startActivity(itemPageScreen);
//                Log.d("store_val_pass","1storeid-"+String.valueOf(mValues.get(position).getId())+
//                        "name-"+mValues.get(position).getBusinessName());
//
//
//            }
//        });
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
        public final LinearLayout details,mainrel;

        public Detail mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mainrel=view.findViewById(R.id.rel_main);
            details = view.findViewById(R.id.details);
            name = view.findViewById(R.id.category_name);
            category = view.findViewById(R.id.location_category);
            distance = view.findViewById(R.id.location_distance);
            ratingBar = view.findViewById(R.id.rating);
            img_companylogo = view.findViewById(R.id.img_companylogo);
        }
    }

    public static Integer distanceBetween(double lat1, double lon1, double lat2, double lon2) {
        float[] distance = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, distance);
        return (int)(distance[0]/1000);
    }

}
