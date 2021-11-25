package com.menlopark.rentsyuser.ui.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.databinding.FragmentCalenderBinding;
import com.menlopark.rentsyuser.model.Calendar_Data.CalendarDataRes;
import com.menlopark.rentsyuser.model.Calendar_Data.CalenderDataParams;
import com.menlopark.rentsyuser.model.dummy.CategoryContent;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.ui.activitys.MainActivity;
import com.menlopark.rentsyuser.util.AppUtility;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.view.adapters.Calender_Adapter;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

public class CalenderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private static final String TAG = CalenderFragment.class.getSimpleName();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CategoryContent.CategoryItem item;
    private CategoryFragment.OnListFragmentInteractionListener mListener;
    private FragmentCalenderBinding binding;
    Context context;
    private List<com.menlopark.rentsyuser.model.Calendar_Data.View> add_List = new ArrayList<>();
    private Calender_Adapter adapter;
    ArrayList<String> arrayList_date;

    Dialog loader;
    ApiService apiService;

    String dynamicTime, selected_date;


    public CalenderFragment() {
        // Required empty public constructor
    }


    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private int load=0;


    public static CalenderFragment newInstance(String param1, String param2) {
        CalenderFragment fragment = new CalenderFragment();
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calender, container, false);
        View view = binding.getRoot();
        binding.setCategoryItems(item);
        context = view.getContext();


        apiService = ApiClient.getClient(PreferenceUtil.getPref(getActivity()).getString(Constants.pref_App_Token, "")).create(ApiService.class);

        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.bookings));

        // Set the adapter

        if (mColumnCount <= 1) {
            binding.list.setLayoutManager(new LinearLayoutManager(context));
        } else {
            binding.list.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        //customstaticData();


       /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        binding.list.setLayoutManager(mLayoutManager);
        binding.list.setItemAnimator(new DefaultItemAnimator());
        binding.list.setAdapter(new BookingListAdapter(add_List, R.layout.list_item_booking, getActivity()) {
            @Override
            protected int getLayoutIdForPosition(int position) {
                return add_List.size();
            }
        });*/

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        binding.list.setLayoutManager(mLayoutManager);
        binding.list.setItemAnimator(new DefaultItemAnimator());

        binding.simpleCalendarView.setDate(System.currentTimeMillis(), true, true);
        binding.simpleCalendarView.setShowWeekNumber(false);
        binding.simpleCalendarView.setFirstDayOfWeek(2);
        binding.simpleCalendarView.setSelectedWeekBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.simpleCalendarView.setUnfocusedMonthDateColor(getResources().getColor(R.color.colorblue));
        binding.simpleCalendarView.setWeekSeparatorLineColor(getResources().getColor(R.color.bottom_gray));
        binding.simpleCalendarView.setSelectedDateVerticalBar(R.color.coloPink);
        binding.simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {

                int sMonth = month + 1;
                selected_date = day + "-" + sMonth + "-" + year;

                call_calendar_Data();
                //Toast.makeText(getActivity().getApplicationContext(), day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
            }
        });

        if (binding.list == null) {
            binding.detailsTap.setVisibility(View.VISIBLE);
        } else {
            binding.detailsTap.setVisibility(View.GONE);
        }

        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.action_bookings));


       /* binding.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(i);
                getActivity().finish();
            }
        });
        binding.relTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), LocationActivity.class);
                getActivity().startActivity(i);
                getActivity().finish();
            }
        });*/


        /*mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // do something, the isChecked will be
        // true if the switch is in the On position
    }
});*/
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        selected_date = df.format(Calendar.getInstance().getTime());
        call_calendar_Data();
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
//        if(load>0)
//        {
//         call_calendar_Data();}
//        else{load++;}
        // Refresh the state of the +1 button each time the activity receives focus.

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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    Calender_Adapter calender_adapter;

    public void call_calendar_Data() {

        CalenderDataParams calenderDataParams = new CalenderDataParams();
        calenderDataParams.setCustomer_id(Prefs.getString(Constants.pref_CustomerId, ""));
        calenderDataParams.setsDate(selected_date);
        /* calenderDataParams.setCustomer_id("5");
        calenderDataParams.setsDate("2018-07-03");*/

        //  Toast.makeText(context, Prefs.getString(Constants.pref_CustomerId, "") + "\n" + selected_date, Toast.LENGTH_SHORT).show();
//        loader = new Dialog(getActivity());
//        loader.show();

        Call<CalendarDataRes> call = apiService.call_CalenderData(calenderDataParams);
        Log.e(TAG, "===>>> " + new Gson().toJson(calenderDataParams));
        call.enqueue(new retrofit2.Callback<CalendarDataRes>() {
            @Override
            public void onResponse(Call<CalendarDataRes> call, retrofit2.Response<CalendarDataRes> response) {

                loader.dismiss();
                if (response.code() == 200) {
                    Log.e("", "onResponse: " + new Gson().toJson(response.body()));

                    Date date1 = null;


                    arrayList_date = new ArrayList<>();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");


                    if(response.body().getData()!=null){
                        for (int i = 0; i < response.body().getData().getView().size(); i++) {
                            add_List.add(response.body().getData().getView().get(i));

                            Calendar cDy = AppUtility.utcToLocal(add_List.get(i).getDateTime());
                            Log.e("calendar_time","time--"+add_List.get(i).getDateTime()+" "+cDy);
                         //  dynamicTime = AppUtility.convertDate(df.format(cDy.getTime()).toString(), "HH:mm aa");


                            arrayList_date.add(add_List.get(i).getDateTime());
                        }


                        calender_adapter = new Calender_Adapter(getActivity(), add_List, arrayList_date);
                        binding.list.setAdapter(calender_adapter);
                        calender_adapter.notifyDataSetChanged();

                    }
                    else{
                        add_List.clear();
                        if (arrayList_date == null)
                            arrayList_date = new ArrayList<>();
                        arrayList_date.clear();
                        calender_adapter = new Calender_Adapter(getActivity(), add_List, arrayList_date);
                        binding.list.setAdapter(calender_adapter);
                        calender_adapter.notifyDataSetChanged();
                        loader.dismiss();
                    }



                    //  String formattedDate = tf.format(date1);




                    // Toast.makeText(getActivity(), response.body().getData().getView().get(0).getItemName(), Toast.LENGTH_SHORT).show();

                } else {
                    add_List.clear();
                    if (arrayList_date == null)
                        arrayList_date = new ArrayList<>();
                    arrayList_date.clear();
                    calender_adapter = new Calender_Adapter(getActivity(), add_List, arrayList_date);
                    binding.list.setAdapter(calender_adapter);
                    calender_adapter.notifyDataSetChanged();
                    loader.dismiss();
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CalendarDataRes> call, Throwable t) {
                //showProgress(false);
                Toast.makeText(context, t.toString() + "", Toast.LENGTH_SHORT).show();
                loader.dismiss();
            }
        });


    }


}
