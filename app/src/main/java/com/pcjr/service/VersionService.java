package com.pcjr.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Mario on 2016/6/14.
 */
public interface VersionService {
    @GET("androidVersion/getNewstVersion")
    Call<JsonObject> getNewstVersion();
}
