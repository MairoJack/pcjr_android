package com.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.plugins.IosDialog;
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
    private RelativeLayout back,realname,bindphone,changepswd,gesture;
    private Button btn_logout;
    private Switch btn_switch;
    private ProgressDialog dialog;
    private SharedPreferenceUtil spu;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_setting);

        initView();
    }

    public void initView(){
        spu = new SharedPreferenceUtil(SafeSettingActivity.this, Constant.FILE);
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        back = (RelativeLayout) findViewById(R.id.back);
        realname = (RelativeLayout) findViewById(R.id.realname);
        bindphone = (RelativeLayout) findViewById(R.id.bindphone);
        changepswd = (RelativeLayout) findViewById(R.id.changepswd);
        gesture = (RelativeLayout) findViewById(R.id.gesture);
        btn_switch = (Switch) findViewById(R.id.btn_switch);
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


        if(spu.getOpenGesture()){
            btn_switch.setChecked(true);
            gesture.setVisibility(View.VISIBLE);
        }else{
            btn_switch.setChecked(false);
            gesture.setVisibility(View.GONE);
        }
        btn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(spu.getFirstGesture()){
                        startActivityForResult(new Intent(SafeSettingActivity.this, GestureEditActivity.class),1);
                    }else {
                        spu.setOpenGesture(true);
                        gesture.setVisibility(View.VISIBLE);
                    }
                }else{
                    spu.setOpenGesture(false);
                    gesture.setVisibility(View.GONE);
                }
            }
        });
        gesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SafeSettingActivity.this, GestureEditActivity.class));
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
                    Toast.makeText(SafeSettingActivity.this,"获取手机信息失败",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(SafeSettingActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void logout(){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
                Call<JsonObject> call = service.revoke(Constant.BEARER+" "+Constant.access_token);
                dialog.setMessage("正在注销...");
                dialog.show();
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            SharedPreferenceUtil spu = new SharedPreferenceUtil(SafeSettingActivity.this,Constant.FILE);
                            spu.clear();
                            Constant.clear();
                            setResult(RESULT_FIRST_USER);
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }else{
                            IosDialog.show("注销失败",SafeSettingActivity.this);
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(SafeSettingActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            btn_switch.setChecked(true);
            spu.setOpenGesture(true);
            spu.setFirstGesture(false);
            gesture.setVisibility(View.VISIBLE);
        }else if(resultCode == RESULT_CANCELED){
            btn_switch.setChecked(false);
        }
    }
}
