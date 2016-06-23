package com.pcjinrong.pcjr.service;

import com.google.gson.JsonObject;
import com.pcjinrong.pcjr.model.FinanceRecords;
import com.pcjinrong.pcjr.model.Users;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Mario on 2016/5/4.
 */
public interface ApiService {

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
    @POST("oauth/access_token")
    Call<JsonObject> getAccessToken(@Field("grant_type") String grant_type, @Field("username") String username, @Field("password") String password, @Field("client_id") String client_id, @Field("client_secret") String client_secret);


    /**
     * 刷新token
     * @param grant_type
     * @param refresh_token
     * @param client_id
     * @param client_secret
     * @return
     */
    @FormUrlEncoded
    @POST("oauth/access_token")
    Call<JsonObject> refreshToken(@Field("grant_type") String grant_type, @Field("refresh_token") String refresh_token, @Field("client_id") String client_id, @Field("client_secret") String client_secret);


    /**
     * 用户登出
     * @param header
     * @return
     */
    @POST("oauth/revoke_access_token")
    Call<JsonObject> revoke(@Header("Authorization") String header);

    /**
     * 获取首页焦点图和公告
     * @return
     */
    @GET("index_focus_info")
    Call<JsonObject> getIndexFocusInfo();

    /**
     * 获取首页产品信息
     * @return
     */
    @GET("index_product_list")
    Call<JsonObject> getIndexProductList();

    /**
     * 获取产品列表
     * @param type: 0 全部；1 销售中；2 销售结束；3 项目结束
     * @param page
     * @param page_size
     * @return
     */
    @GET("invest_product_list")
    Call<JsonObject> getInvestProductList(@Query("type") int type,@Query("page") int page,@Query("page_size") int page_size);

    /**
     * 获取产品详情
     * @param id
     * @return
     */
    @GET("product_detail")
    Call<JsonObject> getProductDetail(@Query("id") String id);

    /**
     * 获取产品投资记录
     * @param id
     * @return
     */
    @GET("product_trading_record_list")
    Call<JsonObject> getProductTradingRecordList(@Query("id") String id,@Query("page") int page,@Query("page_size") int page_size);

    /**
     * 用户注册
     * @param name
     * @param password
     * @param recommend_person
     * @return
     */
    @FormUrlEncoded
    @POST("register")
    Call<JsonObject> register(@Field("name") String name,@Field("password") String password,@Field("recommend_person") String recommend_person);


    /**
     * 获取用户信息
     * @param access_token
     * @return
     */
    @GET("user_info")
    Call<Users> getUserInfo(@Query("access_token") String access_token);

    /**
     * 获取用户中心首页数据
     * @param access_token
     * @return
     */
    @GET("member/index_data")
    Call<JsonObject> getMemberIndex(@Query("access_token") String access_token);



    /**
     * 获取用户交易记录
     * @return
     */
    @GET("member/log_data")
    Call<JsonObject> getMemberLogData(@Query("access_token") String access_token,@Query("type") int type,@Query("page") int page,@Query("page_size") int page_size);

    /**
     * 获取用户投资记录
     * @return
     */
    @GET("member/invest_data")
    Call<JsonObject> getMemberInvestData(@Query("access_token") String access_token,@Query("type") int type,@Query("page") int page,@Query("page_size") int page_size);

    /**
     * 获取用户资金记录
     * @return
     */
    @GET("member/finance_data")
    Call<FinanceRecords> getMemberFinanceData(@Query("access_token") String access_token);

    /**
     *
     * 获取用户回款计划
     * @param access_token
     * @param year  年
     * @param month 月
     * @return
     */
    @GET("member/current_month_repayment_data")
    Call<JsonObject> getMemberRepaymentData(@Query("access_token") String access_token, @Query("year") int year, @Query("month") int month,@Query("page") int page,@Query("page_size") int page_size);

    /**
     * 实名认证
     * @param header
     * @return
     */
    @FormUrlEncoded
    @POST("member/verify_identity")
    Call<JsonObject> verifyIdentity(@Header("Authorization") String header,@Field("realname") String realname,@Field("identity") String identity);

    /**
     * 获取实名认证信息
     * @param access_token
     * @return
     */
    @GET("member/identity_info")
    Call<JsonObject> getMemberIdentityInfo(@Query("access_token") String access_token);

    /**
     * 获取用户银行卡信息
     * @param access_token
     * @return
     */
    @GET("member/bank_card_info")
    Call<JsonObject> getMemberBankCardInfo(@Query("access_token") String access_token);

    /**
     * 获取银行卡信息
     * @return
     */
    @GET("bank_card_list")
    Call<JsonObject> getBankCardList();

    /**
     * 添加银行卡
     * @param header
     * @param bank_id    银行ID
     * @param card_no    银行卡号
     * @param real_name  真实姓名
     * @return
     */
    @FormUrlEncoded
    @POST("member/add_bank_card")
    Call<JsonObject> addBankCard(@Header("Authorization") String header, @Field("bank_id") String bank_id, @Field("card_no") String card_no, @Field("real_name") String real_name);

    /**
     * 删除银行卡
     * @param header
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("member/del_bank_card")
    Call<JsonObject> delBankCard(@Header("Authorization") String header, @Field("id") String id);

    /**
     * 发送绑定验证码
     * @param header
     * @param mobile
     * @return
     */
    @FormUrlEncoded
    @POST("api/bind_mobile_verify")
    Call<JsonObject> bindMobileVerify(@Header("Authorization") String header, @Field("mobile") String mobile);

    /**
     * 发送解绑验证码
     * @param header
     * @return
     */
    @POST("api/unbind_mobile_verify")
    Call<JsonObject> unbindMobileVerify(@Header("Authorization") String header);

    /**
     * 绑定手机
     * @param header
     * @param mobile
     * @param verify
     * @return
     */
    @FormUrlEncoded
    @POST("member/bind_mobile")
    Call<JsonObject> bindMobile(@Header("Authorization") String header,@Field("mobile") String mobile, @Field("verify") String verify);

    /**
     * 解绑手机
     * @param header
     * @param verify
     * @return
     */
    @FormUrlEncoded
    @POST("member/unbind_mobile")
    Call<JsonObject> unbind_mobile(@Header("Authorization") String header, @Field("verify") String verify);

    /**
     * 获取用户手机信息
     * @param access_token
     * @return
     */
    @GET("member/mobile_info")
    Call<JsonObject> getMobileInfo(@Query("access_token") String access_token);

    /**
     * 修改密码
     * @param header
     * @param old_password
     * @param new_password
     * @return
     */
    @FormUrlEncoded
    @POST("member/change_password")
    Call<JsonObject> changePassword(@Header("Authorization") String header,@Field("old_password") String old_password, @Field("new_password") String new_password);

    /**
     * 获取站内信列表
     * @param access_token
     * @param page
     * @return
     */
    @GET("member/letter_list")
    Call<JsonObject> getLetterList(@Query("access_token") String access_token,@Query("page") int page,@Query("page_size") int page_size);

    /**
     * 获取站内信详情
     * @param access_token
     * @param id
     * @return
     */
    @GET("member/letter_detail")
    Call<JsonObject> getLetterDetail(@Query("access_token") String access_token,@Query("id") String id);

    /**
     * 获取用户未使用的优惠券数量
     * @param access_token
     * @return
     */
    @GET("member/unused_coupons_num")
    Call<JsonObject> getUnusedCouponsNum(@Query("access_token") String access_token);

    /**
     * 获取用户投资券列表
     * @param access_token
     * @param type 0 未使用; 1 已使用; 2 已过期;
     * @param page
     * @return
     */
    @GET("member/invest_ticket_list")
    Call<JsonObject> getInvestTicketList(@Query("access_token") String access_token,@Query("type") int type,@Query("page") int page,@Query("page_size") int pageSize);

    /**
     * 获取用户投资券详情
     * @param access_token
     * @param id
     * @return
     */
    @GET("member/invest_ticket_detail")
    Call<JsonObject> getInvestTicketDetail(@Query("access_token") String access_token,@Query("id") String id);

    /**
     *
     * @param access_token
     * @param type 0 未领取; 1 已领取;
     * @param page
     * @return
     */
    @GET("member/red_packet_list")
    Call<JsonObject> getRedPacketList(@Query("access_token") String access_token,@Query("type") int type,@Query("page") int page,@Query("page_size") int pageSize);

    /**
     * 获取用户提现/投资信息
     * @param access_token
     * @return
     */
    @GET("member/withdraw_invest_info")
    Call<JsonObject> getWithdrawInvestInfo(@Query("access_token") String access_token);

    /**
     * 用户提现
     * @param header
     * @param amount    提现金额
     * @param bank_id   银行ID
     * @param verify    验证码
     * @return
     */
    @FormUrlEncoded
    @POST("member/withdraw")
    Call<JsonObject> withdraw(@Header("Authorization") String header,@Field("amount") String amount, @Field("bank_id") String bank_id,@Field("verify") String verify);

    /**
     * 发送提现验证码
     * @param header
     * @return
     */
    @POST("api/withdraw_verify")
    Call<JsonObject> withdrawVerify(@Header("Authorization") String header);

    /**
     * 投资
     * @param access_token
     * @param amount    投资金额
     * @param id        产品ID
     * @param password  密码
     * @return
     */
    @FormUrlEncoded
    @POST("member/invest_product")
    Call<JsonObject> investProduct(@Header("Authorization") String header,@Field("access_token") String access_token,@Field("amount") String amount, @Field("id") String id,@Field("password") String password);

    /**
     * 领取红包
     * @param header
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("member/get_red_packet_reward")
    Call<JsonObject> getRedPacketReward(@Header("Authorization") String header, @Field("id") String id);

    /**
     * 绑定设备
     * @param header
     * @param device_token  设备号
     * @return
     */
    @FormUrlEncoded
    @POST("member/refresh_device_token")
    Call<JsonObject> refreshDeviceToken(@Header("Authorization") String header, @Field("device_token") String device_token);
}
