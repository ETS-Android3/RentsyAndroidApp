package com.menlopark.rentsyuser.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.model.Pending_Bookig_summary.PendingSummaryParams;
import com.menlopark.rentsyuser.model.Pending_Bookig_summary.PendingSummaryRes;
import com.menlopark.rentsyuser.model.ordercancel.ResponseCancel;
import com.menlopark.rentsyuser.model.ordercancel.requestCancel;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.squareup.picasso.Picasso;

import retrofit2.Call;

public class Panding_B_Summary_Fragment extends AppCompatActivity {
    private static final String ARG_PARAM1 = "order_id";
    private static final String ARG_PARAM2 = "param2";


    private String mParam2;
    String order_id;
    private Context context;
    ApiService apiService;
    Dialog loader;

    private AppCompatImageView detailImage;
    private TextView detailTitle;
    private LinearLayout llCatImage;
    private AppCompatImageView ivBrand;
    private TextView tvTitleCompany;
    private View view1;
    private LinearLayout llOne;
    private TextView tvCompanynameSummary;
    private LinearLayout llTwo;
    private TextView tvItemSummary;
    private LinearLayout llThree;
    private TextView tvQuantitySummary;
    private LinearLayout llFour;
    private TextView tvDateSummary;
    private LinearLayout llFive;
    private TextView tvTimeSummary;
    private LinearLayout llSix;
    private TextView tvLengthSummary;
    private View view01;
    private LinearLayout ll01;
    private TextView tvPriceSummary;
    private LinearLayout ll02;
    private TextView tvRecentPeriodSummary;
    private LinearLayout ll03;
    private TextView tvTotalSummary;
    private View view2;
    private LinearLayout btnListSummary;
    private Button addAnother;
    private Button btnConfirmBookNow;
    Activity act;

    private void assignViews() {
        detailImage = (AppCompatImageView) findViewById(R.id.detail_image);
        detailTitle = (TextView) findViewById(R.id.detail_title);
        llCatImage = (LinearLayout) findViewById(R.id.ll_cat_image);
        ivBrand = (AppCompatImageView) findViewById(R.id.iv_brand);
        tvTitleCompany = (TextView) findViewById(R.id.tv_title_company);
        view1 = findViewById(R.id.view1);
        llOne = (LinearLayout) findViewById(R.id.ll_one);
        tvCompanynameSummary = (TextView) findViewById(R.id.tv_companyname_summary);
        llTwo = (LinearLayout) findViewById(R.id.ll_two);
        tvItemSummary = (TextView) findViewById(R.id.tv_item_summary);
        llThree = (LinearLayout) findViewById(R.id.ll_three);
        tvQuantitySummary = (TextView) findViewById(R.id.tv_quantity_summary);
        llFour = (LinearLayout) findViewById(R.id.ll_four);
        tvDateSummary = (TextView) findViewById(R.id.tv_date_summary);
        llFive = (LinearLayout) findViewById(R.id.ll_five);
        tvTimeSummary = (TextView) findViewById(R.id.tv_time_summary);
        llSix = (LinearLayout) findViewById(R.id.ll_six);
        tvLengthSummary = (TextView) findViewById(R.id.tv_length_summary);
        view01 = findViewById(R.id.view01);
        ll01 = (LinearLayout) findViewById(R.id.ll_01);
        tvPriceSummary = (TextView) findViewById(R.id.tv_price_summary);
        ll02 = (LinearLayout) findViewById(R.id.ll_02);
        tvRecentPeriodSummary = (TextView) findViewById(R.id.tv_recent_period_summary);
        ll03 = (LinearLayout) findViewById(R.id.ll_03);
        tvTotalSummary = (TextView) findViewById(R.id.tv_total_summary);
        view2 = findViewById(R.id.view2);
        btnListSummary = (LinearLayout) findViewById(R.id.btn_list_summary);
        addAnother = (Button) findViewById(R.id.add_another);
        btnConfirmBookNow = (Button) findViewById(R.id.btnConfirmBookNow);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_panding_b_summary);
        act = Panding_B_Summary_Fragment.this;
        getSupportActionBar().setTitle("Pending Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        order_id = getIntent().getStringExtra("order_id");

        assignViews();
        init();


        btnConfirmBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestCancel requestCancels = new requestCancel();
                requestCancels.setOrderId(Integer.parseInt(order_id));
                requestCancels.setCatalogueId(CatalogueId);

                Call<ResponseCancel> call = apiService.ordercancel(requestCancels);
                call.enqueue(new retrofit2.Callback<ResponseCancel>() {
                    @Override
                    public void onResponse(Call<ResponseCancel> call, final retrofit2.Response<ResponseCancel> response) {
                        if (response.code() == 200) {
                            loader.dismiss();
                            Toast.makeText(act, "Cancel order", Toast.LENGTH_SHORT).show();
                            act.finish();
                        } else {
                            loader.dismiss();
                            Toast.makeText(act, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseCancel> call, Throwable t) {
                        //showProgress(false);
                        Toast.makeText(context, t.toString() + "", Toast.LENGTH_SHORT).show();
                        loader.dismiss();
                    }

                });

            }
        });
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return super.onNavigateUp();
    }

    int CatalogueId = -1;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void init() {

        apiService = ApiClient.getClient(PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, "")).create(ApiService.class);


    }

    @Override
    public void onResume() {
        super.onResume();
        Call_pending_booking_summary();
    }

    private void Call_pending_booking_summary() {

        PendingSummaryParams pendingBookingParams = new PendingSummaryParams();
        pendingBookingParams.setOrder_id(order_id);

        loader = new Dialog(act);
        loader.show();
        Call<PendingSummaryRes> call = apiService.call_Pending_Summaey(pendingBookingParams);
        call.enqueue(new retrofit2.Callback<PendingSummaryRes>() {
            @Override
            public void onResponse(Call<PendingSummaryRes> call, final retrofit2.Response<PendingSummaryRes> response) {
                if (response.code() == 200) {
                    loader.dismiss();

                    if(response.body().getData()!=null){

                        if(response.body().getData().getView().getAmount()!=null){
                            tvPriceSummary.setText("$" + response.body().getData().getView().getAmount().toString());
                        }


                        tvDateSummary.setText(response.body().getData().getView().getDate());
                        tvTimeSummary.setText(response.body().getData().getView().getTime());
                        tvQuantitySummary.setText(response.body().getData().getView().getQty().toString());
                        tvCompanynameSummary.setText(response.body().getData().getView().getItemName());
                        tvLengthSummary.setText(response.body().getData().getView().getLength().toString());
                        tvTitleCompany.setText(response.body().getData().getView().getStoreName());


                        if (response.body().getData().getView().getStoreImg().equals("")) {

                            Picasso.with(act)
                                    .load(R.drawable.white_bg)
                                    .into(detailImage);

                        } else {
                            Picasso.with(act).load(response.body().getData().getView().getStoreImg())
                                    .placeholder(R.drawable.white_bg).into(detailImage);
                        }
                        CatalogueId = response.body().getData().getView().getItem_id();

                        if (response.body().getData().getView().getOrder_status() == 2) {
                            btnConfirmBookNow.setVisibility(View.GONE);
                        } else if (response.body().getData().getView().getOrder_status() == 1) {
                            btnConfirmBookNow.setVisibility(View.VISIBLE);
                            btnConfirmBookNow.setText("Order Cancel");
                        } else if (response.body().getData().getView().getOrder_status() == 0) {
                            btnConfirmBookNow.setVisibility(View.VISIBLE);
                            btnConfirmBookNow.setText("Order Cancel");
                        }


                        if (response.body().getData().getView().getStoreImg().equals("")) {

                            Picasso.with(act)
                                    .load(R.drawable.white_bg)
                                    .into(ivBrand);

                        } else {
                            Picasso.with(act).load(response.body().getData().getView().getStoreImg())
                                    .placeholder(R.drawable.white_bg).into(ivBrand);

                        }



                    }


                } else {
                    loader.dismiss();
                    Toast.makeText(act, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PendingSummaryRes> call, Throwable t) {
                //showProgress(false);
                Log.d("summaryerror","error--"+t.getMessage());
                Toast.makeText(getApplicationContext(), t.toString() + "", Toast.LENGTH_SHORT).show();
                loader.dismiss();
            }

        });

    }
}
