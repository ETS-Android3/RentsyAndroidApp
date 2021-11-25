package com.menlopark.rentsyuser.ui.fragments;

import static com.menlopark.rentsyuser.server_access.Constants.selected_business_cat_id;
import static com.menlopark.rentsyuser.server_access.Constants.selected_business_sub_cat_id;
import static com.menlopark.rentsyuser.server_access.Constants.selected_is_min_max;
import static com.menlopark.rentsyuser.server_access.Constants.selected_max_price;
import static com.menlopark.rentsyuser.server_access.Constants.selected_min_price;
import static com.menlopark.rentsyuser.ui.activitys.MainActivity.screenName;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.menlopark.rentsyuser.model.stores.AllCategory;
import com.menlopark.rentsyuser.model.stores.AllPrice;
import com.menlopark.rentsyuser.model.stores.AllSubCategory;
import com.menlopark.rentsyuser.model.stores.StoreModel;
import com.menlopark.rentsyuser.model.stores.StoresParams;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.ui.activitys.MainActivity;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.view.adapters.MyCategoryRecyclerViewAdapter;
import com.menlopark.rentsyuser.view.custom_views.SpinnerWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;


public class CategoryFragment extends Fragment implements SpinnerWindow.SpinnerWindow_interface {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_LOCATION_ID = "location_id";
    private static final String ARG_SCREENAME = "screen_name";
    private static final String ARG_CAT_ID = "cat_id";

    static Activity act;
    public List<com.menlopark.rentsyuser.model.stores.Detail> storeDetails;
    public List<com.menlopark.rentsyuser.model.stores.AllCategory> allCategories;
    public List<com.menlopark.rentsyuser.model.stores.AllSubCategory> allSubCategories;
    public List<com.menlopark.rentsyuser.model.stores.AllPrice> allPrices;
    //    public List<String> priceFilterData;
    boolean isCategoryClick = false;
    boolean isSubCategoryClick = false;
    boolean isPriceClick = false;
    ApiService apiService;
    Dialog dialog;
    RecyclerView recyclerView;
    private int mColumnCount;
    private int location_id;
    private int cat_id;

    private OnListFragmentInteractionListener mListener;

    public static CategoryFragment newInstance(Activity actt, int columnCount, int location_id, String screenName) {
        CategoryFragment fragment = new CategoryFragment();

        act = actt;
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putInt(ARG_LOCATION_ID, location_id);
        args.putString(ARG_SCREENAME, screenName);
     Log.d("data_passed","1---"+location_id+" "+screenName);

        //args.putInt(ARG_CAT_ID, cat_id);
        fragment.setArguments(args);


        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onResume();

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            location_id = getArguments().getInt(ARG_LOCATION_ID);

            cat_id = getArguments().getInt(ARG_CAT_ID);
           // screenName = getArguments().getString(ARG_SCREENAME);

            Log.d("data_passed","data11100---"+location_id+" "+getArguments().getInt(ARG_LOCATION_ID)+" "+screenName);

            selected_business_cat_id = "0";
            selected_business_sub_cat_id = "0";
            selected_is_min_max = "0";
            selected_min_price = "0";
            selected_max_price = "0";

          // init();

        }
        dialog = new Dialog(act);
    }

    private void init() {
        //act = getActivity();

        apiService = ApiClient.getClient(PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, "")).create(ApiService.class);
        storeDetails = new ArrayList<>();
        allCategories = new ArrayList<>();
        allSubCategories = new ArrayList<>();
        allPrices = new ArrayList<>();


        if (screenName != null) {
            Log.d("screenname","name--"+screenName);
            if (screenName.equals("Category")) {
                if(location_id!=0){
                    call_Stores_cat();
                }
//                else{
//                    if(location_id!=0){
//                        call_Stores_cat();
//                    }
//                    else{
//                        call_Stores_cat();
//                    }
//                }


            } else if (screenName.equals("Sub Category")) {
               // call_Stores_cat();
                call_Stores_subCat();
            } else {
                call_Stores_Price();
            }
        } else {
            Log.d("screenname","name--null");
            //call_Stores_cat();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }
        Log.d("onresume","the method is called");
        init();
        storeDetails.clear();
        allCategories.clear();
        allSubCategories.clear();
        allPrices.clear();



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

        ((MainActivity) getActivity()).setActionBarTitle(Config.getPreference(Constants.pref_Current_Store));
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void selectedSpinnerItem(int selected_position) {
        if (screenName.equals(getString(R.string.category))) {
            selected_business_cat_id = allCategories.get(selected_position).categoryId.toString();
            if (selected_position == 0) {
                selected_business_cat_id = "0";


            }
            isCategoryClick = false;
            call_Stores_cat();

            ((MainActivity) getActivity()).setTabText(1, allCategories.get(selected_position).categoryName);
        } else if (screenName.equals(getString(R.string.sub_category))) {
            if (selected_position == 0) {
                selected_business_sub_cat_id = "0";

            }
            isSubCategoryClick = false;
            Log.d("subcate","data---"+allSubCategories.size()+" "+selected_position);
            for (int i=0;i>=allSubCategories.size();i++){
                Log.d("subcate","data11100---"+allSubCategories.get(i).subcategory_id+" "+selected_position);
            }
            selected_business_sub_cat_id = allSubCategories.get(selected_position).subcategory_id.toString();
            ((MainActivity) getActivity()).setTabText(2, allSubCategories.get(selected_position).subcategory_name);
            call_Stores_subCat();
        } else {
            selected_is_min_max = "1";
            selected_min_price = allPrices.get(selected_position).minPrice;
            selected_max_price = allPrices.get(selected_position).maxPrice;

            if (selected_position == 0) {

                selected_min_price = "0";
                selected_max_price = "0";
                selected_is_min_max = "0";
            }
            isPriceClick = false;
            ((MainActivity) getActivity()).setTabText(3, allPrices.get(selected_position).itemName);
            call_Stores_Price();
        }
    }

    public void openBottomBar(Integer pos) {

        Log.d("tab_barcl","clicked---1-"+pos);
        if (pos == 1) {
            //screenName="Category";
            if (allCategories.size() <= 0) {
                isCategoryClick = true;

                call_Stores_cat();
            } else {
                viewBottomSpinner(pos);

            }
        } else if (pos == 2) {
          //  screenName="Sub Category";
            if (allSubCategories.size() <= 0) {
                isSubCategoryClick = true;
                call_Stores_subCat();
            } else {
                viewBottomSpinner(pos);

            }

        } else {
            //screenName="Price";
            if (allPrices.size() <= 0) {
                isPriceClick = true;
                call_Stores_Price();
            } else {
                viewBottomSpinner(pos);

            }

        }
    }

    public void call_Stores_cat() {
        StoresParams storesParams = new StoresParams();
        storesParams.setLocation_id(String.valueOf(location_id));
        storesParams.setBusiness_category_id(selected_business_cat_id);

        Log.d("data_passed","data9900---"+String.valueOf(location_id)+" "+selected_business_cat_id);
        if(location_id!=0){
            Call<StoreModel> call = apiService.callStores(storesParams);
            if (dialog != null && !dialog.isShowing())
                dialog.show();
            call.enqueue(new retrofit2.Callback<StoreModel>() {
                @Override
                public void onResponse(Call<StoreModel> call, retrofit2.Response<StoreModel> response) {
                    if (response.code() == 200) {
                        dialog.dismiss();
                        Log.d("data_passed","data9900---onResponse--"+response.body().getStatus() + "");

                        if(!response.body().getStatus().equals("FAIL")){

                            allCategories.clear();
                            storeDetails.clear();
                            storeDetails.addAll(response.body().getData().getDetails());

                            AllCategory allCategory = new AllCategory();
                            allCategory.categoryId = 0;
                            allCategory.categoryName = "All";
                            allCategories.add(0, allCategory);



                            allCategories.addAll(response.body().getData().getAllCategory());

                            Log.d("data_passed","data9900---onResponse--"+response.body().getStatus() + " "+
                                    " "+response.body().getData().getAllCategory().size());


                            Log.d("data_passed","data5566---"+" first_called--"+allCategories.size()+" "+storeDetails.size());
                            recyclerView.setAdapter(new MyCategoryRecyclerViewAdapter(storeDetails, mListener, getActivity()));
                            if (isCategoryClick) {
                                isCategoryClick = false;
                                Log.d("tab_barcl","clicked---2");
                                viewBottomSpinner(1);

                            }
                        }
                        else{
                            storeDetails.clear();
                            recyclerView.setAdapter(new MyCategoryRecyclerViewAdapter(storeDetails, mListener, getActivity()));
                        }


                    } else {
                        dialog.dismiss();
                       // Toast.makeText(getActivity(), response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<StoreModel> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            //call_Stores_cat();
        }

    }

    private void call_Stores_subCat() {
        if (dialog != null && !dialog.isShowing())
            dialog.show();
        StoresParams storesParams = new StoresParams();
        storesParams.setLocation_id(String.valueOf(location_id));
        storesParams.setBusiness_category_id(selected_business_cat_id);
        storesParams.setBusiness_subcategory_id(selected_business_sub_cat_id);


           Log.d("subcat","para-"+location_id+" "+selected_business_cat_id+" "+selected_business_sub_cat_id);


        Call<StoreModel> call = apiService.callStores_subCat(storesParams);
        call.enqueue(new retrofit2.Callback<StoreModel>() {
            @Override
            public void onResponse(Call<StoreModel> call, retrofit2.Response<StoreModel> response) {
                if (response.code() == 200) {
                    dialog.dismiss();
                    if(!response.body().getStatus().equals("FAIL")){

                        allSubCategories.clear();
                        storeDetails.clear();

                        storeDetails.addAll(response.body().getData().getDetails());

                        Log.e("onResponse4455: ", response.body() + "");

                        AllSubCategory allSubCategory = new AllSubCategory();
                        allSubCategory.subcategory_id = 0;
                        allSubCategory.subcategory_name = "All";
                        allSubCategories.add(0, allSubCategory);

                        allSubCategories.addAll(response.body().getData().getAllSubCategory());
                        recyclerView.setAdapter(new MyCategoryRecyclerViewAdapter(storeDetails, mListener, getActivity()));
                    }


                } else {
                    dialog.dismiss();
                    storeDetails.clear();
                    recyclerView.setAdapter(new MyCategoryRecyclerViewAdapter(storeDetails, mListener, getActivity()));
                    Toast.makeText(getActivity(), response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                }
                if (isSubCategoryClick) {
                    isSubCategoryClick = false;
                    viewBottomSpinner(2);
                }

            }

            @Override
            public void onFailure(Call<StoreModel> call, Throwable t) {
                dialog.dismiss();
                allSubCategories.clear();
                recyclerView.setAdapter(new MyCategoryRecyclerViewAdapter(storeDetails, mListener, getActivity()));
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void call_Stores_Price() {
        // Toast.makeText(getActivity(), "Price", Toast.LENGTH_SHORT).show();
        if (dialog != null && !dialog.isShowing())
            dialog.show();
        StoresParams storesParams = new StoresParams();
        storesParams.setLocation_id(String.valueOf(location_id));

        storesParams.setBusiness_category_id(selected_business_cat_id);
        storesParams.setBusiness_subcategory_id(selected_business_sub_cat_id);
        //storesParams.setIs_min_max(selected_is_min_max);
        storesParams.setIs_min_max("1");
        storesParams.setMin_price(selected_min_price);
        storesParams.setMax_price(selected_max_price);

        Log.d("price_api","parameter--"+location_id+" "+selected_business_cat_id+" "+selected_business_sub_cat_id+" "+
                selected_is_min_max+" "+selected_min_price+" "+selected_max_price);

        Call<StoreModel> call = apiService.callgetPriceWiseList(storesParams);
        call.enqueue(new retrofit2.Callback<StoreModel>() {
            @Override
            public void onResponse(Call<StoreModel> call, retrofit2.Response<StoreModel> response) {
                Log.d("price_api","res-"+response.code());
                if (response.code() == 200) {
                    dialog.dismiss();
                    if(!response.body().getStatus().equals("FAIL")){

                        Log.d("price_api","size-"+response.body().getData().getDetails().size()+" flag-"+isPriceClick);
                        storeDetails.clear();
                        storeDetails.addAll(response.body().getData().getDetails());
                        recyclerView.setAdapter(new MyCategoryRecyclerViewAdapter(storeDetails, mListener, getActivity()));
                        if (isPriceClick) {
                            isPriceClick = false;
                            viewBottomSpinner(3);
                        }
                    }

                } else {
                    dialog.dismiss();
                    storeDetails.clear();
                    recyclerView.setAdapter(new MyCategoryRecyclerViewAdapter(storeDetails, mListener, getActivity()));
                   // Toast.makeText(getActivity(), response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StoreModel> call, Throwable t) {
                dialog.dismiss();
               // Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void viewBottomSpinner(int screen) {
        Log.d("open_spin","screen--"+screen+" name-"+screenName);

        if (((MainActivity) getActivity()).getViewPagerVisibility() == View.VISIBLE) {


            if (screen == 1) {
                Log.d("data_passed","data5566---"+" second_called--");
                ArrayList<String> dataArray = new ArrayList<>();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Collections.sort(allCategories, Comparator.comparing(AllCategory::getCategoryName));
                }
                for (int i = 0; i < allCategories.size(); i++) {
                    if (allCategories.get(i).categoryName.equals("All")) {
                        Collections.swap(allCategories, i, 0);
                    }

                }

                for (int i = 0; i < allCategories.size(); i++) {
                    dataArray.add(allCategories.get(i).categoryName);
                }
                if (dataArray.size() > 0) {
                    new SpinnerWindow(CategoryFragment.this);
                    SpinnerWindow.showSpinner(getActivity(), dataArray);

                }

//                if(allCategories!=null){
//                    Log.d("allcategory","size---it is null");
//                    call_Stores_cat();
//                    Handler handler=new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    },200);
//                    Log.d("allcategory","size---"+allCategories.size());
//                }
//                else{
//                    call_Stores_cat();
//                    Handler handler=new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                Collections.sort(allCategories, Comparator.comparing(AllCategory::getCategoryName));
//                            }
//                            for (int i = 0; i < allCategories.size(); i++) {
//                                if (allCategories.get(i).categoryName.equals("All")) {
//                                    Collections.swap(allCategories, i, 0);
//                                }
//
//                            }
//
//                            for (int i = 0; i < allCategories.size(); i++) {
//                                dataArray.add(allCategories.get(i).categoryName);
//                            }
//                            if (dataArray.size() > 0) {
//                                new SpinnerWindow(CategoryFragment.this);
//                                SpinnerWindow.showSpinner(getActivity(), dataArray);
//
//                            }
//                        }
//                    },200);
//                    Log.d("allcategory","size---"+allCategories.size());
//                }


            } else if (screen == 2) {
                ArrayList<String> dataArray = new ArrayList<>();
                for (int i = 0; i < allSubCategories.size(); i++) {
                    if (allSubCategories.get(i).subcategory_name.equals("All")) {
                        Collections.swap(allSubCategories, i, 0);
                    }

                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Collections.sort(allSubCategories, Comparator.comparing(AllSubCategory::getSubcategory_name));
                }
                for (int i = 0; i < allSubCategories.size(); i++) {
                    dataArray.add(allSubCategories.get(i).subcategory_name);
                }
                if (dataArray.size() > 0) {
                    new SpinnerWindow(this);
                    SpinnerWindow.showSpinner(getActivity(), dataArray);

                }
            } else if (screen == 3) {
                allPrices.clear();
                AllPrice allPrice;

                allPrice = new AllPrice();
                allPrice.itemName = "All";
                allPrice.minPrice = "0";
                allPrice.maxPrice = "0";
                allPrices.add(allPrice);


                allPrice = new AllPrice();
                allPrice.itemName = "Under $50";
                allPrice.minPrice = "0";
                allPrice.maxPrice = "50";
                allPrices.add(allPrice);

                allPrice = new AllPrice();
                allPrice.itemName = "$50 - $150";
                allPrice.minPrice = "50";
                allPrice.maxPrice = "150";
                allPrices.add(allPrice);

                allPrice = new AllPrice();
                allPrice.itemName = "$150 - $500";
                allPrice.minPrice = "150";
                allPrice.maxPrice = "500";
                allPrices.add(allPrice);

                allPrice = new AllPrice();
                allPrice.itemName = "$500 - $1000";
                allPrice.minPrice = "500";
                allPrice.maxPrice = "1000";
                allPrices.add(allPrice);

                allPrice = new AllPrice();
                allPrice.itemName = "Over $1000";
                allPrice.minPrice = "1000";
                allPrice.maxPrice = "9999";
                allPrices.add(allPrice);

                ArrayList<String> dataArray = new ArrayList<>();
                for (int i = 0; i < allPrices.size(); i++) {
                    dataArray.add(allPrices.get(i).itemName);
                }
                if (dataArray.size() > 0) {
                    new SpinnerWindow(this);
                    SpinnerWindow.showSpinner(getActivity(), dataArray);

                }
            }
        }


    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(com.menlopark.rentsyuser.model.stores.Detail item);
    }
}
