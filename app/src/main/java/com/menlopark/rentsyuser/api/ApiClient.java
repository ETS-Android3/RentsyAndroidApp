package com.menlopark.rentsyuser.api;

import static com.menlopark.rentsyuser.Config.URL_BASE;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.menlopark.rentsyuser.server_access.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    public static Retrofit getClient(final String token) {
       // Log.e("Token ==>>> ", (Constants.APP_TOKEN.equals("") ? token : Constants.APP_TOKEN));
        Retrofit retrofit = null;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(httpLoggingInterceptor);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("app_token", (Constants.APP_TOKEN.equals("") ? token : Constants.APP_TOKEN))
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        OkHttpClient client = httpClient.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

//        retrofit = new Retrofit.Builder()
//                .client(client)
//                .baseUrl(URL_BASE)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }

}

