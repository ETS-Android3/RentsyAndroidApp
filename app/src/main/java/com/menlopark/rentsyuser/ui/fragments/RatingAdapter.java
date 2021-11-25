package com.menlopark.rentsyuser.ui.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.model.Rating.Detail;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.MyViewHolder> {

    private Context mContext;
    public ArrayList<Detail> SmileList;
    String sRating, sCat_id;
    private onPostClickListener listener;
    int position_update;
    public ArrayList<String> arrayList_update = new ArrayList<String>();
    String sData;
    JSONArray jsonArray = new JSONArray();


    ArrayList<HashMap<String, String>> cat_id_ArrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> rating_ArrayList = new ArrayList<HashMap<String, String>>();

    HashMap<String, String> catIdMap = new HashMap<String, String>();
    HashMap<String, Float> catRatMap = new HashMap<String, Float>();
    HashMap<String, String> mainMap = new HashMap<String, String>();

    public RatingAdapter(ArrayList<Detail> smileList, FragmentActivity activity) {
        this.SmileList = smileList;
        this.mContext = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_rating, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        arrayList_update.clear();
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                sCat_id = String.valueOf(SmileList.get(position).getCatalogueId());
                sRating = String.valueOf(rating);

                catRatMap.put(position + "", rating);
                catIdMap.put(position + "", SmileList.get(position).getCatalogueId().toString());
            }
        });

        holder.tv_title.setText(SmileList.get(position).getItemname());
        Picasso.with(mContext).load(SmileList.get(position).getImages()).into(holder.iv_img);
        //  holder.ratingBar.setRating(Float.valueOf(SmileList.get(position).getRates().toString()));
       /* for (int i = 0; i < SmileList.size(); i++) {

            catRatMap.put(i + "", SmileList.get(position).getRates().toString());
            catIdMap.put(i + "", SmileList.get(position).getCatalogueId().toString());
        }*/
    }

    public JSONArray getRatingData() {
        for (int i = 0; i < catIdMap.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("catalogue_id", Integer.valueOf(catIdMap.get(i + "")));
                jsonObject.put("ratings", catRatMap.get(i + ""));

                Log.e("DAta_JSON", jsonObject.toString());
                jsonArray.put(jsonObject);
                Log.e("DAta_Arry", jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return jsonArray;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public JSONArray getArrayClear() {
        int len = jsonArray.length();

        if (jsonArray != null) {
            for (int i = 0; i < len; i++) {
                jsonArray.remove(0);
                Log.e("Data", jsonArray.toString());
            }
        }

        catRatMap.clear();
        catIdMap.clear();
       // Toast.makeText(mContext, "remove", Toast.LENGTH_SHORT).show();
        return jsonArray;
    }


    @Override
    public int getItemCount() {
        return SmileList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_img;
        TextView tv_title;
        RatingBar ratingBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            final int pos = getAdapterPosition();

            iv_img = itemView.findViewById(R.id.iv_img);
            tv_title = itemView.findViewById(R.id.tv_title);
            ratingBar = itemView.findViewById(R.id.rating_bar);


        }
    }

    public interface onPostClickListener {
        void onItemClick(Detail item);
    }
}
