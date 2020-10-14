package com.tahlilgargroup.androidchatlibrary;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static  String API_Common_URL =ChatClass.API_Common_URL;///*"http://185.153.209.110:9003/api/";*/"http://commonwebservice.tahlilgargroup.com/api/";

    private static Retrofit CommonRetrofit=null;



    private static String TAG= ServiceGenerator.class.getSimpleName();

    public static Retrofit GetCommonClient(){

        if (CommonRetrofit==null) {
            OkHttpClient client=new OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .build();
            CommonRetrofit = new Retrofit.Builder()
                    .baseUrl(API_Common_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return CommonRetrofit;
    }



}
