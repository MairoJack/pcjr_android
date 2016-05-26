package com.pcjr.service;

import com.google.gson.JsonObject;
import com.pcjr.model.FinanceRecords;
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

    /**
     * 获取访问token
     * @param grant_type
     * @param username
     * @param password
     * @param client_id
     * @param client_secret
     * @return
     */
    @FormUrlEncoded
    @POST("/oauth/access_token")
    Call<JsonObject> getAccessToken(@Field("grant_type") String grant_type, @Field("username") String username, @Field("password") String password, @Field("client_id") String client_id, @Field("client_secret") String client_secret);

    /**
     * 获取用户信息
     * @param access_token
     * @return
     */
    @GET("/user_info")
    Call<Users> getUserInfo(@Query("access_token") String access_token);

    /**
     * 获取用户中心首页数据
     * @param access_token
     * @return
     */
    @GET("/member_index_data")
    Call<JsonObject> getMemberIndex(@Query("access_token") String access_token);

    /**
     * 获取首页焦点图和公告
     * @return
     */
    @GET("/index_focus_info")
    Call<JsonObject> getIndexFocusInfo();

    /**
     * 获取用户交易记录
     * @return
     */
    @GET("/member_log_data")
    Call<JsonObject> getMemberLogData(@Query("access_token") String access_token);

    /**
     * 获取用户投资记录
     * @return
     */
    @GET("/member_invest_data")
    Call<JsonObject> getMemberInvestData(@Query("access_token") String access_token);

    /**
     * 获取用户资金记录
     * @return
     */
    @GET("/member_finance_data")
    Call<FinanceRecords> getMemberFinanceData(@Query("access_token") String access_token);

}
