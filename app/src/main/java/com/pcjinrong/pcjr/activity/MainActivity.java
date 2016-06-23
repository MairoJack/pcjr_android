package com.pcjinrong.pcjr.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.pcjinrong.pcjr.adapter.FragmentAdapter;
import com.pcjinrong.pcjr.plugins.FragmentNavigator;
import com.pcjinrong.pcjr.plugins.IosDialog;
import com.pcjinrong.pcjr.utils.SharedPreferenceUtil;
import com.pcjr.R;
import com.pcjinrong.pcjr.action.UpdateManager;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.plugins.BottomNavigatorView;
import com.pcjinrong.pcjr.service.ApiService;
import com.pcjinrong.pcjr.service.RefreshTokenService;
import com.pcjinrong.pcjr.utils.RetrofitUtils;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FragmentActivity implements BottomNavigatorView.OnBottomNavigatorViewItemClickListener {

    private static final int DEFAULT_POSITION = 0;

    private FragmentNavigator mNavigator;

    // a simple custom bottom navigation view
    private BottomNavigatorView bottomNavigatorView;
    private int last_position = 0;
    private long exitTime = 0;

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

        // 检查软件更新
        UpdateManager manager = new UpdateManager(MainActivity.this);
        manager.check();

        // 信鸽
        XGAction();
        tryLogin();
        startService(new Intent(this, RefreshTokenService.class));


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
                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), Constant.REQUSET);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else if (!Constant.isGestureLogin) {
                SharedPreferenceUtil spu = new SharedPreferenceUtil(MainActivity.this, Constant.FILE);
                if(spu.getOpenGesture()) {
                    startActivityForResult(new Intent(MainActivity.this, GestureVerifyActivity.class), Constant.REQUSET);
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
        if (requestCode == Constant.REQUSET && resultCode == RESULT_OK) {
            setCurrentTab(2);
        }else if (requestCode == Constant.REQUSET && resultCode == RESULT_FIRST_USER){
            IosDialog.show(data.getStringExtra("error"),this);
        }else if(requestCode == Constant.SAFESETTING && resultCode == RESULT_FIRST_USER){
            setCurrentTab(0);
        }else if(requestCode == Constant.REQUSET && resultCode == 2){
            setCurrentTab(0);
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
                            Constant.refresh_token = refreshToken;
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
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this,RefreshTokenService.class));
    }
}