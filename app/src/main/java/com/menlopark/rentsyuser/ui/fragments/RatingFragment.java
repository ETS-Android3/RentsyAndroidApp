package com.menlopark.rentsyuser.ui.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.api.ServerCall;
import com.menlopark.rentsyuser.model.Post_Rating.PostRatingRes;
import com.menlopark.rentsyuser.model.Rating.Detail;
import com.menlopark.rentsyuser.model.Rating.RatingParam;
import com.menlopark.rentsyuser.model.Rating.RatingRes;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.ui.activitys.MainActivity;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingFragment extends Fragment implements RatingAdapter.onPostClickListener {

    RecyclerView rv_rating;
    ApiService apiService;
    Dialog loader;
    RatingAdapter ratingAdapter;

    public ArrayList<Detail> SmileList;
    Button btn_finish;
    Gson gson;
    String sOrder_id;
    TextView tv_note;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating, container, false);

        apiService = ApiClient.getClient(PreferenceUtil.getPref(getActivity()).getString(Constants.pref_App_Token,"")).create(ApiService.class);
        gson = new Gson();

        tv_note = view.findViewById(R.id.tv_note);
        /*if (getArguments() != null) {
            // sOrder_id = getArguments().getString("item_id");
        }*/

        sOrder_id = "1"; // Change for static Data
        if (sOrder_id != null) {
            get_rating_data();
        } else {
            Toast.makeText(getActivity(), "No Data found..!!", Toast.LENGTH_SHORT).show();
        }

        btn_finish = view.findViewById(R.id.btn_finish);
        SmileList = new ArrayList<>();
        rv_rating = view.findViewById(R.id.rating_list);
        rv_rating.setLayoutManager(new LinearLayoutManager(getActivity()));

        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.reating));


        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray jsonArray = ratingAdapter.getRatingData();
                postToServer(jsonArray);
            }
        });
        return view;
    }


    private void get_rating_data() {
        RatingParam ratingParam = new RatingParam();
        Log.d("ratingget","called---"+sOrder_id);
        ratingParam.setOrder_id(sOrder_id);

        loader = new Dialog(getActivity());
        loader.show();

        Call<RatingRes> call = apiService.call_get_rating(ratingParam);
        call.enqueue(new Callback<RatingRes>() {
            @Override
            public void onResponse(Call<RatingRes> call, Response<RatingRes> response) {

                String sMsg = response.body().getMessage();
                if (response.code() == 200) {
                    loader.dismiss();
                  //  Toast.makeText(getActivity(), sMsg, Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < response.body().getData().getDetails().size(); i++) {
                        SmileList.add(response.body().getData().getDetails().get(i));
                    }

                    if (SmileList.size() == 0) {
                        btn_finish.setVisibility(View.GONE);
                        tv_note.setVisibility(View.VISIBLE);
                    }
                    ratingAdapter = new RatingAdapter(SmileList, getActivity());
                    rv_rating.setAdapter(ratingAdapter);

                } else {
                    loader.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RatingRes> call, Throwable t) {
                Log.d("ratingget","called---"+t.getMessage());
                loader.dismiss();

            }
        });
    }

    @Override
    public void onItemClick(Detail item) {
        // String sName = item.getRates().toString();
        // Toast.makeText(getActivity(), sName + "AA" + "-", Toast.LENGTH_SHORT).show();
    }


    public void postToServer(JSONArray jsonArray) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", sOrder_id);
            jsonObject.put("cust", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ServerCall.getResponse(getActivity(), "rate-cust", jsonObject.toString(), true, new ServerCall.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                PostRatingRes postRatingRes = new PostRatingRes();
                postRatingRes = gson.fromJson(result, PostRatingRes.class);
                Toast.makeText(getActivity(), postRatingRes.getMessage(), Toast.LENGTH_SHORT).show();
                ratingAdapter.getArrayClear();
                //{"order_id":"1","cust":[{"catalogue_id":1,"ratings":2},{"catalogue_id":5,"ratings":3}]}
                //{"status":"SUCCESS","message":"Thank You","data":null}
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(), error + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void PostRatingToRetrofit(JSONArray studentsArry) {

        /*HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("order_id", "1");
        hashMap.put("cust", "[{\"catalogue_id\":6,\"ratings\":4}]");
*/

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", "1");
            jsonObject.put("cust", "[{\"catalogue_id\":6,\"ratings\":4}]");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<PostRatingRes> call = apiService.call_post_rating(jsonObject);
        call.enqueue(new Callback<PostRatingRes>() {
            @Override
            public void onResponse(Call<PostRatingRes> call, Response<PostRatingRes> response) {
                Toast.makeText(getActivity(), response.body() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PostRatingRes> call, Throwable t) {
                loader.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
