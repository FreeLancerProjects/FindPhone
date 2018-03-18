package com.SimiColon.MobileSearch.findphone.Services;

import com.SimiColon.MobileSearch.findphone.Model.User_Model;
import com.SimiColon.MobileSearch.findphone.Model.Report_Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Elashry on 12/03/2018.
 */

public interface Service {

    /*---------------------------------------- user login & register -------------------------------------------*/

    @FormUrlEncoded
    @POST("api/registration")
    Call<User_Model> userSignUp(
            @Field("name")     String name,
            @Field("password") String password,
            @Field("photo")    String photo,
            @Field("phone")    String phone,
            @Field("email")    String email,
            @Field("country")  String country,
            @Field("city")     String city,
            @Field("address")  String address);

    @FormUrlEncoded
    @POST("api/login")
    Call<User_Model> userLogIn(
            @Field("user_email")    String email,
            @Field("password") String password);

     /*---------------------------------------- find phone & report -------------------------------------------*/

    @FormUrlEncoded
    @POST("api/reportphone")
    Call<Report_Model> reportphone(
            @Field("imei")       String imei,
            @Field("brand")      String brand,
            @Field("owner")       String owner,
            @Field("statue")      String statue,
            @Field("phone")       String phone,
            @Field("email")       String email,
            @Field("adress")      String adress,
            @Field("photo")        String photo,
            @Field("description")  String description);

    @FormUrlEncoded
    @POST("api/findephone")
    Call<List<Report_Model>> findephone(
            @Field("imei")       String imei);

    @GET("api/allphones")
    Call<List<Report_Model>> getallphones();




}
