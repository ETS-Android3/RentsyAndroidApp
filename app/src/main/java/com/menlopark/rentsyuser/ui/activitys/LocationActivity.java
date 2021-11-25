package com.menlopark.rentsyuser.ui.activitys;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.menlopark.rentsyuser.server_access.Constants.Current_Latitude;
import static com.menlopark.rentsyuser.server_access.Constants.Current_Longitude;
import static com.menlopark.rentsyuser.server_access.Constants.pref_Current_Store;
import static com.menlopark.rentsyuser.server_access.Constants.pref_Current_Store_id;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.databinding.ActivityLocationBinding;
import com.menlopark.rentsyuser.model.Location;
import com.menlopark.rentsyuser.model.Profile.ProfileParam;
import com.menlopark.rentsyuser.model.Profile.ProfileRes;
import com.menlopark.rentsyuser.model.ResponseLocation;
import com.menlopark.rentsyuser.model.location.Detail;
import com.menlopark.rentsyuser.model.location.LocationModel;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.util.GoogleClientApiHelper;
import com.menlopark.rentsyuser.util.LocationService;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.view.adapters.LocationAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends AppCompatActivity {

    public List<Detail> citydetails;
    Dialog dialog;
    ActivityLocationBinding binding;
    Activity act;
    ApiService apiService;
    TextView loginMenu;
    View layoutohno;
    GoogleClientApiHelper googleClientApiHelper;
    LocationService locationService;
    Location loc;
    Dialog loader;
    ArrayList<String> arrayList = new ArrayList<>();
    String sData;
    FloatingActionButton floatingActionButton;
    private List<Location> add_List = new ArrayList<>();
    private List<Location> city_List = new ArrayList<>();
    private LocationAdapter mAdapter, adapter;
    private RecyclerView addressList;
    private boolean mSaleIndecator = false;

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        return super.onSupportNavigateUp();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location);
        getSupportActionBar().setCustomView(R.layout.location_custom_layout);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        init();

        floatingActionButton = findViewById(R.id.flotSelection);


//        String json = loadJSONFromAsset(LocationActivity.this);
//        try {
//            JSONObject obj = new JSONObject(json);
//            JSONArray jsonArray = obj.getJSONArray("locations");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject object = jsonArray.getJSONObject(i);
//                dynamicData(object);
//            }
//
//            mAdapter = new LocationAdapter(citydetails, R.layout.list_item_locations, act, "locations") {
//                @Override
//                protected int getLayoutIdForPosition(int position) {
//                    return citydetails.size();
//                }
//            };
//
//        } catch (Throwable t) {
//
//        }
        binding.mycurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.settingsList.setVisibility(GONE);
                binding.mycurrentLocation.setVisibility(GONE);
                mSaleIndecator = true;
                binding.mycurrentLocation.setVisibility(GONE);

                getSaleVisibility();

            }
        });


        binding.layoutohno.flotSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LocationActivity.this, MainActivity.class);
                i.putExtra(getString(R.string.INTENT_LOCATION), Config.SELECTED_LOCATION);

            }
        });
    }

    public void getCityName(final android.location.Location location, OnGeocoderFinishedListener listener) {
        new AsyncTask<Void, Integer, List<Address>>() {
            @Override
            protected List<Address> doInBackground(Void... arg0) {
                Geocoder coder = new Geocoder(act, Locale.ENGLISH);
                List<Address> results = null;
                try {
                    results = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    // nothing
                }
                return results;
            }

            @Override
            protected void onPostExecute(List<Address> results) {
                if (results != null && listener != null) {
                    listener.onFinished(results);
                }
            }
        }.execute();
    }

    private void init() {
        act = LocationActivity.this;
        dialog = new Dialog(act);
        googleClientApiHelper = new GoogleClientApiHelper(act);
        apiService = ApiClient.getClient(PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, "")).create(ApiService.class);
        citydetails = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.settingsList.setLayoutManager(mLayoutManager);
        binding.settingsList.setItemAnimator(new DefaultItemAnimator());
        loginMenu = getSupportActionBar().getCustomView().findViewById(R.id.login);

        if (PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, "").equals("")) {
            loginMenu.setVisibility(View.VISIBLE);
        } else {
            loginMenu.setVisibility(View.GONE);
        }

                  // loginMenu.setVisibility(View.VISIBLE);

        startService(new Intent(act,LocationService.class));
    }


    public abstract class OnGeocoderFinishedListener {
        public abstract void onFinished(List<Address> results);
    }
    public int getSaleVisibility() {
        adapter = new LocationAdapter(citydetails, R.layout.list_item_locations, this, "city") {
            @Override
            protected int getLayoutIdForPosition(final int position) {

                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("onCreateView: ", new JSONArray(adapter.getArrayList()) + "");
                        arrayList = new ArrayList<>(adapter.getArrayList());

                        for (int i = 0; i < arrayList.size(); i++) {
                            sData = arrayList.get(0);
                        }
                        if (sData != null) {

                            String[] arrSplit = sData.split(",");
                            String location_id = arrSplit[0];
                            String location_name = arrSplit[1];
                            Toast.makeText(act, location_id + "\n" + location_name, Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(LocationActivity.this, MainActivity.class);
                            i.putExtra("location", location_name);
                            i.putExtra("location_id", location_id);

                            startActivity(i);
                        }


                    }
                });
                return citydetails.size();
            }
        };
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.layoutohno.cityList.setLayoutManager(mLayoutManager);
        binding.layoutohno.cityList.setItemAnimator(new DefaultItemAnimator());


        android.location.Location targetLocation = new android.location.Location("");//provider name is unnecessary
        targetLocation.setLatitude(Current_Latitude);//your coords of course
        targetLocation.setLongitude(Current_Longitude);
        if(targetLocation.getLatitude()!=0.0 && targetLocation.getLongitude()!=0.0){
            getCityName(targetLocation, new OnGeocoderFinishedListener() {
                @Override
                public void onFinished(List<Address> results) {
                   // results.get(0).setLocality("Gold coast");
                    String addressString=results.get(0).getAddressLine(0).toString()+" "+results.get(0).getLocality()+" "+results.get(0).getSubAdminArea()+" "+results.get(0).getThoroughfare()+" "+results.get(0).getCountryName();
                    for(int i=0;i<citydetails.size();i++){
                        if(addressString.contains(citydetails.get(i).getName())){
                            String location_id =citydetails.get(i).getId()+"";
                            String location_name = citydetails.get(i).getName();
                            Intent ii = new Intent(LocationActivity.this, MainActivity.class);
                            ii.putExtra("location", location_name);
                            ii.putExtra("location_id", location_id);
                            Config.setPreference(pref_Current_Store, location_name);
                            Config.setPreference(pref_Current_Store_id, location_id);
                            startActivity(ii);
                            break;
                        }

                    }
                    binding.layoutohno.cityList.setAdapter(adapter);
                }
            });

        } else {
            binding.layoutohno.cityList.setAdapter(adapter);
        }


        return mSaleIndecator ? VISIBLE : GONE;
    }


    public void clickEvent(View v) {
        if (v.getId() == R.id.login) {
            Intent i = new Intent(LocationActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }

    private void dynamicData(JSONObject jsonArray) {
        ResponseLocation resp = new ResponseLocation();
        add_List.add((Location) resp.getLocations());
        resp.setLocations(add_List);
        Log.e("Test", String.valueOf(resp.getLocations()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        citydetails.clear();
        call_Location(citydetails);

        //pROFILE DATA aPI
       //get_profile();
    }

    public void call_Location(final List<Detail> citydetails) {
        if (dialog != null && !dialog.isShowing() && !isFinishing())
            dialog.show();
        Call<LocationModel> call = apiService.callLocation();
        call.enqueue(new retrofit2.Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, retrofit2.Response<LocationModel> response) {
                Log.d("onResponse: ", "location---"+ response.code()+"");
                if (response.code()== 200) {
                    if (dialog != null && dialog.isShowing() && !isFinishing())
                        dialog.dismiss();
                    citydetails.addAll(response.body().getData());
                    String successResponse = new Gson().toJson(response.body());
                    Log.e("onResponse: ",  successResponse+"");
                    PreferenceUtil.getPref(act).edit()
                            .putString(Constants.pref_CityDetails, new Gson().toJson(response.body()))
                            .apply();

                    mAdapter = new LocationAdapter(response.body().getData(), R.layout.list_item_locations, act, "locations") {
                        @Override
                        protected int getLayoutIdForPosition(int position) {
                            return citydetails.size();
                        }
                    };

                    binding.settingsList.setAdapter(mAdapter);
                } else {
                    if (dialog != null && dialog.isShowing() && !isFinishing())
                        dialog.dismiss();
                    Toast.makeText(LocationActivity.this, response + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                Log.d("onResponse: ", "locationerr---"+ t.getMessage()+"");
                if (dialog != null && dialog.isShowing() && !isFinishing())
                    dialog.dismiss();
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void get_profile() {

        ProfileParam profileParam = new ProfileParam();

        profileParam.setCustomer_id("4");
        loader = new Dialog(LocationActivity.this);
        loader.show();


        Call<ProfileRes> call = apiService.call_Profile("Bearer "+PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, ""));

        call.enqueue(new Callback<ProfileRes>() {
            @Override
            public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {


             Log.d("profileres","res--"+response.code());

                if (response.code() == 200) {
                    loader.dismiss();


                } else {
                    loader.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ProfileRes> call, Throwable t) {
                loader.dismiss();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(LocationActivity.this, LocationActivity.class);
        startActivity(intent);
        finish();*/
    }
}
