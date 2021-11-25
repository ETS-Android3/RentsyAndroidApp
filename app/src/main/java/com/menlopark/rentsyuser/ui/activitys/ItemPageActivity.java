package com.menlopark.rentsyuser.ui.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.api.ServerCall;
import com.menlopark.rentsyuser.model.Location;
import com.menlopark.rentsyuser.model.ResponseModel;
import com.menlopark.rentsyuser.model.dummy.CategoryContent;
import com.menlopark.rentsyuser.model.favorite_store.AddFavoriteModel;
import com.menlopark.rentsyuser.model.stores.Catalogs.CatalogList;
import com.menlopark.rentsyuser.model.stores.StoreDetails.StoreDetailsResp;
import com.menlopark.rentsyuser.model.stores.StoresDetailsParams;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.view.adapters.CatalogAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


public class ItemPageActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = ItemPageActivity.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match

    public List<StoreDetailsResp> storeDetails;
    public List<com.menlopark.rentsyuser.model.stores.Catalogs.Detail> catalogLists = new ArrayList<>();
    ApiService apiService;
    Dialog dialog;
    Activity act;
    // TODO: Rename and change types of parameters
    private String storeName;
    private String storeId, token;
    private CategoryContent.CategoryItem item;
    private List<Location> add_List = new ArrayList<>();
    private AppCompatImageView detailImage;
    private ImageView btnFavourite;
    private RelativeLayout relOne;
    private TextView detailTitle;
    private TextView detailPrice;
    private RatingBar rating;
    private TextView detailAmenities;
    private RelativeLayout relTow;
    private TextView detailDescTitle;
    private TextView detailDesc;
    private ImageView detailsMap;
    private View view1;
    private RecyclerView list;
    int isfav=0;

    GoogleMap Mmap;
    Marker marker;


    private void assignViews() {
        detailImage = findViewById(R.id.detail_image);
        btnFavourite = findViewById(R.id.btn_favourite);
        relOne = findViewById(R.id.item_recyclerview);
        detailTitle = findViewById(R.id.detail_title);
        detailPrice = findViewById(R.id.detail_price);
        rating = findViewById(R.id.rating);
        detailAmenities = findViewById(R.id.detail_amenities);
        relTow = findViewById(R.id.rel_tow);
        detailDescTitle = findViewById(R.id.detail_desc_title);
        detailDesc = findViewById(R.id.detail_desc);
        detailsMap = findViewById(R.id.details_map);
        view1 = findViewById(R.id.view1);
        list = findViewById(R.id.list);
        dialog = new Dialog(act);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_category_item_page);
        act = ItemPageActivity.this;
        assignViews();
        if (getIntent().hasExtra("storeName") && getIntent().hasExtra("storeId")) {
            storeName = getIntent().getStringExtra("storeName");
            storeId = getIntent().getStringExtra("storeId");

            Log.d("store_val_pass","11storeid-"+storeId+ "name-"+storeName);

        }
        getSupportActionBar().setTitle(storeName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apiService = ApiClient.getClient(PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, "")).create(ApiService.class);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
        list.setLayoutManager(mLayoutManager);
        list.setItemAnimator(new DefaultItemAnimator());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public boolean onNavigateUp() {
        finish();
        return super.onNavigateUp();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onResume() {
        super.onResume();
        call_StoresDetails();
    }


    public void call_AddToFavorite() {

        AddFavoriteModel storesDetailsParams = new AddFavoriteModel();
        storesDetailsParams.setStore_id(storeId);
        if(isfav==1){
            storesDetailsParams.setIs_favourite(0);
        }
        else{
            storesDetailsParams.setIs_favourite(1);
        }

       // storesDetailsParams.setCustomer_id(Config.getPreference(Constants.pref_CustomerId));
        Call<ResponseModel> call = apiService.getFavoriteStatus(storesDetailsParams,"Bearer "+PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, ""));
        call.enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, retrofit2.Response<ResponseModel> response) {
                dialog.hide();
                Log.d("store_parameter","fav--"+response.code());
                //Log.e(TAG, result);

                if (!Config.getPreference(Constants.pref_App_Token).equals("")) {
                    btnFavourite.setVisibility(View.VISIBLE);

                    try {
                     if(response.code()==200){
                         if(response.body().getMessage().equals("Store successfully added to favourites")){
                             btnFavourite.setImageResource(R.drawable.ic_like_selected);
                             showAlert(response.body().getMessage());
                             isfav=1;
                         }
                         else{
                             btnFavourite.setImageResource(R.drawable.ic_like);
                             showAlert(response.body().getMessage());
                             isfav=0;
                         }
                     }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    btnFavourite.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                //showProgress(false);
                dialog.hide();
                Log.d("call_Catalogs","errcalled---"+t.toString());
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


      //  http://3.19.212.234/rentsy/public/api/customer/add-store-to-favourite----for add store to favourite(get-favourite-status.json)

//        ServerCall.getResponse(act, "customer/add-store-to-favourite", jsonObject.toString(), true, new ServerCall.VolleyCallback() {
//            @Override
//            public void onSuccess(String result) {
//                dialog.hide();
//                //Log.e(TAG, result);
//
//                if (!Config.getPreference(Constants.pref_App_Token).equals("")) {
//                    btnFavourite.setVisibility(View.VISIBLE);
//                    JSONObject response = null;
//                    try {
//                        response = new JSONObject(result);
//
//                        if (response.getString("status").equals("SUCCESS")  &&
//                                (response.getString("message").toString().equals("Store Added To Favourites") ||
//                                        response.getString("message").toString().equals("Store is added as favourite"))) {
//                            btnFavourite.setImageResource(R.drawable.ic_like_selected);
//                            showAlert(response.getString("message"));
//                        } else {
//                            btnFavourite.setImageResource(R.drawable.ic_like);
//                            showAlert(response.getString("message"));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    /*if (btnFavourite.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_like_selected).getConstantState()) {
//                        btnFavourite.setImageResource(R.drawable.ic_like);
//                    } else {
//                        btnFavourite.setImageResource(R.drawable.ic_like_selected);
//                    }*/
//                } else {
//                    btnFavourite.setVisibility(View.GONE);
//                }
//
//            }
//
//            @Override
//            public void onError(String error) {
//                dialog.hide();
//                Toast.makeText(act, error + "", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void showAlert(String message) {
        new AlertDialog.Builder(act)
                .setMessage(message)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    public void call_StoresDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("store_id", storeId);
            jsonObject.put("customer_id", Config.getPreference(Constants.pref_CustomerId));
            //51,63,Fail

            Log.d("store_parameter","para-"+storeId+" "+Config.getPreference(Constants.pref_CustomerId));

        } catch (JSONException e) {
            e.printStackTrace();
        }
       // http://3.19.212.234/rentsy/public/api/store-details
        ServerCall.getResponse(act, "store-details", jsonObject.toString(), true, new ServerCall.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                dialog.hide();

                try {
                    StoreDetailsResp storeDetailsResp = new Gson().fromJson(result, StoreDetailsResp.class);
                    JSONObject obj = new JSONObject(result);
                    Log.d("store_parameter","res-"+obj.getString("status"));
                    if (obj.getString("status").equals("SUCCESS")  ) {
                        if (!Config.getPreference(Constants.pref_App_Token).equals("")) {
                            btnFavourite.setVisibility(View.VISIBLE);
                            if (storeDetailsResp.getData().getDetails().getIs_favorite() == 1) {
                                isfav=1;
                                btnFavourite.setImageResource(R.drawable.ic_like_selected);
                            } else {
                                isfav=0;
                                btnFavourite.setImageResource(R.drawable.ic_like);
                            }
                        } else {
                            btnFavourite.setVisibility(View.GONE);
                        }
                        //rating.setRating(storeDetailsResp.getData().getDetails().getRating());
                        detailTitle.setText(storeDetailsResp.getData().getDetails().getBusinessName());
                        detailAmenities.setText(storeDetailsResp.getData().getDetails().getBusinessCategoryName());
                        if(storeDetailsResp.getData().getDetails().getImgName()!=null){
                               if (!storeDetailsResp.getData().getDetails().getImgName().equals("")) {
                            Picasso.with(act).load(storeDetailsResp.getData().getDetails().getImgName()).into(detailImage);
                        }
                        }


                        if (storeDetailsResp.getData().getDetails().getRating() != null) {
                            rating.setRating(storeDetailsResp.getData().getDetails().getRating());
                        }
                        if (storeDetailsResp.getData().getDetails().getDescription() != null) {
                            detailDesc.setText(storeDetailsResp.getData().getDetails().getDescription());

                        }

                        if (storeDetailsResp.getData().getDetails().getOpen() == 0) {
                            detailPrice.setText("Closed");
                            detailPrice.setTextColor(ContextCompat.getColor(act, R.color.coloRed));
                        } else {
                            detailPrice.setTextColor(ContextCompat.getColor(act, R.color.colorGreen));
                            detailPrice.setText("Open Now");
                        }

                        if (storeDetailsResp.getData().getDetails().getLatitude() != null && storeDetailsResp.getData().getDetails().getLongitude() != null && Mmap != null) {
                            LatLng sydney = new LatLng(Double.parseDouble(storeDetailsResp.getData().getDetails().getLatitude()), Double.parseDouble(storeDetailsResp.getData().getDetails().getLongitude()));
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(sydney);
                            Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 6));
                            Mmap.addMarker(markerOptions);

                            Mmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    if (storeDetailsResp.getData().getDetails().getLatitude() != null && storeDetailsResp.getData().getDetails().getLongitude() != null) {
                                        double latitude = Double.parseDouble(storeDetailsResp.getData().getDetails().getLatitude());
                                        double longitude = Double.parseDouble(storeDetailsResp.getData().getDetails().getLongitude());
                                        String label = storeDetailsResp.getData().getDetails().getBusinessName();
                                        String uriBegin = "geo:" + latitude + "," + longitude;
                                        String query = latitude + "," + longitude + "(" + label + ")";
                                        String encodedQuery = Uri.encode(query);
                                        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                                        Uri uri = Uri.parse(uriString);
                                        Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                                        startActivity(mapIntent);
                                    }
                                }
                            });
                        }

                        detailsMap.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (storeDetailsResp.getData().getDetails().getLatitude() != null && storeDetailsResp.getData().getDetails().getLongitude() != null) {
                                    double latitude = Double.parseDouble(storeDetailsResp.getData().getDetails().getLatitude());
                                    double longitude = Double.parseDouble(storeDetailsResp.getData().getDetails().getLongitude());
                                    String label = storeDetailsResp.getData().getDetails().getBusinessName();
                                    String uriBegin = "geo:" + latitude + "," + longitude;
                                    String query = latitude + "," + longitude + "(" + label + ")";
                                    String encodedQuery = Uri.encode(query);
                                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                                    Uri uri = Uri.parse(uriString);
                                    Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                                    startActivity(mapIntent);
                                }
                            }
                        });
                        btnFavourite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                call_AddToFavorite();
                            }
                        });
                        catalogLists.clear();
                        call_Catalogs();
                    } else if (obj.getString("status") .equals("FAIL")) {
                        Toast.makeText(act, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        dialog.dismiss();

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                Log.d("store_parameter","error-"+error);
                dialog.hide();
               // Toast.makeText(act, error + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void call_Catalogs() {
        StoresDetailsParams storesDetailsParams = new StoresDetailsParams();
        storesDetailsParams.setStore_id(storeId);
        Call<CatalogList> call = apiService.callCatalog(storesDetailsParams);

        Log.d("call_Catalogs","called---"+storeId);
        call.enqueue(new retrofit2.Callback<CatalogList>() {
            @Override
            public void onResponse(Call<CatalogList> call, retrofit2.Response<CatalogList> response) {
                Log.d("call_Catalogs","called---"+response.code());
                if (response.code() == 200) {
                    dialog.dismiss();
                   // Log.e("", "onResponse: " + new Gson().toJson(response.body()));

                    if(response.body().getStatus().equals("FAIL")){

                    }
                    else{
                        for (int i = 0; i < response.body().getData().getDetails().size(); i++) {
                            catalogLists.add(response.body().getData().getDetails().get(i));
                        }
                        list.setAdapter(new CatalogAdapter(act, catalogLists));
                    }



//                    for (int i = 0; i < response.body().getDetails().size(); i++) {
//                        catalogLists.add(response.body().getDetails().get(i));
//                    }





                } else {

                    dialog.dismiss();

                    Toast.makeText(act, response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                    //password.setError(getString(R.string.error_incorrect_password));

                }
            }

            @Override
            public void onFailure(Call<CatalogList> call, Throwable t) {
                //showProgress(false);
                Log.d("call_Catalogs","errcalled---"+t.toString());
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap map) {
        Mmap = map;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(false);
        map.setTrafficEnabled(false);
        map.setIndoorEnabled(false);
        map.setBuildingsEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


    }


   /* final CatalogAdapter adapter = new CatalogAdapter(new CatalogAdapter.MyInterface() {
        @Override
        public void someEvent() {
            adapter.notifyDataSetChanged();
        }
    });*/


}
