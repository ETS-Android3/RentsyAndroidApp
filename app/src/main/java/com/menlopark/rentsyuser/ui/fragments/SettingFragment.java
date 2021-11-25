package com.menlopark.rentsyuser.ui.fragments;


import static com.menlopark.rentsyuser.server_access.Constants.Current_Latitude;
import static com.menlopark.rentsyuser.server_access.Constants.Current_Longitude;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.model.Setting.Post_Setting.PostSettingParam;
import com.menlopark.rentsyuser.model.Setting.Post_Setting.PostSettingRes;
import com.menlopark.rentsyuser.model.Setting.SettingParam;
import com.menlopark.rentsyuser.model.Setting.SettingRes;
import com.menlopark.rentsyuser.model.location.LocationModel;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.ui.activitys.LocationActivity;
import com.menlopark.rentsyuser.ui.activitys.LoginActivity;
import com.menlopark.rentsyuser.ui.activitys.MainActivity;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int PLUS_ONE_REQUEST_CODE = 0;

    private String mParam1;
    private String mParam2;


    //private OnFragmentInteractionListener mListener;
//    private ViewDataBinding binding;

    ApiService apiService;
    Dialog loader;
    Button btn_Update;

    EditText details_cost, tv_radious, tv_city;
    Switch notificationSwitch, emailSwitch, invoicesSwitch;
    String sCurrent_address, sRadios, sCurrent_city, notification_Switch = "0", email_Switch = "0", invoice_Switch = "0";

    private TextView detailTitle;
    private View view1;
    private LinearLayout relTwo;
    private TextView detailService;
    private AppCompatEditText detailsCost;
    private View view2;
    private LinearLayout relThree;
    private EditText tvRadious;
    private View view4;
    private LinearLayout relFour;
    private EditText tvCity;
    private View view5;
    private TextView detailTitle2;
    private View view6;
    private RelativeLayout relFive;

    private View view7;
    private RelativeLayout relSix;

    private View view8;
    private RelativeLayout relSeven;

    private View view9;
    private LinearLayout llBookingOptionTow;

    private Button btnUpdate, btnLogout;
    LocationModel citydetails;


    private void assignViews(View view) {
        detailTitle = (TextView) view.findViewById(R.id.detail_title);
        view1 = view.findViewById(R.id.view1);
        relTwo = (LinearLayout) view.findViewById(R.id.rel_two);
        detailService = (TextView) view.findViewById(R.id.detail_service);
        detailsCost = (AppCompatEditText) view.findViewById(R.id.details_cost);
        view2 = view.findViewById(R.id.view2);
        relThree = (LinearLayout) view.findViewById(R.id.rel_three);
        tvRadious = (EditText) view.findViewById(R.id.tv_radious);
        view4 = view.findViewById(R.id.view4);
        relFour = (LinearLayout) view.findViewById(R.id.rel_four);
        tvCity = (EditText) view.findViewById(R.id.tv_city);
        view5 = view.findViewById(R.id.view5);
        detailTitle2 = (TextView) view.findViewById(R.id.detail_title2);
        view6 = view.findViewById(R.id.view6);
        relFive = (RelativeLayout) view.findViewById(R.id.rel_five);
        notificationSwitch = (Switch) view.findViewById(R.id.notificationSwitch);
        view7 = view.findViewById(R.id.view7);
        relSix = (RelativeLayout) view.findViewById(R.id.rel_six);
        emailSwitch = (Switch) view.findViewById(R.id.emailSwitch);
        view8 = view.findViewById(R.id.view8);
        relSeven = (RelativeLayout) view.findViewById(R.id.rel_seven);
        invoicesSwitch = (Switch) view.findViewById(R.id.invoicesSwitch);
        view9 = view.findViewById(R.id.view9);
        llBookingOptionTow = (LinearLayout) view.findViewById(R.id.ll_booking_option_tow);
        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        btnUpdate = (Button) view.findViewById(R.id.btn_Update);
        Log.e("assignViews: ", PreferenceUtil.getPref(getActivity()).getString(Constants.pref_CityDetails, "") + "");
        citydetails = new Gson().fromJson(PreferenceUtil.getPref(getActivity()).getString(Constants.pref_CityDetails, ""), LocationModel.class);
    }


    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        apiService = ApiClient.getClient(PreferenceUtil.getPref(getActivity()).getString(Constants.pref_App_Token, "")).create(ApiService.class);

        assignViews(view);
        if (PreferenceUtil.getPref(getActivity()).getString(Constants.pref_App_Token, "").equals("")) {
            btnLogout.setText(R.string.login);
        } else {
            btnLogout.setText(R.string.logout);
        }

        get_settings_data();
        btn_Update = view.findViewById(R.id.btn_Update);

        details_cost = view.findViewById(R.id.details_cost);
        tv_radious = view.findViewById(R.id.tv_radious);
        tv_city = view.findViewById(R.id.tv_city);

        notificationSwitch = view.findViewById(R.id.notificationSwitch);
        emailSwitch = view.findViewById(R.id.emailSwitch);
        invoicesSwitch = view.findViewById(R.id.invoicesSwitch);

        Location targetLocation = new Location("");//provider name is unnecessary
        targetLocation.setLatitude(Current_Latitude);//your coords of course
        targetLocation.setLongitude(Current_Longitude);

        if (Current_Latitude == 0.0 && Current_Longitude == 0.0) {
            details_cost.setText("Location not enabled");
        } else {
            getCityName(targetLocation, new OnGeocoderFinishedListener() {
                @Override
                public void onFinished(List<Address> results) {
                    details_cost.setText(results.get(0).getLocality());
                }
            });
        }


        relThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRadius();
            }
        });

        relFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCityPreference();
            }
        });



            notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(buttonView.isPressed()){
                        if (isChecked) {
                            notification_Switch = "1";
                            updateData();
                        } else {
                            notification_Switch = "0";
                            updateData();
                        }
                    }

                }
            });





            emailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(buttonView.isPressed()){
                        if (isChecked) {
                            email_Switch = "1";
                            updateData();
                        } else {
                            email_Switch = "0";
                            updateData();
                        }
                    }

                }
            });






            invoicesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(buttonView.isPressed()){
                        if (isChecked) {
                            invoice_Switch = "1";
                            updateData();
                        } else {
                            invoice_Switch = "0";
                            updateData();
                        }
                    }


                }
            });




        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PreferenceUtil.getPref(getActivity()).getString(Constants.pref_App_Token, "").equals("")) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(i);
                    getActivity().finish();
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setIcon(R.drawable.app_icon)
                            .setTitle("Logout")
                            .setMessage("Are you sure you want to Logout?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LoginManager.getInstance().logOut();
                                    PreferenceUtil.getPref(getActivity()).edit()
                                            .putString(Constants.pref_App_Token, "")
                                            .apply();
                                    PreferenceUtil.getPref(getActivity()).edit()
                                            .putString(Constants.pref_CustomerId, "")
                                            .apply();

                                    Config.setPreference(Constants.pref_App_Token, "");
                                    Config.setPreference(Constants.pref_CustomerId, "");
                                    Config.setPreference(Constants.pref_CustomerEmail, "");


                                    Constants.APP_TOKEN = "";
                                    Intent i = new Intent(getActivity(), LoginActivity.class);
                                    getActivity().startActivity(i);
                                    getActivity().finish();
                                }

                            })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();

                    Button bn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    Button bp = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    bn.setTextColor(Color.BLACK);
                    bp.setTextColor(Color.BLACK);
                }


            }
        });

        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.action_settings));
        return view;
    }

    public void updateData() {
        sCurrent_address = details_cost.getText().toString();
        sRadios = tv_radious.getText().toString();
        sCurrent_city = tv_city.getText().toString();
        post_settings_data(sCurrent_address, sRadios, sCurrent_city, notification_Switch, email_Switch, invoice_Switch);
    }

    private void dialogRadius() {
        ListView listView;
        String[] s = {"50km", "100km", "200km", "500km", "1000km"};
        final android.app.Dialog dialog = new android.app.Dialog(getActivity());
        dialog.setContentView(R.layout.list_location);
        dialog.setTitle("Select Radius");
        listView = dialog.findViewById(R.id.list_location);


        dialog.show();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, s) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                tv_radious.setText((String) listView.getItemAtPosition(position).toString().trim());
                dialog.cancel();
                updateData();

            }

        });
        adapter.notifyDataSetChanged();
    }

    private void dialogCityPreference() {
        ListView listView;
//        String[] s = new String[citydetails.getData().getDetails().size()];
//        for (int i = 0; i < citydetails.getData().getDetails().size(); i++) {
//            s[i] = citydetails.getData().getDetails().get(i).getName();
//        }

        String[] s = new String[citydetails.getData().size()];
        for (int i = 0; i < citydetails.getData().size(); i++) {
            s[i] = citydetails.getData().get(i).getName();
        }


        final android.app.Dialog dialog = new android.app.Dialog(getActivity());
        dialog.setContentView(R.layout.list_location);
        dialog.setTitle("Select City");
        listView = dialog.findViewById(R.id.list_location);


        dialog.show();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, s) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                tv_city.setText((String) listView.getItemAtPosition(position).toString().trim());
                dialog.cancel();
                updateData();

            }

        });
        adapter.notifyDataSetChanged();
    }

    public void getCityName(final Location location, OnGeocoderFinishedListener listener) {
        new AsyncTask<Void, Integer, List<Address>>() {
            @Override
            protected List<Address> doInBackground(Void... arg0) {
                Geocoder coder = new Geocoder(getContext(), Locale.ENGLISH);
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

    public abstract class OnGeocoderFinishedListener {
        public abstract void onFinished(List<Address> results);
    }

    private void post_settings_data(String sCurrent_address, String sRadios, String sCurrent_city,
                                    String notification_Switch, String email_Switch, String invoice_Switch) {

        PostSettingParam postSettingParam = new PostSettingParam();
        postSettingParam.setCustomer_id(Prefs.getString(Constants.pref_CustomerId, ""));
        postSettingParam.setLocation_id("2");
        postSettingParam.setRadious(sRadios);
        postSettingParam.setCurrent_location(sCurrent_address);
        postSettingParam.setPush_notification_status(notification_Switch);
        postSettingParam.setEmail_alerts_status(email_Switch);
        postSettingParam.setEmail_invoice_status(invoice_Switch);




        Log.d("putsetting", postSettingParam.toString());
        loader = new Dialog(getActivity());
        loader.show();

        Call<PostSettingRes> call = apiService.call_post_setting(postSettingParam);

        call.enqueue(new Callback<PostSettingRes>() {
            @Override
            public void onResponse(Call<PostSettingRes> call, Response<PostSettingRes> response) {
                Log.d("putsetting", "res-"+response.code());



                if (response.code() == 200) {

                    String sMsg = response.body().getMessage();
                    loader.dismiss();

                    Toast.makeText(getActivity(), sMsg, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), LocationActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                } else {
                    loader.dismiss();
                }
            }

            @Override
            public void onFailure(Call<PostSettingRes> call, Throwable t) {
                Log.d("putsetting", "error-"+t.getMessage());
                loader.dismiss();

            }
        });
    }

    private void get_settings_data() {


        SettingParam settingParam = new SettingParam();
        settingParam.setCustomer_id(Prefs.getString(Constants.pref_CustomerId, ""));
        //settingParam.setCustomer_id("1");
        Log.d("getsetting","para-"+Prefs.getString(Constants.pref_CustomerId, ""));

        loader = new Dialog(getActivity());
        loader.show();
        Call<SettingRes> call = apiService.call_get_Setting(settingParam);

        call.enqueue(new Callback<SettingRes>() {
            @Override
            public void onResponse(Call<SettingRes> call, Response<SettingRes> response) {

                Log.d("getsetting","res-"+response.code());


                if (response.code() == 200) {
                    loader.dismiss();

                    if(!response.body().getStatus().equals("FAIL")){
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        details_cost.setText(response.body().getData().getDetails().getCurrentLocation());
                        tv_radious.setText(response.body().getData().getDetails().getRadious().toString() + "km");
                        tv_city.setText(response.body().getData().getDetails().getCurrentLocation());

                        String Spush_notification_status = String.valueOf(response.body().getData().getDetails().getPushNotificationStatus());
                        String sEmail_alerts_status = String.valueOf(response.body().getData().getDetails().getEmailAlertsStatus());
                        String sEmail_invoice_status = String.valueOf(response.body().getData().getDetails().getEmailInvoiceStatus());
                        if (Spush_notification_status.equals("1")) {
                            notificationSwitch.setChecked(true);
                            notification_Switch = "1";
                        } else {
                            notificationSwitch.setChecked(false);
                            notification_Switch = "0";
                        }

                        if (sEmail_alerts_status.equals("1")) {
                            emailSwitch.setChecked(true);
                            email_Switch = "1";
                        } else {
                            emailSwitch.setChecked(false);
                            email_Switch = "0";
                        }

                        if (sEmail_invoice_status.equals("1")) {
                            invoicesSwitch.setChecked(true);
                            invoice_Switch = "1";
                        } else {
                            invoicesSwitch.setChecked(false);
                            invoice_Switch = "0";
                        }
                    }




                } else {
                    loader.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SettingRes> call, Throwable t) {
                Log.d("getsetting","error-"+t.getMessage());
                loader.dismiss();

            }
        });
    }

    public void commanFragmentCall(Fragment fragment) {

        Fragment cFragment = fragment;
        if (cFragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack("Triocab", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment, cFragment);
            fragmentTransaction.isAddToBackStackAllowed();
            fragmentTransaction.commitNow();
        }
    }

}
