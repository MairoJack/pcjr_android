package com.pcjinrong.pcjr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.model.Announce;
import com.pcjinrong.pcjr.model.FocusImg;
import com.pcjinrong.pcjr.service.ApiService;
import com.pcjinrong.pcjr.utils.RetrofitUtils;
import com.pcjinrong.pcjr.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 启动屏
 * Created by Mario on 2016/5/24.
 */
public class SplashActivity extends Activity {

    private Constant constant;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        constant = (Constant) getApplication();
        init();
    }

    private void init() {
        ApiService service = RetrofitUtils.createNoTokenApi(ApiService.class);
        Call<JsonObject> call = service.getIndexFocusInfo();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    List<FocusImg> focusImgs = null;
                    List<FocusImg> midFocusImgs = null;
                    List<Announce> announces = null;
                    int mCounter = 0;
                    if (json.get("top_focus_img") != null) {
                        focusImgs = gson.fromJson(json.get("top_focus_img"), new TypeToken<List<FocusImg>>() {
                        }.getType());
                    }
                    if (json.get("middle_focus_img") != null) {
                        midFocusImgs = gson.fromJson(json.get("middle_focus_img"), new TypeToken<List<FocusImg>>() {
                        }.getType());
                    }
                    if (json.get("announce") != null) {
                        announces = gson.fromJson(json.get("announce"), new TypeToken<List<Announce>>() {
                        }.getType());
                        mCounter = announces.size();
                    }
                    constant.setFocusImgs(focusImgs);
                    constant.setMidFocusImgs(midFocusImgs);
                    constant.setAnnounces(announces);
                    constant.setmCounter(mCounter);
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("pcjr", "onResponse:Throwable:"+t.getMessage());
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
                finish();
            }


        });
    }


}
