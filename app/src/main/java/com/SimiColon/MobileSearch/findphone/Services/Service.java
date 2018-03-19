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
    @POST("api/auth")
    Call<User_Model> userLogIn(
            @Field("email")    String email,
            @Field("password") String password);

     /*---------------------------------------- find phone & report -------------------------------------------*/

    @FormUrlEncoded
    @POST("api/report")
    Call<Report_Model> reportphone(
            @Field("user_id_fk") String user_id_fk,
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
