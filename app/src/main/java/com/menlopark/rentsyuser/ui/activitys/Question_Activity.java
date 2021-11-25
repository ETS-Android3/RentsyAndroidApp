package com.menlopark.rentsyuser.ui.activitys;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.model.Help_QuestionAnswer.Detail;
import com.menlopark.rentsyuser.model.Help_QuestionAnswer.HelpQuestionAnswerParams;
import com.menlopark.rentsyuser.model.Help_QuestionAnswer.HelpQuestionAnswerRes;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;

import java.util.ArrayList;

import retrofit2.Call;

public class Question_Activity extends AppCompatActivity {

    ImageView iv_back;
    RecyclerView rv_questions;
    ApiService apiService;
    Dialog loader;
    String sCat_Id;
    ArrayList<Detail> Que_ans_List;
    QusAws_Adapter qusAws_adapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        apiService = ApiClient.getClient(PreferenceUtil.getPref(this).getString(Constants.pref_App_Token,"")).create(ApiService.class);
        sCat_Id = getIntent().getStringExtra("cat_id");
       // Toast.makeText(this, "ID:" + sCat_Id, Toast.LENGTH_SHORT).show();


        init();
        Que_ans_List = new ArrayList<>();
        rv_questions.setLayoutManager(new LinearLayoutManager(Question_Activity.this));


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Call_Help_QuestionAnswer();
    }

    private void init() {
        iv_back = findViewById(R.id.iv_back);
        rv_questions = findViewById(R.id.rv_questions);
    }

    private void Call_Help_QuestionAnswer() {

        HelpQuestionAnswerParams helpQuestionAnswerParams = new HelpQuestionAnswerParams();
        helpQuestionAnswerParams.setHelp_category_id(sCat_Id);


        loader = new Dialog(Question_Activity.this);
        loader.show();

        Call<HelpQuestionAnswerRes> call = apiService.call_Help_Question_Answer(helpQuestionAnswerParams);
        call.enqueue(new retrofit2.Callback<HelpQuestionAnswerRes>() {
            @Override
            public void onResponse(Call<HelpQuestionAnswerRes> call, final retrofit2.Response<HelpQuestionAnswerRes> response) {

                String sMsg = response.body().getMessage();

                if (response.code() == 200) {
                    loader.dismiss();



                    for (int i = 0; i < response.body().getData().getDetails().size(); i++) {
                        Que_ans_List.add(response.body().getData().getDetails().get(i));
                    }
                    qusAws_adapter = new QusAws_Adapter(Question_Activity.this, Que_ans_List);
                    rv_questions.setAdapter(qusAws_adapter);
                } else {
                    loader.dismiss();
                    Toast.makeText(Question_Activity.this, sMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HelpQuestionAnswerRes> call, Throwable t) {
                Log.d("answer","syntax---"+t.toString());
                Toast.makeText(Question_Activity.this, t.toString() + "", Toast.LENGTH_SHORT).show();
                loader.dismiss();
            }
        });


    }

}
