package com.menlopark.rentsyuser.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.core.view.MenuItemCompat;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.model.Catalogueitem.CatalogueItemRes;
import com.menlopark.rentsyuser.model.Catalogueitem.Catalogue_item_Params;
import com.menlopark.rentsyuser.model.add_to_cart.AddToCartParams;
import com.menlopark.rentsyuser.model.add_to_cart.AddToCartRes;
import com.menlopark.rentsyuser.model.dummy.CategoryContent;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.ui.activitys.CartActivity;
import com.menlopark.rentsyuser.ui.activitys.LoginActivity;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;

public class DetailsFragment extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private static final String TAG = DetailsFragment.class.getSimpleName();
    String sItem_id;
    ApiService apiService;
    Dialog loader;
    ListView listView;
    String sDate, sendDateFormat, sTimes, sendTimeFormat, sQuantity, sLength_of_booking;
    Button btn_quantity, btn_date, btn_time, btn_length_of_booking;
    String sBookingPeriod;
    String sBookingAmount;
    String sCartId;
    Activity act;
    TextView textCartItemCount;
    int mCartItemCount = 0;
    // TODO: Rename and change types of parameters
    private String item_id, item_name;
    private String mParam2;
    private CategoryContent.CategoryItem item;
    private DatePickerDialog brithDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SliderLayout detailImage;
    private RelativeLayout relOne;
    private TextView detailTitle;
    private TextView detailPrice;
    private RatingBar rating;
    private TextView detailCategory;
    private TextView detailAmenities;
    private RelativeLayout relTow;
    private TextView detailDescTitle;
    private TextView detailDesc;
    private ImageView detailsMap;
    private View view1;
    private TextView detailsBooknow;
    private View view2;
    private LinearLayout llBookingOption;
    private Button btnQuantity;
    private Button btnDate;
    private LinearLayout llBookingOptionTow;
    private Button btnTime;
    private Button btnLengthOfBooking;
    private Button btn_bookNow;

    public DetailsFragment() {
        // Required empty public constructor
    }

    private void assignViews() {
        detailImage = findViewById(R.id.detail_image);
        relOne = findViewById(R.id.item_recyclerview);
        detailTitle = findViewById(R.id.detail_title);
        detailPrice = findViewById(R.id.detail_price);
        rating = findViewById(R.id.rating);
        detailCategory = findViewById(R.id.detail_category);
        detailAmenities = findViewById(R.id.detail_amenities);
        relTow = findViewById(R.id.rel_tow);
        detailDescTitle = findViewById(R.id.detail_desc_title);
        detailDesc = findViewById(R.id.detail_desc);
        detailsMap = findViewById(R.id.details_map);
        view1 = findViewById(R.id.view1);
        detailsBooknow = findViewById(R.id.details_booknow);
        view2 = findViewById(R.id.view2);
        llBookingOption = findViewById(R.id.ll_booking_option);
        btnQuantity = findViewById(R.id.btn_quantity);
        btnDate = findViewById(R.id.btn_date);
        llBookingOptionTow = findViewById(R.id.ll_booking_option_tow);
        btnTime = findViewById(R.id.btn_time);
        btnLengthOfBooking = findViewById(R.id.btn_length_of_booking);
        btn_bookNow = findViewById(R.id.btn_bookNow);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_details);
        act = DetailsFragment.this;
        assignViews();
        if (getIntent().hasExtra("item_id")) {
            item_id = getIntent().getStringExtra("item_id");
        }
        if (getIntent().hasExtra("item_name")) {
            item_name = getIntent().getStringExtra("item_name");
        }
        getSupportActionBar().setTitle(item_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init_a();
        btn_bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ParseUtils.CommonDialog(getContext(), getResources().getString(R.string.congratulations), getResources().getString(R.string.continues), "cart");


                if (validateFields() && isLoggedIn()) {
                    Constants.APP_TOKEN = PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, "");
                    call_Add_To_Cart();
                }


            }
        });

        btnQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn())
                    dialog_quantity();


            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()) {
                    setDateTimeField();
                    brithDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    brithDatePickerDialog.show();

                }

            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()) {
                    dialo_time();

                }

            }
        });

        btnLengthOfBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()) {
                    dialog_length_of_booking();

                }

            }
        });
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

    private void call_Add_To_Cart() {
        String dateandTime = convertToGmt(sendDateFormat + " " + sendTimeFormat);
        AddToCartParams addToCartParams = new AddToCartParams();
        addToCartParams.setCustomer_id(Prefs.getString(Constants.pref_CustomerId, ""));
        addToCartParams.setCatalogue_id(item_id);
        addToCartParams.setQty(sQuantity);
        addToCartParams.setDate(dateandTime.split(" ")[0]);
        addToCartParams.setTime(dateandTime.split(" ")[1]);
        addToCartParams.setLength(sLength_of_booking.replaceAll("[^0-9]", ""));
        addToCartParams.setBooking_period(sBookingPeriod);
        addToCartParams.setAmount(sBookingAmount);
        Log.d("add2cart","para--"+Prefs.getString(Constants.pref_CustomerId,"")+" "+item_id+" "+sQuantity+
                " "+dateandTime.split(" ")[0]+" "+dateandTime.split(" ")[1]+" "+sLength_of_booking.replaceAll("[^0-9]", "")+
                " "+sBookingPeriod+" "+sBookingAmount);
        loader = new Dialog(act);
        loader.show();

        Call<AddToCartRes> call = apiService.callAddTocart(addToCartParams);
        Log.i(TAG, "CALL URL --> " + call.request().url().toString());
        call.enqueue(new retrofit2.Callback<AddToCartRes>() {
            @Override
            public void onResponse(Call<AddToCartRes> call, retrofit2.Response<AddToCartRes> response) {
                Log.d("add2cart","res--"+response.code());
                if (response.code() == 200) {
                    loader.dismiss();

                    PreferenceUtil.getPref(act).edit().putString(Constants.pref_Cart_Response, new Gson().toJson(response.body())).apply();
                    startActivity(new Intent(act, CartActivity.class));
                } else {
                    loader.dismiss();
                    Toast.makeText(act, response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartRes> call, Throwable t) {
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
                loader.dismiss();
            }
        });


    }

    private void Booking_Dailog() {
        final android.app.Dialog dialog = new android.app.Dialog(act);
        dialog.setContentView(R.layout.dailog_delete);
        TextView txt_title = dialog.findViewById(R.id.txt_title);
        TextView txt_done = dialog.findViewById(R.id.txt_done);
        TextView txt_details_invite = dialog.findViewById(R.id.txt_details_invite);
        TextView txt_details = dialog.findViewById(R.id.txt_details);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        txt_title.setText(R.string.bookin_now);
        txt_done.setText(R.string.continues);
        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                /*act.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, new CategoryFragment()).commit();*/


            }
        });

        dialog.show();
    }


    private void init_a() {
        apiService = ApiClient.getClient(Config.getPreference(Constants.key_device_token)).create(ApiService.class);
        dateFormatter = new SimpleDateFormat("dd/mm/yyyy");

    }


    private void setDateTimeField() {
        Calendar newCalender = Calendar.getInstance();
        brithDatePickerDialog = new DatePickerDialog(act, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthofyera, int dayofmonth) {
                sDate = String.format("%02d/%02d", dayofmonth, (monthofyera + 1)) + "/" + year;
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthofyera, dayofmonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                sendDateFormat = format.format(calendar.getTime());
                btnDate.setText(sDate);

            }
        }, newCalender.get(Calendar.YEAR),
                newCalender.get(Calendar.MONTH),
                newCalender.get(Calendar.DAY_OF_MONTH));
    }


    private String convertToGmt(String date_time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = dateFormat.parse(date_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        return dateFormatGmt.format(date) + "";

    }


    private void dialo_time() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(act, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Calendar datetime = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                datetime.set(Calendar.MINUTE, minute);
                if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                    sTimes = String.format("%02d:%02d", selectedHour, selectedMinute) + ":00";
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                    sendTimeFormat = String.format("%02d:%02d", selectedHour, selectedMinute);
                    btnTime.setText(sTimes);
                } else {
                    //it's before current'
                    Toast.makeText(getApplicationContext(), "Invalid Time", Toast.LENGTH_LONG).show();
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    private void dialog_length_of_booking() {

        String timeKey = (sBookingPeriod.equals("0")) ? "Day" : "Hour";

        String[] s = {"1 " + timeKey, "2 " + timeKey + "s", "3 " + timeKey + "s", "4 " + timeKey + "s", "5 " + timeKey + "s", "6 " + timeKey + "s",
                "7 " + timeKey + "s", "8 " + timeKey + "s", "9 " + timeKey + "s", "10 " + timeKey + "s"};
        final android.app.Dialog dialog = new android.app.Dialog(act);
        dialog.setContentView(R.layout.list_location);
        dialog.setTitle("Select Length");
        listView = dialog.findViewById(R.id.list_location);

        dialog.show();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,
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
                int itemPosition = position;
                sLength_of_booking = (String) listView.getItemAtPosition(position);

                btnLengthOfBooking.setText(sLength_of_booking);
                dialog.cancel();


            }

        });
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        updateCartCount();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void updateCartCount() {
        try {
            AddToCartRes addToCartRes = new Gson().fromJson(PreferenceUtil.getPref(act).getString(Constants.pref_Cart_Response, ""), AddToCartRes.class);
            mCartItemCount = addToCartRes.getData().getCartInfo().size();
            setupBadge();
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Intent i = new Intent(act, CartActivity.class);
                startActivity(i);

                break;

        }
        return super.onOptionsItemSelected(item);

    }

    private void dialog_quantity() {


        String[] s = {"1 ", "2", "3 ", "4", "5 ", "6",
                "7 ", "8", "9 ", "10"};
        final android.app.Dialog dialog = new android.app.Dialog(act);
        dialog.setContentView(R.layout.list_location);
        dialog.setTitle("Select Quantity");
        listView = dialog.findViewById(R.id.list_location);


        dialog.show();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                sQuantity = (String) listView.getItemAtPosition(position);

                btnQuantity.setText(sQuantity);
                dialog.cancel();


            }

        });
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
        call_get_catalogue_item();
    }


    @Override
    public void onStop() {
        detailImage.stopAutoCycle();
        super.onStop();
    }

    public void call_get_catalogue_item() {
        Catalogue_item_Params catalogue_item_params = new Catalogue_item_Params();
        catalogue_item_params.setItem_id(item_id);
        loader = new Dialog(act);
        loader.show();

        Call<CatalogueItemRes> call = apiService.call_get_catalogue_item(catalogue_item_params);
        call.enqueue(new retrofit2.Callback<CatalogueItemRes>() {
            @Override
            public void onResponse(Call<CatalogueItemRes> call, final retrofit2.Response<CatalogueItemRes> response) {
                if (response.code() == 200) {
                    loader.dismiss();
                    Log.e("", "onResponse: " + new Gson().toJson(response.body()));
                    ArrayList<String> url_maps = new ArrayList<>();
                    if(response.body().getData().getDetails().getPhoto1()!=null){
                        Log.d("called500","slidercall1--"+response.body().getData().getDetails().getPhoto1());
                        url_maps.add(response.body().getData().getDetails().getPhoto1());
                    }
                    if(response.body().getData().getDetails().getPhoto2()!=null){
                        Log.d("called500","slidercall2--"+response.body().getData().getDetails().getPhoto2());

                        url_maps.add(response.body().getData().getDetails().getPhoto2());
                    }
                    if(response.body().getData().getDetails().getPhoto3()!=null){
                        Log.d("called500","slidercall3--"+response.body().getData().getDetails().getPhoto3());
                        url_maps.add(response.body().getData().getDetails().getPhoto3());
                    }
                    if(response.body().getData().getDetails().getPhoto4()!=null){
                        Log.d("called500","slidercall4--"+response.body().getData().getDetails().getPhoto4());
                        url_maps.add(response.body().getData().getDetails().getPhoto4());
                    }
                    if(response.body().getData().getDetails().getPhoto5()!=null){
                        Log.d("called500","slidercall5--"+response.body().getData().getDetails().getPhoto5());
                        url_maps.add(response.body().getData().getDetails().getPhoto5());
                    }


//                    for (int i = 0; i < response.body().getData().getDetails().get(0).getImages().size(); i++) {
//
//                        url_maps.add(response.body().getData().getDetails().get(0).getImages().get(i));
//                    }

                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("called500","slidercall--"+url_maps.size());
                            for (String name : url_maps) {

                                TextSliderView textSliderView = new TextSliderView(act);
                                // initialize a SliderLayout
                                textSliderView
                                        .description(response.body().getData().getDetails().getItemname())
                                        .image(name)
                                        .setScaleType(BaseSliderView.ScaleType.Fit);
                                //add your extra information

                                detailImage.addSlider(textSliderView);
                            }
                            detailImage.setPresetTransformer(SliderLayout.Transformer.Accordion);
                            detailImage.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            detailImage.setCustomAnimation(new DescriptionAnimation());
                            detailImage.setDuration(4000);
                        }
                    },200);



                    detailTitle.setText(response.body().getData().getDetails().getItemname());
                    //detailPrice.setText(response.body().getData().getDetails().get(0).getBookingrate().toString());
                    detailCategory.setText(response.body().getData().getDetails().getCategoryname());
                    detailDesc.setText(HtmlCompat.fromHtml(response.body().getData().getDetails().getDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                    try {
                        rating.setRating(response.body().getData().getDetails().getRating().floatValue());

                    } catch (Exception e) {
                    }
                    detailAmenities.setText(response.body().getData().getDetails().getStoreaddress());
                    //detailDescTitle.setText(response.body().getData().getDetails().get(0).get);


                    sBookingAmount = response.body().getData().getDetails().getBookingrate().toString();
                    if (response.body().getData().getDetails().getMinbookingperiod().equals("0")) {
                        detailPrice.setText("$" + sBookingAmount + "/" + "day");
                        sBookingPeriod = "0";
                    } else {
                        detailPrice.setText("$" + sBookingAmount + "/" + "hour");
                        sBookingPeriod = "1";
                    }


                    detailsMap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (response.body().getData().getDetails().getLatitude() != null && response.body().getData().getDetails().getLongitude() != null) {
                                double latitude = Double.parseDouble(response.body().getData().getDetails().getLatitude());
                                double longitude = Double.parseDouble(response.body().getData().getDetails().getLongitude());
                                String label = response.body().getData().getDetails().getCategoryname();
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
                    updateCartCount();

                    //  Toast.makeText(act, response.body().getData().getDetails().get(0).getItemname(), Toast.LENGTH_SHORT).show();

                } else {
                    loader.dismiss();
                    Toast.makeText(act, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CatalogueItemRes> call, Throwable t) {
                //showProgress(false);
                Log.d("called500","error--"+t.getMessage());
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
                loader.dismiss();
            }
        });

    }


    private boolean validateFields() {
        if (btnQuantity.getText().equals("Quantity")) {
            Toast.makeText(act, "Select Quantity", Toast.LENGTH_SHORT).show();
            return false;
        } else if (btnDate.getText().equals("Date")) {
            Toast.makeText(act, "Select Date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (btnTime.getText().equals("Time")) {
            Toast.makeText(act, "Select Time", Toast.LENGTH_SHORT).show();
            return false;
        } else if (btnLengthOfBooking.getText().equals("Length of Booking")) {
            Toast.makeText(act, "Select Length of Booking", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    public boolean isLoggedIn() {
        if (PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, "").equals("")) {
            AlertDialog dialog = new AlertDialog.Builder(act)
                    .setIcon(R.drawable.app_icon)
                    .setTitle("Login/Sign Up")
                    .setMessage("Please sign in or create an account to continue.")
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Config.removePreference(Constants.pref_App_Token);
                            PreferenceUtil.getPref(act).edit()
                                    .putString(Constants.pref_App_Token, "")
                                    .commit();
                            Constants.APP_TOKEN = "";
                            Intent i = new Intent(act, LoginActivity.class);
                            act.startActivity(i);
                            act.finish();
                        }

                    })
                    .show();

            Button bn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            Button bp = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            bn.setTextColor(Color.BLACK);
            bp.setTextColor(Color.BLACK);
            return false;
        } else {
            return true;
        }

    }
}
