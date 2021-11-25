package com.menlopark.rentsyuser;

import android.content.Context;
import android.content.ContextWrapper;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.google.firebase.iid.FirebaseInstanceId;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.menlopark.rentsyuser.di.component.AppComponent;
import com.menlopark.rentsyuser.di.component.DaggerAppComponent;
import com.menlopark.rentsyuser.di.module.ApplicationModule;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.pixplicity.easyprefs.library.Prefs;

public class RenrsyUser extends MultiDexApplication {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder().applicationModule(new ApplicationModule(this)).build();
        FacebookSdk.sdkInitialize(getApplicationContext());
        FirebaseInstanceId.getInstance().getToken();

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        if (!Config.getPreference(PreferenceUtil.getPref(this).getString(Constants.pref_App_Token,"")).equals("")) {
            Constants.APP_TOKEN=PreferenceUtil.getPref(this).getString(Constants.pref_App_Token,"");
        }

        new Instabug.Builder(this, "54dee96dc81ab07b4dd08991cd9da772")
                .setInvocationEvents(InstabugInvocationEvent.SHAKE, InstabugInvocationEvent.SCREENSHOT)
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
