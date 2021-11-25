package com.menlopark.rentsyuser.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.model.stores.Detail;
import com.menlopark.rentsyuser.model.stores.StoreModel;
import com.menlopark.rentsyuser.model.stores.StoresParams;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.ui.activitys.MainActivity;
import com.menlopark.rentsyuser.view.adapters.MyCategoryRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


public class Category_SubCatFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_LOCATION_ID = "location_id";
    private static final String ARG_CAT_ID = "cat_id";
    private int mColumnCount;
    private int location_id;
    private int cat_id;
    private OnListFragmentInteractionListener mListener;
    static Activity act;
    ApiService apiService;
    Dialog dialog;
    public List<Detail> storeDetails;
    RecyclerView recyclerView;

    public static Category_SubCatFragment newInstance(Activity actt, int columnCount, int location_id) {
        Category_SubCatFragment fragment = new Category_SubCatFragment();
        Bundle args = new Bundle();
        act = actt;
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putInt(ARG_LOCATION_ID, location_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            location_id = getArguments().getInt(ARG_LOCATION_ID);
            cat_id = getArguments().getInt(ARG_CAT_ID);

        }
    }

    private void init() {
        dialog = new Dialog(act);
        apiService = ApiClient.getClient(PreferenceUtil.getPref(act).getString(Constants.pref_App_Token,"")).create(ApiService.class);
        storeDetails = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }
        init();
        storeDetails.clear();
        call_Stores_subCat();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onResume();

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }

        //((MainActivity) getActivity()).setActionBarTitle(getString(R.string.action_location));
        ((MainActivity) getActivity()).setActionBarTitle(Config.getPreference(Constants.pref_Current_Store));
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(com.menlopark.rentsyuser.model.stores.Detail item);
    }


    private void call_Stores_subCat() {
        dialog.show();
        StoresParams storesParams = new StoresParams();
        storesParams.setLocation_id(String.valueOf(location_id));
        storesParams.setBusiness_category_id("0");
        storesParams.setBusiness_subcategory_id("0");
        Call<StoreModel> call = apiService.callStores_subCat(storesParams);
        call.enqueue(new retrofit2.Callback<StoreModel>() {
            @Override
            public void onResponse(Call<StoreModel> call, retrofit2.Response<StoreModel> response) {
                if (response.code() == 200) {
                    dialog.dismiss();
                    storeDetails.addAll(response.body().getData().getDetails());
                    recyclerView.setAdapter(new MyCategoryRecyclerViewAdapter(storeDetails, (CategoryFragment.OnListFragmentInteractionListener) mListener, getActivity()));
                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StoreModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
