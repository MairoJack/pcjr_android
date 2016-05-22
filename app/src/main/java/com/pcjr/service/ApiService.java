package com.pcjr.service;

import com.google.gson.JsonObject;
import com.pcjr.model.Oauth;
import com.pcjr.model.Product;
import com.pcjr.model.Users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mario on 2016/5/4.
 */
public interface ApiService {
    @GET("/user")
    Call<Product> loadPro();

    @GET("/users/{user}")
    Call<Users> loadUsers(@Path("user") String user);

    @FormUrlEncoded
    @POST("/oauth/access_token")
    Call<JsonObject> getAccessToken(@Field("grant_type") String grant_type, @Field("username") String username, @Field("password") String password, @Field("client_id") String client_id, @Field("client_secret") String client_secret);

    @GET("/user_info")
    Call<Users> getUserInfo(@Query("access_token") String access_token);

}
