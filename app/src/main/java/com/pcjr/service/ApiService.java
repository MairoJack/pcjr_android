package com.pcjr.service;

import com.google.gson.JsonObject;
import com.pcjr.model.FinanceRecords;
import com.pcjr.model.Product;
import com.pcjr.model.PaymentPlan;
import com.pcjr.model.Users;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
     * 获取访问token(用户登录)
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
     * 用户登出
     * @param access_token
     * @return
     */
    @FormUrlEncoded
    @POST("/oauth/revoke_access_token")
    Call<JsonObject> revoke(@Field("access_token") String access_token);


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
    @GET("/member/index_data")
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
    @GET("/member/log_data")
    Call<JsonObject> getMemberLogData(@Query("access_token") String access_token);

    /**
     * 获取用户投资记录
     * @return
     */
    @GET("/member/invest_data")
    Call<JsonObject> getMemberInvestData(@Query("access_token") String access_token);

    /**
     * 获取用户资金记录
     * @return
     */
    @GET("/member/finance_data")
    Call<FinanceRecords> getMemberFinanceData(@Query("access_token") String access_token);

    /**
     *
     * 获取用户回款计划
     * @param access_token
     * @param year  年
     * @param month 月
     * @return
     */
    @GET("/member/current_month_repayment_data")
    Call<JsonObject> getMemberRepaymentData(@Query("access_token") String access_token, @Query("year") int year, @Query("month") int month);

    /**
     * 实名认证
     * @param header
     * @return
     */
    @POST("/member/verify_identity")
    Call<JsonObject> verifyIdentity(@Header("Authorization") String header,@Header("realname") String realname,@Header("identity") String identity);

    /**
     * 获取实名认证信息
     * @param access_token
     * @return
     */
    @GET("/member/identity_info")
    Call<JsonObject> getMemberIdentityInfo(@Query("access_token") String access_token);

    /**
     * 获取用户银行卡信息
     * @param access_token
     * @return
     */
    @GET("/member/bank_card_info")
    Call<JsonObject> getMemberBankCardInfo(@Query("access_token") String access_token);

    /**
     * 获取银行卡信息
     * @return
     */
    @GET("/bank_card_list")
    Call<JsonObject> getBankCardList();

    /**
     * 添加银行卡
     * @param access_token
     * @param bank_id    银行ID
     * @param card_no    银行卡号
     * @param real_name  真实姓名
     * @return
     */
    @FormUrlEncoded
    @POST("/member/add_bank_card")
    Call<JsonObject> addBankCard(@Field("access_token") String access_token, @Field("bank_id") String bank_id, @Field("card_no") String card_no, @Field("real_name") String real_name);

    /**
     * 删除银行卡
     * @param access_token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/member/del_bank_card")
    Call<JsonObject> delBankCard(@Field("access_token") String access_token, @Field("id") String id);

    /**
     * 发送绑定验证码
     * @param access_token
     * @param mobile
     * @return
     */
    @FormUrlEncoded
    @POST("/api/bind_mobile_verify")
    Call<JsonObject> bindMobileVerify(@Field("access_token") String access_token, @Field("mobile") String mobile);

    /**
     * 发送解绑验证码
     * @param access_token
     * @return
     */
    @FormUrlEncoded
    @POST("/api/unbind_mobile_verify")
    Call<JsonObject> unbindMobileVerify(@Field("access_token") String access_token);

    /**
     * 绑定手机
     * @param access_token
     * @param mobile
     * @param verify
     * @return
     */
    @FormUrlEncoded
    @POST("/member/bind_mobile")
    Call<JsonObject> bindMobile(@Field("access_token") String access_token,@Field("mobile") String mobile, @Field("verify") String verify);

    /**
     * 解绑手机
     * @param access_token
     * @param mobile
     * @param verify
     * @return
     */
    @FormUrlEncoded
    @POST("/member/unbind_mobile")
    Call<JsonObject> unbind_mobile(@Field("access_token") String access_token,@Field("mobile") String mobile, @Field("verify") String verify);

    /**
     * 获取用户手机信息
     * @param access_token
     * @return
     */
    @GET("/member/mobile_info")
    Call<JsonObject> getMobileInfo(@Query("access_token") String access_token);

    /**
     * 修改密码
     * @param access_token
     * @param old_password
     * @param new_password
     * @return
     */
    @FormUrlEncoded
    @POST("/member/change_password")
    Call<JsonObject> changePassword(@Field("access_token") String access_token,@Field("old_password") String old_password, @Field("new_password") String new_password);

    /**
     * 获取站内信列表
     * @param access_token
     * @param type
     * @param page
     * @return
     */
    @GET("/member/letter_list")
    Call<JsonObject> getLetterList(@Query("access_token") String access_token,@Query("type") int type,@Query("page") int page);

    /**
     * 获取站内信详情
     * @param access_token
     * @param id
     * @return
     */
    @GET("/member/letter_detail")
    Call<JsonObject> getLetterDetail(@Query("access_token") String access_token,@Query("id") String id);

}
