package com.menlopark.rentsyuser.view.adapters;

import static com.menlopark.rentsyuser.server_access.Constants.pref_Current_Store;
import static com.menlopark.rentsyuser.server_access.Constants.pref_Current_Store_id;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.databinding.ListItemLocationsBinding;
import com.menlopark.rentsyuser.model.location.Detail;
import com.menlopark.rentsyuser.ui.activitys.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2/1/2018.
 */

public abstract class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    public int mSelectedItem = -1;
    public ArrayList<String> arrayList_location = new ArrayList<String>();
    int sSelected_Id;
    private List<Detail> cityDetails = new ArrayList<>();
    private int rowLayout;
    private Context context;
    private String type, sSelected_Name;
    private boolean flagSelection = false;

    public LocationAdapter(List<Detail> cityDetails, int rowLayout, Context context, String type) {
        this.cityDetails = cityDetails;
        this.rowLayout = rowLayout;
        this.context = context;
        this.type = type;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_locations, parent, false);
        return new LocationViewHolder(DataBindingUtil.bind(itemView));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final LocationViewHolder holder, final int position) {
        holder.binding.setAdapter(this);

        holder.binding.locationName.setText(cityDetails.get(position).getName());

        if (cityDetails.get(position).getComingsoon().equals("0")) {
            holder.binding.locationDetails.setText("Get notified when Rentsy is available in this city");
            holder.binding.comming.setVisibility(View.VISIBLE);
        } else {
            if (!type.equals("city")) {
                holder.binding.btnRegister.setVisibility(View.VISIBLE);

            }
            holder.binding.locationDetails.setText(cityDetails.get(position).getItems_count() + " Items for Rent");
        }
        if (cityDetails.get(position).getImgName().isEmpty()) {
            holder.binding.imgLocation.setImageResource(R.drawable.white_bg);
        } else
            Picasso.with(context).load(cityDetails.get(position).getImgName()).placeholder(R.drawable.white_bg).into(holder.binding.imgLocation);


        if (mSelectedItem != position) {
            holder.binding.locationName.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
        } else {

            holder.binding.locationName.setTextColor(ContextCompat.getColor(context, R.color.coloPink));

        }

        holder.binding.relMain.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(final View view) {

                if (cityDetails.get(position).getComingsoon().equals("0")) {
                    // Toast.makeText(context, "Comming Soon..!!", Toast.LENGTH_SHORT).show();
                } else {
                    Config.SELECTED_LOCATION = cityDetails.get(position).getName();

                    if (type.equalsIgnoreCase("locations")) {
                        Intent i = new Intent(context, MainActivity.class);
                        i.putExtra(context.getString(R.string.INTENT_LOCATION), cityDetails.get(position).getName());
                        i.putExtra("location_id", cityDetails.get(position).getId().toString());

                        Log.d("selected","loc--"+cityDetails.get(position).getId().toString());
                        Config.setPreference(pref_Current_Store, cityDetails.get(position).getName());
                        Config.setPreference(pref_Current_Store_id, cityDetails.get(position).getId().toString());
                        context.startActivity(i);
                    } else {
                        mSelectedItem = position;
                        arrayList_location.clear();

                        sSelected_Id = cityDetails.get(position).getId();
                        sSelected_Name = cityDetails.get(position).getName();
                        arrayList_location.add(sSelected_Id + "," + sSelected_Name);


                        notifyDataSetChanged();

                        Intent i = new Intent(context, MainActivity.class);
                        i.putExtra("location", cityDetails.get(position).getName());
                        i.putExtra("location_id", cityDetails.get(position).getId() + "");
                        Config.setPreference(pref_Current_Store, cityDetails.get(position).getName());
                        Config.setPreference(pref_Current_Store_id, cityDetails.get(position).getId().toString());
                        context.startActivity(i);

                    }
                }


            }
        });
        if (type.equalsIgnoreCase("city")) {
            holder.binding.imgLocation.setVisibility(View.GONE);
            holder.binding.locationDetails.setVisibility(View.GONE);
            holder.binding.locationName.setGravity(Gravity.CENTER);
            holder.binding.comming.setVisibility(View.GONE);
            if (cityDetails.get(position).getComingsoon().equals("0")) {
                holder.binding.relMain.setVisibility(View.GONE);
            }

        } else {
            holder.binding.imgLocation.setVisibility(View.VISIBLE);
            holder.binding.locationDetails.setVisibility(View.VISIBLE);
            holder.binding.locationName.setGravity(Gravity.LEFT);
            holder.binding.locationDetails.setGravity(Gravity.LEFT);
            holder.binding.locationName.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen._15sdp));


        }
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    protected abstract int getLayoutIdForPosition(int position);

    @Override
    public int getItemCount() {
        return cityDetails.size();
    }

   /* @BindingAdapter({"app:onClick"})
    public void onLocationClick(Location viewModel)
    {
        Toast.makeText(context,viewModel.getName(),Toast.LENGTH_SHORT).show();
    }*/

    public ArrayList<String> getArrayList() {
        return arrayList_location;
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {

        ListItemLocationsBinding binding;

        public LocationViewHolder(ListItemLocationsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }
}