package com.menlopark.rentsyuser.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.model.Booking_Summary.BookingSummaryParams;
import com.menlopark.rentsyuser.model.Booking_Summary.BookingSummaryRes;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.util.Dialog;

import retrofit2.Call;



public class SummaryFragment extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    String cart_id;
    ApiService apiService;
    Dialog loader;
    String token;
    Activity act;
   
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_summary);
        act = SummaryFragment.this;
        cart_id = getIntent().getStringExtra("cart_id");
        init();
    }

    private void init() {
        token = PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, "");
        apiService = ApiClient.getClient(token).create(ApiService.class);

    }

    @Override
    public void onResume() {
        super.onResume();
        call_Get_Bokking_summary();
    }


    private void call_Get_Bokking_summary() {

        BookingSummaryParams bookingSummaryParams = new BookingSummaryParams();
        bookingSummaryParams.setCart_id(cart_id);

        loader = new Dialog(act);
        loader.show();
        Call<BookingSummaryRes> call = apiService.call_Booking_Summary(bookingSummaryParams);
        call.enqueue(new retrofit2.Callback<BookingSummaryRes>() {
            @Override
            public void onResponse(Call<BookingSummaryRes> call, retrofit2.Response<BookingSummaryRes> response) {
                if (response.body().getStatus() == 200) {
                    loader.dismiss();
                    //  Toast.makeText(act, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    /*binding.tvCompanynameSummary.setText(response.body().getData().getView().getStoreName());
                    binding.tvItemSummary.setText(response.body().getData().getView().getItemName());
                    binding.tvQuantitySummary.setText(response.body().getData().getView().getQty().toString());
                    binding.tvDateSummary.setText(response.body().getData().getView().getDate());
                    binding.tvTimeSummary.setText(response.body().getData().getView().getTime());
                    binding.tvLengthSummary.setText(response.body().getData().getView().getLength().toString() +"");
                    binding.tvPriceSummary.setText("$" + response.body().getData().getView().getBookingrate());
                    binding.tvRecentPeriodSummary.setText(response.body().getData().getView().getBookingPeriod());
                    binding.tvTotalSummary.setText("$" + response.body().getData().getView().getAmount());


                    Picasso.with(act).load(response.body().getData().getView().getCatalogueImg()).placeholder(R.drawable.img_slide).into(binding.detailImage);
*/
                } else {
                    loader.dismiss();
                    Toast.makeText(act, response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookingSummaryRes> call, Throwable t) {
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
                loader.dismiss();
            }
        });


    }


}
