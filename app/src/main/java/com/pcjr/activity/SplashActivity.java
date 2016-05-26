package com.pcjr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.model.Announce;
import com.pcjr.model.FocusImg;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;
import com.pcjr.utils.SharedPreferenceUtil;

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
        ApiService service = RetrofitUtils.createApi(ApiService.class);
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
                Log.d("Mario", "onResponse:Throwable:"+t.getMessage());
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
                finish();
            }


        });
    }


}
