package com.pcjinrong.pcjr.action;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.service.ApiService;
import com.pcjinrong.pcjr.utils.RetrofitUtils;
import com.pcjinrong.pcjr.utils.SharedPreferenceUtil;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

/**
 * Created by Mario on 2016/7/14.
 */
public class TokenAuthenticator implements Authenticator {


    @Override
    public Request authenticate(Route route, Response response) throws IOException {

        ApiService service = RetrofitUtils.createRefreshApi(ApiService.class);
        Call<JsonObject> call = service.refreshToken("refresh_token", Constant.refresh_token, Constant.CLIENTID, Constant.CLIENTSECRET);
        JsonObject json = call.execute().body();


        if (responseCount(response) >= 3) {
            return null;
        }

        if (json != null) {
            String accessToken = json.get("access_token").getAsString();
            String refreshToken = json.get("refresh_token").getAsString();
            Constant.access_token = accessToken;
            Constant.refresh_token = refreshToken;
            return response.request().newBuilder().header("Authorization", Constant.BEARER + " " + accessToken)
            .build();
        } else {
            throw new IOException("12");
        }
    }

    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}
