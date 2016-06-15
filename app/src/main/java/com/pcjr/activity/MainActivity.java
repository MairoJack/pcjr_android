package com.pcjr.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.pcjr.R;
import com.pcjr.adapter.FragmentAdapter;
import com.pcjr.common.Constant;
import com.pcjr.model.PaymentPlan;
import com.pcjr.plugins.BottomNavigatorView;
import com.pcjr.plugins.FragmentNavigator;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;
import com.pcjr.utils.SharedPreferenceUtil;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FragmentActivity implements BottomNavigatorView.OnBottomNavigatorViewItemClickListener {

    private static final int DEFAULT_POSITION = 0;
    public static final int REQUSET = 1;
    private FragmentNavigator mNavigator;

    // a simple custom bottom navigation view
    private BottomNavigatorView bottomNavigatorView;
    private int last_position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigator = new FragmentNavigator(getSupportFragmentManager(), new FragmentAdapter(), R.id.container);
        // set default tab position
        mNavigator.setDefaultPosition(DEFAULT_POSITION);
        mNavigator.onCreate(savedInstanceState);

        bottomNavigatorView = (BottomNavigatorView) findViewById(R.id.bottomNavigatorView);
        bottomNavigatorView.setOnBottomNavigatorViewItemClickListener(this);

        setCurrentTab(mNavigator.getCurrentPosition());
        // 信鸽
        XGAction();
        tryLogin();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigator.onSaveInstanceState(outState);
    }

    @Override
    public void onBottomNavigatorViewItemClick(int position, View view) {
        Constant.TYPE = 0;
        if (position == 2) {
            if (Constant.isLogin && Constant.isGestureLogin) {
                setCurrentTab(position);
            } else if (!Constant.isLogin) {
                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), REQUSET);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else if (!Constant.isGestureLogin) {
                SharedPreferenceUtil spu = new SharedPreferenceUtil(MainActivity.this, Constant.FILE);
                if(spu.getOpenGesture()) {
                    startActivityForResult(new Intent(MainActivity.this, GestureVerifyActivity.class), REQUSET);
                }else{
                    setCurrentTab(position);
                }
            }else{
                setCurrentTab(position);
            }
        }else{
            setCurrentTab(position);
        }
    }

    public void setCurrentTab(int position) {
        mNavigator.showFragment(position);
        bottomNavigatorView.select(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUSET && resultCode == RESULT_OK) {
            setCurrentTab(2);
            Log.d("Mario", "onActivityResult: " + data.getStringExtra("abc"));
        }
    }

    private void tryLogin() {
        final SharedPreferenceUtil spu = new SharedPreferenceUtil(this, Constant.FILE);
        if (!spu.getisFirst()) {
            ApiService service = RetrofitUtils.createApi(ApiService.class);
            Call<JsonObject> call = service.getAccessToken("password", spu.getUsernam(), spu.getPassword(), Constant.CLIENTID, Constant.CLIENTSECRET);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        JsonObject json = response.body();
                        if (json.get("access_token") != null) {
                            String accessToken = json.get("access_token").getAsString();
                            String refreshToken = json.get("refresh_token").getAsString();
                            spu.setAccessToken(accessToken);
                            spu.setRefresToken(refreshToken);
                            Constant.isLogin = true;
                            Constant.access_token = accessToken;
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("Mario", "onResponse:Throwable:" + t);
                }
            });
        }
    }

    /**
     * 开启信鸽推送
     */
    public void XGAction() {
        Context context = getApplicationContext();
        XGPushManager.registerPush(context);

        Intent service = new Intent(context, XGPushService.class);
        context.startService(service);

        Constant.DEVICETOKEN = XGPushConfig.getToken(context);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Mario", "onPause: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Mario", "onRestart: ");
    }
}