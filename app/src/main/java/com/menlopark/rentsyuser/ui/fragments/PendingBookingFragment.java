package com.menlopark.rentsyuser.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.databinding.FragmentPendingBookingBinding;
import com.menlopark.rentsyuser.model.Pendding_Booking.PendingBookingParams;
import com.menlopark.rentsyuser.model.Pendding_Booking.PendingBookingRes;
import com.menlopark.rentsyuser.model.dummy.CategoryContent;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.ui.activitys.LoginActivity;
import com.menlopark.rentsyuser.ui.activitys.MainActivity;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.view.adapters.PendingBookingAdapter;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class PendingBookingFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    int mColumnCount = 1;
    OnListFragmentInteractionListener mListener;
    private FragmentPendingBookingBinding binding;
    private Context context;
    ApiService apiService;
    Dialog loader;
    public List<com.menlopark.rentsyuser.model.Pendding_Booking.View> bookiLists;
    String sCus_id;
    LinearLayout rv_contain;

    PendingBookingAdapter adapter;

    public static PendingBookingFragment newInstance(int columnCount) {
        PendingBookingFragment fragment = new PendingBookingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pending_booking, container, false);
        View view = binding.getRoot();
        context = view.getContext();
        rv_contain = view.findViewById(R.id.rv_contain);


        init();
        sCus_id = Prefs.getString(Constants.pref_CustomerId, "");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        binding.list.setLayoutManager(mLayoutManager);
        binding.list.setItemAnimator(new DefaultItemAnimator());

        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.action_pending_bookings));
        return view;
    }

    private void init() {
        apiService = ApiClient.getClient(PreferenceUtil.getPref(getActivity()).getString(Constants.pref_App_Token,"")).create(ApiService.class);
        bookiLists = new ArrayList<com.menlopark.rentsyuser.model.Pendding_Booking.View>();
    }


    public void call_get_Pennding_booking() {
        PendingBookingParams pendingBookingParams = new PendingBookingParams();
        pendingBookingParams.setFlag("0");
        pendingBookingParams.setCustomer_id(Prefs.getString(Constants.pref_CustomerId, ""));
        // pendingBookingParams.setCustomer_id("7");
        loader = new Dialog(getActivity());
        loader.show();
        Call<PendingBookingRes> call = apiService.call_Pending_Booking(pendingBookingParams);
        call.enqueue(new retrofit2.Callback<PendingBookingRes>() {
            @Override
            public void onResponse(Call<PendingBookingRes> call, final retrofit2.Response<PendingBookingRes> response) {
                Log.d("pendingbook","res--"+response.code());
                if (response.code() == 200) {
                    loader.dismiss();

                    if(response.body().getData()!=null){
                        for (int i = 0; i < response.body().getData().getView().size(); i++) {
                            bookiLists.add(response.body().getData().getView().get(i));
                        }

                        if(bookiLists.size()>0){
                            binding.nodatatext.setVisibility(View.GONE);
                            binding.list.setVisibility(View.VISIBLE);
                            adapter = new PendingBookingAdapter(getActivity(), bookiLists);
                            binding.list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }else{
                            binding.nodatatext.setVisibility(View.VISIBLE);
                            binding.list.setVisibility(View.GONE);
                        }
                    }



                } else if (response.code() == 401) {
                    Toast.makeText(getActivity(), response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                } else {
                    loader.dismiss();
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PendingBookingRes> call, Throwable t) {
                Log.d("pendingbook","error--"+t.getMessage());
                Toast.makeText(context, t.toString() + "", Toast.LENGTH_SHORT).show();
                loader.dismiss();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (bookiLists != null) {
            bookiLists.clear();
        }

        if (sCus_id != null) {
            call_get_Pennding_booking();
        } else {
            rv_contain.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "No Data found..!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener extends CategoryFragment.OnListFragmentInteractionListener {
        void onListFragmentInteraction(CategoryContent.CategoryItem item);
    }


}
