package com.menlopark.rentsyuser.di.module;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.api.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


@Module
public class ApiModule {
    @Provides
    @Singleton
    public ApiService apiService(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        return new Retrofit.Builder()
                .baseUrl(Config.API_URL)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build()
                .create(ApiService.class);
    }
}
