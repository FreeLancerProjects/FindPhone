package com.SimiColon.MobileSearch.findphone.Services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Elashry on 12/03/2018.
 */

public class ServiceApi {

    public static final String BASE_URL="http://mobilost.semicolonsoft.com/";
    private static Retrofit retrofit=null;

    public static Retrofit createClient(){
        if (retrofit==null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
