package com.menlopark.rentsyuser.ui.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.databinding.ActivityForgotPasswordBinding;
import com.menlopark.rentsyuser.model.Forgot_model.ForgotParams;
import com.menlopark.rentsyuser.model.Forgot_model.ForgotResp;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.ui.BaseActivity;

import retrofit2.Call;

public class ForgotPasswordActivity extends BaseActivity {

    private static final int REQUEST_READ_CONTACTS = 0;

    static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();
    ActivityForgotPasswordBinding binding;
    Activity act;
    ApiService apiService;
    String sEmail;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        binding.recoverEmail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    sEmail = binding.email.getText().toString();
                    call_forgot_password(sEmail);
                }
            }
        });
    }

    private void init() {
        act = ForgotPasswordActivity.this;
        apiService = ApiClient.getClient(PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, "")).create(ApiService.class);
        dialog = new Dialog(act);
    }

    private boolean isValidate() {
        if (binding.email.getText().toString().trim().length() > 0) {
            if (Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
                return true;
            } else {
                binding.email.setError("Enter Valid Email Id");
                return false;
            }
        } else {
            binding.email.setError("Enter Email Id");
            return false;
        }
    }


    public void call_forgot_password(String sEmail) {
        ForgotParams forgotParams = new ForgotParams();
        forgotParams.setEmail(sEmail);
        dialog.show();
        Log.e(TAG, "==>" + new Gson().toJson(forgotParams));
        Call<ForgotResp> call = apiService.callForgot(forgotParams);
        call.enqueue(new retrofit2.Callback<ForgotResp>() {
            @Override
            public void onResponse(Call<ForgotResp> call, retrofit2.Response<ForgotResp> response) {
                dialog.dismiss();
                Log.e(TAG, response.toString());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Toast.makeText(act, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }
            }

            @Override
            public void onFailure(Call<ForgotResp> call, Throwable t) {
                dialog.dismiss();
                Log.e(TAG, t.getMessage());
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean isEmailValid(String email) {
        Boolean valid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        return valid;
    }


}

