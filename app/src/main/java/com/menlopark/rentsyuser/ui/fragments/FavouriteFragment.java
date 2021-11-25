package com.menlopark.rentsyuser.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.model.favorite_store.FavoriteListModel;
import com.menlopark.rentsyuser.model.stores.StoreModel;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.ui.activitys.MainActivity;
import com.menlopark.rentsyuser.view.adapters.FavouriteRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FavouriteFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_LOCATION_ID = "location_id";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private int location_id = 1;
    private OnListFragmentInteractionListener mListener;
    Activity act;
    ApiService apiService;
    Dialog dialog;
    public List<com.menlopark.rentsyuser.model.stores.Detail> storeDetails;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    RecyclerView recyclerView;

    public FavouriteFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FavouriteFragment newInstance(int columnCount, int location_id) {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putInt(ARG_LOCATION_ID, location_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            location_id = getArguments().getInt(ARG_LOCATION_ID);
        }

    }

    private void init() {
        act = getActivity();
        dialog = new Dialog(act);
//        apiService = ApiClient.getClient(Config.getPreference(act, Constants.pref_App_Token)).create(ApiService.class);
        apiService = ApiClient.getClient(PreferenceUtil.getPref(act).getString(Constants.pref_App_Token,"")).create(ApiService.class);
        storeDetails = new ArrayList<>();

    }

    @Override
    public void onResume() {
        super.onResume();
        storeDetails.clear();
        call_Stores();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }

        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.action_favorite));


        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(com.menlopark.rentsyuser.model.stores.Detail item);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // call_Stores();
        } else {
        }
    }


    public void call_Stores() {
        dialog.show();
        FavoriteListModel favoriteListModel = new FavoriteListModel();
        favoriteListModel.setCustomer_id(Config.getPreference(Constants.pref_CustomerId));
        //
       // Call<StoreModel> call = apiService.callFavoriteList(favoriteListModel);

        Call<StoreModel> call = apiService.callFavoriteList("Bearer "+PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, ""));
        Log.e("call_Stores: ", new Gson().toJson(favoriteListModel));
        call.enqueue(new retrofit2.Callback<StoreModel>() {
            @Override
            public void onResponse(Call<StoreModel> call, retrofit2.Response<StoreModel> response) {
                if (response.code() == 200) {
                    dialog.dismiss();
                    if(response.body().getData()!=null){
                        for (int i = 0; i < response.body().getData().getDetails().size(); i++) {
                            storeDetails.add(response.body().getData().getDetails().get(i));
                        }
                        recyclerView.setAdapter(new FavouriteRecyclerViewAdapter(storeDetails, mListener, getActivity()));
                    }



                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), response.body().getMessage()+"", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<StoreModel> call, Throwable t) {
                //showProgress(false);
                dialog.dismiss();
                Log.d("favourite","reserror-"+t.getMessage()+" token: "+PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, ""));
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();

            }
        });

    }


}
