package com.pcjr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.gson.JsonObject;
import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.plugins.ColoredSnackbar;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;
import com.pcjr.utils.Validator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 绑定手机
 * Created by Mario on 2016/5/24.
 */
public class BindMobileActivity extends Activity {
    private RelativeLayout back;
    private Button btn_save,btn_checkcode;
    private EditText txt_checkcode;
    private TextView txt_mobile;
    private ApiService service;
    private TimeCount time;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_mobile);
        service = RetrofitUtils.createApi(ApiService.class);
        initView();
    }

    public void initView(){
        btn_save = (Button) findViewById(R.id.btn_save);
        txt_checkcode = (EditText) findViewById(R.id.txt_checkcode);
        txt_mobile = (TextView) findViewById(R.id.txt_mobile);
        btn_checkcode = (Button) findViewById(R.id.btn_checkcode);
        back = (RelativeLayout) findViewById(R.id.back);
        time = new TimeCount(60000, 1000);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bind();
            }
        });

        btn_checkcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendCheckCode();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    public void bind() {
        String mobile = txt_mobile.getText().toString().trim();
        String checkcode = txt_checkcode.getText().toString().trim();
        if(mobile.equals("")){
            TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content),"请输入手机号", TSnackbar.LENGTH_SHORT);
            ColoredSnackbar.warning(snackbar).show();
            return;
        }
        if(checkcode.equals("")){
            TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content),"请输入验证码", TSnackbar.LENGTH_SHORT);
            ColoredSnackbar.warning(snackbar).show();
            return;
        }
        Call<JsonObject> call = service.bindMobile(Constant.BEARER+" "+Constant.access_token, mobile, checkcode);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), json.get("message").getAsString(), TSnackbar.LENGTH_SHORT);
                        ColoredSnackbar.warning(snackbar).show();
                        finish();
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    } else {
                        TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), json.get("message").getAsString(), TSnackbar.LENGTH_SHORT);
                        ColoredSnackbar.warning(snackbar).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(BindMobileActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendCheckCode() {
        String mobile = txt_mobile.getText().toString().trim();
        if(mobile.equals("")){
            TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content),"请输入手机号", TSnackbar.LENGTH_SHORT);
            ColoredSnackbar.warning(snackbar).show();
            return;
        }
        if(!Validator.isMobile(mobile)){
            TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), "手机号格式错误", TSnackbar.LENGTH_SHORT);
            ColoredSnackbar.warning(snackbar).show();
            return;
        }
        btn_checkcode.setClickable(false);
        btn_checkcode.setBackgroundResource(R.drawable.button_gray);
        time.start();
        Call<JsonObject> call = service.bindMobileVerify(Constant.BEARER+" "+Constant.access_token,mobile);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (!json.get("success").isJsonNull() && json.get("success").getAsBoolean()) {
                        TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), json.get("message").getAsString(), TSnackbar.LENGTH_SHORT);
                        ColoredSnackbar.warning(snackbar).show();
                    } else {
                        TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), json.get("message").getAsString(), TSnackbar.LENGTH_SHORT);
                        ColoredSnackbar.warning(snackbar).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(BindMobileActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
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

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btn_checkcode.setText("发送手机验证码");
            btn_checkcode.setClickable(true);
            btn_checkcode.setBackgroundResource(R.drawable.orangebtn);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_checkcode.setText(millisUntilFinished / 1000 + "秒后可重新获取");
        }
    }
}
