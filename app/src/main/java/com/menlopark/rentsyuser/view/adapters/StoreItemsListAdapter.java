package com.menlopark.rentsyuser.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.databinding.ListStoreItemsBinding;
import com.menlopark.rentsyuser.model.Location;
import com.menlopark.rentsyuser.ui.activitys.MainActivity;
import com.menlopark.rentsyuser.ui.fragments.DetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2/1/2018.
 */

public abstract class StoreItemsListAdapter extends RecyclerView.Adapter<StoreItemsListAdapter.LocationViewHolder> {

    private List<Location> movies = new ArrayList<>();
    private int rowLayout;
    private Context context;
    private final List<com.menlopark.rentsyuser.model.stores.Detail>  mValues;


    public class LocationViewHolder extends RecyclerView.ViewHolder {

        ListStoreItemsBinding binding;
        public LocationViewHolder(ListStoreItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    public StoreItemsListAdapter(List<com.menlopark.rentsyuser.model.stores.Detail>  mValues, int rowLayout, Context context) {
        this.mValues = mValues;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_store_items, parent, false);
        return new LocationViewHolder((ListStoreItemsBinding) DataBindingUtil.bind(itemView));
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, final int position) {
        holder.binding.setAdapter(this);
//        holder.binding.setData_locations(movies.get(position));

        holder.binding.locationName.setText(movies.get(position).getName());
        holder.binding.locationDetails.setText(movies.get(position).getDetails());
        Picasso.with(context).load(R.drawable.img_item).placeholder(R.drawable.white_bg).into(holder.binding.imgLocation);

        holder.binding.locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(context, MainActivity.class);
//                i.putExtra(context.getString(R.string.INTENT_LOCATION),movies.get(position).getName());
//                context.startActivity(i);
                //commanFragmentCall(new DetailsFragment().newInstance(movies.get(position).getName().toString(),""));

                Intent detailsScreen=new Intent(context, DetailsFragment.class);
                detailsScreen.putExtra("item_name", movies.get(position).getName().toString());
                detailsScreen.putExtra("item_id", movies.get(position).getId().toString());
                context.startActivity(detailsScreen);

            }
        });
        holder.binding.locationCost.setText("$80/day");
    }



    public void commanFragmentCall(Fragment fragment) {


        Fragment cFragment = fragment;
        if (cFragment != null) {
            FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
            fragmentManager.popBackStack("Rentsy", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment, cFragment);
            fragmentTransaction.isAddToBackStackAllowed();
            fragmentTransaction.commit();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    protected abstract int getLayoutIdForPosition(int position);

   /* @BindingAdapter({"app:onClick"})
    public void onLocationClick(Location viewModel)
    {
        Toast.makeText(context,viewModel.getName(),Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public int getItemCount() {
        return movies.size();
    }
}