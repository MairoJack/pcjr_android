package com.pcjinrong.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.pcjinrong.pcjr.plugins.IosDialog;
import com.pcjinrong.pcjr.utils.SharedPreferenceUtil;
import com.pcjr.R;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.plugins.AlertView;
import com.pcjinrong.pcjr.service.ApiService;
import com.pcjinrong.pcjr.utils.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登陆
 * Created by Mario on 2016/6/7.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    public static final String TAG = LoginActivity.class.getSimpleName();

    private TextView reg, forget,index;
    private EditText text_username;
    private EditText text_password;
    private Button but_login;
    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
    }

    public void initView() {
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        index = (TextView) findViewById(R.id.index);
        reg = (TextView) findViewById(R.id.reg);
        forget = (TextView) findViewById(R.id.forget);
        text_username = (EditText) findViewById(R.id.username);
        text_password = (EditText) findViewById(R.id.password);

        but_login = (Button) findViewById(R.id.loginbtn);
        but_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        reg.setOnClickListener(this);
        forget.setOnClickListener(this);
        index.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.index:
                setResult(2);
                finish();
                break;
            case R.id.reg:
                intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.forget:
                intent = new Intent(LoginActivity.this, WebViewActivity.class);
                intent.putExtra("url", "https://m.pcjinrong.pcjinrong.com.pcjinrong.com.pcjinrong.pcjr.com/forgetpassword");
                intent.putExtra("title", "忘记密码");
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    public void login() {
        final String username = text_username.getText().toString().trim();
        final String password = text_password.getText().toString().trim();
        if (username.equals("")) {
            IosDialog.show("用户名不能为空",this);
            return;
        }
        if (password.equals("")) {
            new AlertView("密码不能为空", null, null, new String[]{"好"}, null, this,
                    AlertView.Style.Alert, null).show();
            return;
        }
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getAccessToken("password", username, password, Constant.CLIENTID, Constant.CLIENTSECRET);
        dialog.setMessage("正在登录...");
        dialog.show();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("access_token") != null) {
                        //绑定设备号
                        bindDevice();
                        SharedPreferenceUtil spu = new SharedPreferenceUtil(LoginActivity.this, Constant.FILE);
                        String accessToken = json.get("access_token").getAsString();
                        String refreshToken = json.get("refresh_token").getAsString();
                        Constant.access_token = accessToken;
                        Constant.refresh_token = refreshToken;
                        Constant.isLogin = true;
                        spu.setUsername(username);
                        spu.setPassword(password);
                        spu.setAccessToken(accessToken);
                        spu.setRefresToken(refreshToken);
                        spu.setIsFirst(false);
                        String tag = getIntent().getStringExtra("tag");
                        if (tag != null && tag.equals("invest")) {
                            overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);
                        } else {
                            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            spu.setFirstGesture(true);
                            spu.setOpenGesture(false);
                            spu.setGesture("");
                            setResult(RESULT_OK, new Intent());
                            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                        }
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, json.get("status_code").toString() + ":" + json.get("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    IosDialog.show("用户名或密码错误",LoginActivity.this);
                }
                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;

    }

    public void bindDevice() {
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.refreshDeviceToken(Constant.BEARER + " " + Constant.access_token, Constant.DEVICETOKEN);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        Log.i("pcjinrong.pcjinrong.com.pcjinrong.com.pcjinrong.pcjr", "refreshDeviceToken:onResponse:" + json.get("message").getAsString());
                    } else {
                        Log.i("pcjinrong.pcjinrong.com.pcjinrong.com.pcjinrong.pcjr", "refreshDeviceToken:onResponse:" + json.get("message").getAsString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("pcjinrong.pcjinrong.com.pcjinrong.com.pcjinrong.pcjr", "refreshDeviceToken:onResponse:网络异常");
            }
        });
    }
}
