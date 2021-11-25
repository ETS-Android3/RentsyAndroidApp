package com.menlopark.rentsyuser.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.model.Notification.Detail;
import com.menlopark.rentsyuser.model.Notification.NotiParam;
import com.menlopark.rentsyuser.model.Notification.NotiRes;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.ui.activitys.MainActivity;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.view.adapters.NotificationAdapter;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {

    private NotificationAdapter mAdapter;
    private RecyclerView addressList;
    private List<Detail> add_List = new ArrayList<>();
    ApiService apiService;
    Dialog loader;
    String sCus_id;
    TextView tv_note;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notification, container, false);
        apiService = ApiClient.getClient(PreferenceUtil.getPref(getActivity()).getString(Constants.pref_App_Token,"")).create(ApiService.class);
        addressList = (RecyclerView) root.findViewById(R.id.notification_list);


        tv_note = root.findViewById(R.id.tv_note);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        addressList.setLayoutManager(mLayoutManager);
        addressList.setItemAnimator(new DefaultItemAnimator());

        sCus_id = Prefs.getString(Constants.pref_CustomerId, "");

        if (sCus_id != null) {
            Log.d("notification_res","called--11" );
            call_notifiocation_data();
        } else {
            tv_note.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "No Data found..!!", Toast.LENGTH_SHORT).show();
        }

        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.action_notification));
        return root;
    }

    private void call_notifiocation_data() {

        NotiParam notiParam = new NotiParam();
        //  notiParam.setCustomer_id("1");
        Log.d("notification_res","called--"+Prefs.getString(Constants.pref_CustomerId, "") );
        notiParam.setCustomer_id(Prefs.getString(Constants.pref_CustomerId, ""));
        //notiParam.setCustomer_id("51");
        notiParam.setPage("1");

        loader = new Dialog(getActivity());
        loader.show();

        Call<NotiRes> call = apiService.call_Notification_List(notiParam);

        call.enqueue(new Callback<NotiRes>() {
            @Override
            public void onResponse(Call<NotiRes> call, Response<NotiRes> response) {

               // Long sResponce = response.body().getStatus();
             //   String sMsg = response.body().getMessage();
              //  Log.d("notification_res","response-"+response.code()+" "+response.body().getData().getNotificationInfo().size());


                if (response.code() == 200) {
                    loader.dismiss();

                    //Toast.makeText(getActivity(), sMsg, Toast.LENGTH_SHORT).show();

//                    for (int i = 0; i < response.body().getData().getNotificationInfo().size(); i++) {
//                        add_List.add(response.body().getData().getNotificationInfo().get(i));
//                    }
//
//                    if (add_List.size() ==0){
//                        tv_note.setVisibility(View.VISIBLE);


//                    }

                    if(response.body().getData()!=null){
                        if (response.body().getData().getNotificationInfo().size() > 0) {
                            mAdapter = new NotificationAdapter(getActivity(), response.body().getData().getNotificationInfo());
                            addressList.setAdapter(mAdapter);
                        } else {
                            tv_note.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "Not Data available..!!", Toast.LENGTH_SHORT).show();
                        }
                    }



                } else {
                    loader.dismiss();
                    //Toast.makeText(getActivity(), sMsg + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotiRes> call, Throwable t) {
                Log.d("notification_res","called--22"+t.getMessage() );
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







