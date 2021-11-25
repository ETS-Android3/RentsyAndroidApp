package com.menlopark.rentsyuser.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.model.HalpCata.Detail;
import com.menlopark.rentsyuser.model.HalpCata.HelpCatRes;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpActivity extends AppCompatActivity {


    RecyclerView Question_list;
    Help_Adapter help_adapter;
    ApiService apiService;
    Dialog loader;
    ArrayList<Detail> HelpQueList;
    ImageView iv_back;
    LinearLayout linGeneral,linTips;

    @Override
    public void onBackPressed() {
       /* Intent intent = new Intent(HelpActivity.this, LocationActivity.class);
        startActivity(intent);*/
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Question_list = findViewById(R.id.Question_list);
        iv_back = findViewById(R.id.iv_back);

        linGeneral=findViewById(R.id.generallin);
        linTips=findViewById(R.id.tipslin);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        apiService = ApiClient.getClient(PreferenceUtil.getPref(this).getString(Constants.pref_App_Token,"")).create(ApiService.class);
        HelpQueList = new ArrayList<>();

        Question_list.setLayoutManager(new LinearLayoutManager(HelpActivity.this));

        Call_Help_Categories();
    }


    private void Call_Help_Categories() {

        loader = new Dialog(HelpActivity.this);
        loader.show();
        Call<HelpCatRes> call = apiService.call_Help_Categories();

        call.enqueue(new Callback<HelpCatRes>() {
            @Override
            public void onResponse(Call<HelpCatRes> call, Response<HelpCatRes> response) {



                if (response.code() == 200) {
                    loader.dismiss();

                    if(!response.body().getStatus().equals("FAIL")) {


                        for (int i = 0; i < response.body().getDetails().size(); i++) {
                           // HelpQueList.add(response.body().getData().getDetails().get(i));
                            if(response.body().getDetails().get(i).getTypeName().equals("GENERAL")){
                                TextView textView1 = new TextView(getApplicationContext());
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(0, 2, 0, 2);
                                textView1.setPadding(50, 20, 30, 20);
                                textView1.setId(response.body().getDetails().get(i).getId());
                                textView1.setTag(response.body().getDetails().get(i).getId());
                                textView1.setText(response.body().getDetails().get(i).getName());
                                textView1.setLayoutParams(params);
                                linGeneral.addView(textView1);

                                textView1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                       // Toast.makeText(getApplicationContext(),view.getId()+"-"+view.getTag().toString(),Toast.LENGTH_SHORT).show();
                                        Intent intent_qus = new Intent(HelpActivity.this, Question_Activity.class);
                                        intent_qus.putExtra("cat_id", view.getTag().toString());
                                        startActivity(intent_qus);
                                    }
                                });

                            }
                            else{
                                TextView textView1 = new TextView(getApplicationContext());
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(0, 2, 0, 2);
                                textView1.setPadding(50, 20, 30, 20);
                                textView1.setId(response.body().getDetails().get(i).getId());
                                textView1.setTag(response.body().getDetails().get(i).getId());
                                textView1.setText(response.body().getDetails().get(i).getName());
                                textView1.setLayoutParams(params);
                                linTips.addView(textView1);
                                textView1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Toast.makeText(getApplicationContext(),view.getId()+"-"+view.getTag().toString(),Toast.LENGTH_SHORT).show();
                                        Intent intent_qus = new Intent(HelpActivity.this, Question_Activity.class);
                                        intent_qus.putExtra("cat_id", view.getTag().toString());
                                        startActivity(intent_qus);
                                    }
                                });
                            }


                        }
                      //  help_adapter = new Help_Adapter(HelpActivity.this, HelpQueList);
                      //  Question_list.setAdapter(help_adapter);
                    }



                } else {
                    loader.dismiss();
                }
            }

            @Override
            public void onFailure(Call<HelpCatRes> call, Throwable t) {
                loader.dismiss();

            }
        });


    }


}
