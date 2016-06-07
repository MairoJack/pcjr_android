package com.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.fragment.PersonFragment;
import com.pcjr.plugins.ColoredSnackbar;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;
import com.pcjr.utils.SharedPreferenceUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登陆
 * Created by Mario on 2016/6/7.
 */
public class LoginActivity extends Activity implements View.OnClickListener, Validator.ValidationListener {

    public static final String TAG = LoginActivity.class.getSimpleName();

    private TextView reg, forget;
    @NotEmpty(message = "用户名不能为空")
    private EditText text_username;
    @NotEmpty(message = "密码不能为空")
    private EditText text_password;

    private Button but_login;

    private Validator validator;

    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);
        setContentView(R.layout.login);
        initView();
    }

    public void initView() {
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        reg = (TextView) findViewById(R.id.reg);
        forget = (TextView) findViewById(R.id.forget);
        text_username = (EditText) findViewById(R.id.username);
        text_password = (EditText) findViewById(R.id.password);

        but_login = (Button) findViewById(R.id.loginbtn);
        but_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        reg.setOnClickListener(this);
        forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.reg:
                intent = new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.forget:
                intent = new Intent(LoginActivity.this,WebViewActivity.class);
                intent.putExtra("url","https://m.pcjr.com/forgetpassword");
                intent.putExtra("title","忘记密码");
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        final String username = text_username.getText().toString().trim();
        final String password = text_password.getText().toString().trim();
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getAccessToken("password",username, password, "1", "123");
        dialog.setMessage("正在登录...");
        dialog.show();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("access_token") != null) {
                        SharedPreferenceUtil spu = new SharedPreferenceUtil(LoginActivity.this,Constant.FILE);
                        String accessToken = json.get("access_token").getAsString();
                        String refreshToken = json.get("refresh_token").getAsString();
                        Constant.access_token = accessToken;
                        Constant.isLogin = true;
                        spu.setUsername(username);
                        spu.setPassword(password);
                        spu.setAccessToken(accessToken);
                        spu.setRefresToken(refreshToken);
                        spu.setIsFirst(false);
                        dialog.dismiss();
                        String tag = getIntent().getStringExtra("tag");
                        finish();
                        if(tag!=null && tag.equals("invest")) {
                            overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);
                        }else{
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                        }
                    } else {
                        //Snackbar.make(getView(),"dsds",Snackbar.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, json.get("status_code").toString() + ":" + json.get("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    dialog.dismiss();
                    Snackbar snackbar = Snackbar.make(reg,"用户名或密码错误", Snackbar.LENGTH_SHORT);
                    ColoredSnackbar.warning(snackbar).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            String tag = getIntent().getStringExtra("tag");
            if(tag!=null && tag.equals("invest")){
                finish();
            }else{
                finish();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }

        }
        return false;

    }
}
