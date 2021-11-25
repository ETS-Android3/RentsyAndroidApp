package com.menlopark.rentsyuser.ui.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.databinding.ActivityTermConditionBinding;
import com.menlopark.rentsyuser.model.Term_Condition.TermParams;
import com.menlopark.rentsyuser.model.Term_Condition.TermRes;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.util.Dialog;

import retrofit2.Call;

public class Term_ConditionActivity extends AppCompatActivity {

    ActivityTermConditionBinding binding;
    Activity act;
    ApiService apiService;
    Dialog loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_term_condition);

        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        call_term_and_condition();
    }

    private void init() {
        act = Term_ConditionActivity.this;
        apiService = ApiClient.getClient(PreferenceUtil.getPref(act).getString(Constants.pref_App_Token,"")).create(ApiService.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }

    public void call_term_and_condition() {
        TermParams termParams = new TermParams();
        termParams.setCategory("1");

        Call<TermRes> call = apiService.call_Term_Condition(termParams);
        loader = new Dialog(Term_ConditionActivity.this);
        loader.show();
        call.enqueue(new retrofit2.Callback<TermRes>() {
            @Override
            public void onResponse(Call<TermRes> call, retrofit2.Response<TermRes> response) {
                loader.dismiss();
                if (response.code() == 200) {
                    binding.txtContent.setText(Html.fromHtml(response.body().getData().getmDetails().get(0).getDetails()));
                } else {
                    loader.dismiss();
                    Toast.makeText(act, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TermRes> call, Throwable t) {
                loader.dismiss();
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
