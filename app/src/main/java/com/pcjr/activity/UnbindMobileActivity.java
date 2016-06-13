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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjr.R;
import com.pcjr.adapter.BankCardListViewAdapter;
import com.pcjr.common.Constant;
import com.pcjr.model.BankCard;
import com.pcjr.plugins.ColoredSnackbar;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 绑定手机
 * Created by Mario on 2016/5/24.
 */
public class UnbindMobileActivity extends Activity {
    private RelativeLayout back;
    private Button btn_save, btn_checkcode;
    private TextView txt_old_mobile;
    private EditText txt_checkcode;
    private ApiService service;
    private TimeCount time;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unbind_mobile);
        initView();
        initData();
    }

    public void initView() {
        txt_old_mobile = (TextView) findViewById(R.id.txt_old_mobile);
        txt_checkcode = (EditText) findViewById(R.id.txt_checkcode);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_checkcode = (Button) findViewById(R.id.btn_checkcode);
        back = (RelativeLayout) findViewById(R.id.back);
        time = new TimeCount(60000, 1000);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbind();
            }
        });

        btn_checkcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_checkcode.setClickable(false);
                btn_checkcode.setBackgroundResource(R.drawable.button_gray);
                time.start();
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

    public void initData() {
        service = RetrofitUtils.createApi(ApiService.class);
        String oldMobile = getIntent().getStringExtra("mobile");
        txt_old_mobile.setText(oldMobile);
    }

    public void unbind() {
        String checkcode = txt_checkcode.getText().toString().trim();
        if(checkcode.equals("")){
            TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content),"请输入验证码", TSnackbar.LENGTH_SHORT);
            ColoredSnackbar.warning(snackbar).show();
            return;
        }
        Call<JsonObject> call = service.unbind_mobile(Constant.BEARER+" "+Constant.access_token, checkcode);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        finish();
                        startActivity(new Intent(UnbindMobileActivity.this, BindMobileActivity.class));
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    } else {
                        TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), json.get("message").getAsString(), TSnackbar.LENGTH_SHORT);
                        ColoredSnackbar.warning(snackbar).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(UnbindMobileActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendCheckCode() {
        Call<JsonObject> call = service.unbindMobileVerify(Constant.BEARER+" "+Constant.access_token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (!json.get("success").isJsonNull() && json.get("success").getAsBoolean()) {
                        TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), json.get("message").getAsString(), TSnackbar.LENGTH_SHORT);
                        ColoredSnackbar.confirm(snackbar).show();
                    } else {
                        TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), json.get("message").getAsString(), TSnackbar.LENGTH_SHORT);
                        ColoredSnackbar.warning(snackbar).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(UnbindMobileActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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
        public void onFinish() {// 计时完毕
            btn_checkcode.setText("获取验证码");
            btn_checkcode.setClickable(true);
            btn_checkcode.setBackgroundResource(R.drawable.orangebtn);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_checkcode.setText(millisUntilFinished / 1000 + "秒后可重新获取");
        }
    }
}
