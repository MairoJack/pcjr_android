package com.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.plugins.ColoredSnackbar;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;
import com.pcjr.utils.SharedPreferenceUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 安全设置
 * Created by Mario on 2016/5/24.
 */
public class SafeSettingActivity extends Activity {
    private RelativeLayout back,realname,bindphone,changepswd;
    private Button btn_logout;

    private ProgressDialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_setting);

        initView();
    }

    public void initView(){
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        back = (RelativeLayout) findViewById(R.id.back);
        realname = (RelativeLayout) findViewById(R.id.realname);
        bindphone = (RelativeLayout) findViewById(R.id.bindphone);
        changepswd = (RelativeLayout) findViewById(R.id.changepswd);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        realname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SafeSettingActivity.this, RealNameVerifiedActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        bindphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMobileInfo();

            }
        });
        changepswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SafeSettingActivity.this, ChangePasswordActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(SafeSettingActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确认注销?")
                        .setConfirmText("确认")
                        .setCancelText("取消")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                logout();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        }).
                      show();


            }
        });
    }

    public void getMobileInfo(){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getMobileInfo(Constant.access_token);
        dialog.setMessage("正在获取手机信息...");
        dialog.show();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if(json.get("success").getAsBoolean()) {
                        Intent intent = new Intent(SafeSettingActivity.this, UnbindMobileActivity.class);
                        intent.putExtra("mobile",json.get("data").getAsJsonObject().get("mobile").getAsString());
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }else {
                        startActivity(new Intent(SafeSettingActivity.this, BindMobileActivity.class));
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                }else{
                    Snackbar snackbar = Snackbar.make(back,"获取手机信息失败", Snackbar.LENGTH_SHORT);
                    ColoredSnackbar.warning(snackbar).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                Snackbar snackbar = Snackbar.make(back,"网络错误", Snackbar.LENGTH_SHORT);
                ColoredSnackbar.alert(snackbar).show();
            }
        });
    }
    public void logout(){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
                Call<JsonObject> call = service.revoke(Constant.access_token);
                dialog.setMessage("正在注销...");
                dialog.show();
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            SharedPreferenceUtil spu = new SharedPreferenceUtil(SafeSettingActivity.this,Constant.FILE);
                            spu.clear();
                            Constant.clear();
                            finish();
                            startActivity(new Intent(SafeSettingActivity.this,MainActivity.class));
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }else{
                            Snackbar snackbar = Snackbar.make(back,"注销失败", Snackbar.LENGTH_SHORT);
                            ColoredSnackbar.warning(snackbar).show();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        dialog.dismiss();
                        Snackbar snackbar = Snackbar.make(back,"网络错误", Snackbar.LENGTH_SHORT);
                        ColoredSnackbar.alert(snackbar).show();
                    }
                });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;

    }

}
