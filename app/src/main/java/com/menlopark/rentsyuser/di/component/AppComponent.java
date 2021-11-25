package com.menlopark.rentsyuser.di.component;

import android.content.Context;


import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.di.module.ApiModule;
import com.menlopark.rentsyuser.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface AppComponent {
    Context appContext();

    Config config();

    ApiService apiService();
}
