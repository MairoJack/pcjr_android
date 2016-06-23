package com.pcjinrong.pcjr.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Mario on 2016/5/4.
 */
public class Member implements Serializable{
    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private String realname;
    @Expose
    private String password;
    @Expose
    private String identity;
    @Expose
    private String code;
    @Expose
    private String referrer;
    @Expose
    private String mobile;
    @Expose
    private String email;
    @Expose
    private String address;
    @Expose
    private String sex;
    @Expose
    private String age;
    @Expose
    private String job;
    @Expose
    private String locked;
    @Expose
    private String identity_pic_front;
    @Expose
    private String identity_pic_back;
    @Expose
    private String regtime;
    @Expose
    private String available_balance;
    @Expose
    private String reward_amount;
    @Expose
    private String total_reward_amount;
    @Expose
    private String uncollected_capital_sum;
    @Expose
    private String uncollected_interest_sum;
    @Expose
    private String recharge_amount;
    @Expose
    private String withdraw_amount;
    @Expose
    private String earned_interest;
    @Expose
    private String gross_investment;
    @Expose
    private String seed;
    @Expose
    private String head_portrait;
    @Expose
    private String is_certification;
    @Expose
    private String uploadtime;
    @Expose
    private String verifyTimes;
    @Expose
    private String recommend_person_id;
    @Expose
    private String inner_recommend_person_id;
    @Expose
    private String anniversary_recommend_person_id;
    @Expose
    private String reward_recommend_person_id;
    @Expose
    private String is_reward;
    @Expose
    private String is_anniversary_reward;
    @Expose
    private String reward_time;
    @Expose
    private String channel;
    @Expose
    private String openid;
    @Expose
    private String has_unbinded_mobile;
    @Expose
    private String reg_ip;
    @Expose
    private String has_binded_password_protect;
    @Expose
    private String resource_reward;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setRealname(String realname){
        this.realname = realname;
    }
    public String getRealname(){
        return this.realname;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }
    public void setIdentity(String identity){
        this.identity = identity;
    }
    public String getIdentity(){
        return this.identity;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setReferrer(String referrer){
        this.referrer = referrer;
    }
    public String getReferrer(){
        return this.referrer;
    }
    public void setMobile(String mobile){
        this.mobile = mobile;
    }
    public String getMobile(){
        return this.mobile;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return this.address;
    }
    public void setSex(String sex){
        this.sex = sex;
    }
    public String getSex(){
        return this.sex;
    }
    public void setAge(String age){
        this.age = age;
    }
    public String getAge(){
        return this.age;
    }
    public void setJob(String job){
        this.job = job;
    }
    public String getJob(){
        return this.job;
    }
    public void setLocked(String locked){
        this.locked = locked;
    }
    public String getLocked(){
        return this.locked;
    }
    public void setIdentity_pic_front(String identity_pic_front){
        this.identity_pic_front = identity_pic_front;
    }
    public String getIdentity_pic_front(){
        return this.identity_pic_front;
    }
    public void setIdentity_pic_back(String identity_pic_back){
        this.identity_pic_back = identity_pic_back;
    }
    public String getIdentity_pic_back(){
        return this.identity_pic_back;
    }
    public void setRegtime(String regtime){
        this.regtime = regtime;
    }
    public String getRegtime(){
        return this.regtime;
    }
    public void setAvailable_balance(String available_balance){
        this.available_balance = available_balance;
    }
    public String getAvailable_balance(){
        return this.available_balance;
    }
    public void setReward_amount(String reward_amount){
        this.reward_amount = reward_amount;
    }
    public String getReward_amount(){
        return this.reward_amount;
    }
    public void setTotal_reward_amount(String total_reward_amount){
        this.total_reward_amount = total_reward_amount;
    }
    public String getTotal_reward_amount(){
        return this.total_reward_amount;
    }
    public void setUncollected_capital_sum(String uncollected_capital_sum){
        this.uncollected_capital_sum = uncollected_capital_sum;
    }
    public String getUncollected_capital_sum(){
        return this.uncollected_capital_sum;
    }
    public void setUncollected_interest_sum(String uncollected_interest_sum){
        this.uncollected_interest_sum = uncollected_interest_sum;
    }
    public String getUncollected_interest_sum(){
        return this.uncollected_interest_sum;
    }
    public void setRecharge_amount(String recharge_amount){
        this.recharge_amount = recharge_amount;
    }
    public String getRecharge_amount(){
        return this.recharge_amount;
    }
    public void setWithdraw_amount(String withdraw_amount){
        this.withdraw_amount = withdraw_amount;
    }
    public String getWithdraw_amount(){
        return this.withdraw_amount;
    }
    public void setEarned_interest(String earned_interest){
        this.earned_interest = earned_interest;
    }
    public String getEarned_interest(){
        return this.earned_interest;
    }
    public void setGross_investment(String gross_investment){
        this.gross_investment = gross_investment;
    }
    public String getGross_investment(){
        return this.gross_investment;
    }
    public void setSeed(String seed){
        this.seed = seed;
    }
    public String getSeed(){
        return this.seed;
    }
    public void setHead_portrait(String head_portrait){
        this.head_portrait = head_portrait;
    }
    public String getHead_portrait(){
        return this.head_portrait;
    }
    public void setIs_certification(String is_certification){
        this.is_certification = is_certification;
    }
    public String getIs_certification(){
        return this.is_certification;
    }
    public void setUploadtime(String uploadtime){
        this.uploadtime = uploadtime;
    }
    public String getUploadtime(){
        return this.uploadtime;
    }
    public void setVerifyTimes(String verifyTimes){
        this.verifyTimes = verifyTimes;
    }
    public String getVerifyTimes(){
        return this.verifyTimes;
    }
    public void setRecommend_person_id(String recommend_person_id){
        this.recommend_person_id = recommend_person_id;
    }
    public String getRecommend_person_id(){
        return this.recommend_person_id;
    }
    public void setInner_recommend_person_id(String inner_recommend_person_id){
        this.inner_recommend_person_id = inner_recommend_person_id;
    }
    public String getInner_recommend_person_id(){
        return this.inner_recommend_person_id;
    }
    public void setAnniversary_recommend_person_id(String anniversary_recommend_person_id){
        this.anniversary_recommend_person_id = anniversary_recommend_person_id;
    }
    public String getAnniversary_recommend_person_id(){
        return this.anniversary_recommend_person_id;
    }
    public void setReward_recommend_person_id(String reward_recommend_person_id){
        this.reward_recommend_person_id = reward_recommend_person_id;
    }
    public String getReward_recommend_person_id(){
        return this.reward_recommend_person_id;
    }
    public void setIs_reward(String is_reward){
        this.is_reward = is_reward;
    }
    public String getIs_reward(){
        return this.is_reward;
    }
    public void setIs_anniversary_reward(String is_anniversary_reward){
        this.is_anniversary_reward = is_anniversary_reward;
    }
    public String getIs_anniversary_reward(){
        return this.is_anniversary_reward;
    }
    public void setReward_time(String reward_time){
        this.reward_time = reward_time;
    }
    public String getReward_time(){
        return this.reward_time;
    }
    public void setChannel(String channel){
        this.channel = channel;
    }
    public String getChannel(){
        return this.channel;
    }
    public void setOpenid(String openid){
        this.openid = openid;
    }
    public String getOpenid(){
        return this.openid;
    }
    public void setHas_unbinded_mobile(String has_unbinded_mobile){
        this.has_unbinded_mobile = has_unbinded_mobile;
    }
    public String getHas_unbinded_mobile(){
        return this.has_unbinded_mobile;
    }
    public void setReg_ip(String reg_ip){
        this.reg_ip = reg_ip;
    }
    public String getReg_ip(){
        return this.reg_ip;
    }
    public void setHas_binded_password_protect(String has_binded_password_protect){
        this.has_binded_password_protect = has_binded_password_protect;
    }
    public String getHas_binded_password_protect(){
        return this.has_binded_password_protect;
    }
    public void setResource_reward(String resource_reward){
        this.resource_reward = resource_reward;
    }
    public String getResource_reward(){
        return this.resource_reward;
    }

}
