package com.menlopark.rentsyuser.di.module;

import android.content.Context;


import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.RenrsyUser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final RenrsyUser mApp;

    public ApplicationModule(RenrsyUser app) {
        mApp = app;
    }

    @Provides
    @Singleton
    public Context appContext() {
        return mApp;
    }

    @Provides
    @Singleton
    public Config config() {
        return new Config(mApp);
    }

}
