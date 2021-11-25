package com.menlopark.rentsyuser.ui.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.databinding.ActivitySplaceBinding;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.ui.BaseActivity;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 3000;
    @Inject
    Config config;
    ActivitySplaceBinding binding;
    Activity act;
    ApiService apiService;
    Dialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        act = SplashActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splace);
        apiService = ApiClient.getClient(PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, "")).create(ApiService.class);


       /* try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.menlopark.rentsyuser",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/

        if (!PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, "").equals("")) {
            Constants.APP_TOKEN = PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, "");
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, LocationActivity.class);
                if (!Config.getPreference(Constants.pref_LoginUser).equals("")) {
                    mainIntent.putExtra("isRegister", false);
                    mainIntent.putExtra("isLoggedin", true);
                } else {
                    mainIntent.putExtra("isRegister", false);
                    mainIntent.putExtra("isLoggedin", false);
                }
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, LocationActivity.class);
                mainIntent.putExtra("isRegister", false);
                startActivity(mainIntent);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
