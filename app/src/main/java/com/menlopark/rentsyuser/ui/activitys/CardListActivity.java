package com.menlopark.rentsyuser.ui.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ServerCall;
import com.menlopark.rentsyuser.payment.CardInfo;
import com.menlopark.rentsyuser.payment.CardListAdapter;
import com.menlopark.rentsyuser.payment.OnCardClickListener;
import com.menlopark.rentsyuser.server_access.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CardListActivity extends AppCompatActivity implements OnCardClickListener {

    private RelativeLayout relMain;
    private RecyclerView cardsList;
    private Button btn_add_new_card;
    CardListAdapter mAdapter;
    List<CardInfo> cardInfos = new ArrayList<>();
    Activity act;

    private void assignViews() {
        relMain = (RelativeLayout) findViewById(R.id.rel_main);
        cardsList = (RecyclerView) findViewById(R.id.cards_list);
        btn_add_new_card = (Button) findViewById(R.id.btn_add_new_card);


        mAdapter = new CardListAdapter(cardInfos, R.layout.list_item_cards, act, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
        cardsList.setLayoutManager(mLayoutManager);
        mLayoutManager = new LinearLayoutManager(act) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        cardsList.setItemAnimator(new DefaultItemAnimator());


        btn_add_new_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(act, NewCardAddActivity.class));
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_list);
        act = CardListActivity.this;

        getSupportActionBar().setTitle("Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assignViews();
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

    @Override
    public void onCardClick(CardInfo item, int position) {

    }

    public void call_getListOfCards() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", Config.getPreference(Constants.pref_CustomerId));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("call_getListOfCards: ", jsonObject.toString() + "");
        ServerCall.getResponse(act, "my-payment-methods", jsonObject.toString(), true, new ServerCall.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    cardInfos.clear();
                    JSONObject obj = new JSONObject(result);
                    JSONObject data_object = obj.getJSONObject("data");
                    JSONArray jsonArray = data_object.getJSONArray("card_info");

                    CardInfo[] projectsList = new Gson().fromJson(jsonArray.toString(), CardInfo[].class);
                    for (CardInfo project : projectsList) {
                        cardInfos.add(project);
                    }
                    cardsList.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                }

            }

            @Override
            public void onError(String error) {
                Toast.makeText(act, error + "", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        call_getListOfCards();
    }
}
