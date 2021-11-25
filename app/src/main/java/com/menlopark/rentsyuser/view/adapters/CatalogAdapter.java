package com.menlopark.rentsyuser.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.ui.fragments.DetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {

    private List<com.menlopark.rentsyuser.model.stores.Catalogs.Detail> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    final String token;


    // data is passed into the constructor
    public CatalogAdapter(Context context, List<com.menlopark.rentsyuser.model.stores.Catalogs.Detail> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        token = PreferenceUtil.getPref(context).getString(Constants.pref_App_Token,"");
    }




    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_store_item_row, parent, false);
        return new ViewHolder(view);
    }



    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.location_name.setText(mData.get(position).getItemname());
        holder.location_details.setText(mData.get(position).getCategoryname());

        if (mData.get(position).getImages()!=null){
            if (!mData.get(position).getImages().equals("")) {

                Picasso.with(context).load(mData.get(position).getImages()).into(holder.img_location);
            }
        }




    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

   public interface MyInterface {
        void someEvent();
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView location_name, location_details, location_view;
        ImageView img_location;

        ViewHolder(View itemView) {
            super(itemView);
            location_name = itemView.findViewById(R.id.location_name);
            location_details = itemView.findViewById(R.id.location_details);
            location_view = itemView.findViewById(R.id.location_view);
            img_location = itemView.findViewById(R.id.img_location);
            itemView.setOnClickListener(this);


            location_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position_update = getPosition();
                    String item_id = mData.get(getPosition()).getId();
                    String item_name = mData.get(getPosition()).getItemname();

                 /*   DetailsFragment fragmentB = new DetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("item_id", item_id);
                    fragmentB.setArguments(bundle);


                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragmentB).addToBackStack(null).commit();
*/

                    Intent detailsScreen=new Intent(context, DetailsFragment.class);
                    detailsScreen.putExtra("item_id", item_id);
                    detailsScreen.putExtra("item_name", item_name);
                    context.startActivity(detailsScreen);

                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).getId();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    public interface ItemClickListene {
        void someEvent();
    }

    public void commanFragmentCall(Fragment fragment, String item_id) {


        /*Fragment cFragment = fragment;
        if (cFragment != null) {
            FragmentManager fragmentManager =context.getSupportFragmentManager();
            fragmentManager.popBackStack("Rentsy", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment, cFragment);
            fragmentTransaction.isAddToBackStackAllowed();
            fragmentTransaction.commit();
        }*/
    }

}
