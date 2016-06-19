package com.pcjr.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.gson.JsonObject;
import com.pcjr.common.Constant;
import com.pcjr.utils.RetrofitUtils;
import java.util.Timer;
import java.util.TimerTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 刷新 Token Service
 * Created by Mario on 16/6/19.
 */
public class RefreshTokenService extends Service {
    private Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!Constant.refresh_token.equals("")) refreshToken();
            }
        }, 0, 3000 * 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    private void refreshToken() {
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.refreshToken("refresh_token",Constant.refresh_token,Constant.CLIENTID,Constant.CLIENTSECRET);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    String accessToken = json.get("access_token").getAsString();
                    String refreshToken = json.get("refresh_token").getAsString();
                    Constant.access_token = accessToken;
                    Constant.refresh_token = refreshToken;
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("pcjr", "onFailure: refreshToken:"+t.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
        }
    }
}
