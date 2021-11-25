package com.menlopark.rentsyuser.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.databinding.ListItemBookingBinding;
import com.menlopark.rentsyuser.model.Location;
import com.menlopark.rentsyuser.ui.activitys.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2/1/2018.
 */

public abstract class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.LocationViewHolder> {

    private List<Location> movies = new ArrayList<>();
    private int rowLayout;
    private Context context;

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        ListItemBookingBinding binding;
        public LocationViewHolder(ListItemBookingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public BookingListAdapter(List<Location> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_booking, parent, false);
        return new LocationViewHolder((ListItemBookingBinding) DataBindingUtil.bind(itemView));
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, final int position) {
        holder.binding.setAdapter(this);
       holder.binding.locationName.setText(movies.get(position).getName());
        holder.binding.locationDetails.setText(movies.get(position).getDetails());
        Picasso.with(context).load(R.drawable.white_bg).placeholder(R.drawable.white_bg).into(holder.binding.imgLocation);

        /*holder.binding.relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commanFragmentCall(new SummaryFragment().newInstance("SUMMARY",""));
            }
        });*/
        holder.binding.txtDuration.setText("2 Hours");
        holder.binding.txtTime.setText("7:00pm");
    }



    public void commanFragmentCall(Fragment fragment) {

        Fragment cFragment = fragment;
        if (cFragment != null) {
            FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
            fragmentManager.popBackStack("Triocab", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment, cFragment);
            fragmentTransaction.isAddToBackStackAllowed();
            fragmentTransaction.commitNow();
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