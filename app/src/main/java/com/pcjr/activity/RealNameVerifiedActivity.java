package com.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.plugins.ColoredSnackbar;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 实名认证
 * Created by Mario on 2016/5/24.
 */
public class RealNameVerifiedActivity extends Activity implements Validator.ValidationListener{
    private RelativeLayout back;
    @NotEmpty(message = "真实姓名不能为空")
    private EditText txt_realname;
    @NotEmpty(message = "身份证号码不能为空")
    private EditText txt_idcard;
    private Button btn_save;

    private Validator validator;
    private ProgressDialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realname);
        initView();
        initData();
    }

    public void initView(){
        btn_save = (Button) findViewById(R.id.btn_save);
        txt_realname = (EditText) findViewById(R.id.txt_realname);
        txt_idcard = (EditText) findViewById(R.id.txt_idcard);
        back = (RelativeLayout) findViewById(R.id.back);

        validator = new Validator(this);
        validator.setValidationListener(this);
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
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

    public void initData(){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getMemberIdentityInfo(Constant.access_token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        JsonObject data = json.get("data").getAsJsonObject();
                        txt_realname.setText(data.get("realname").getAsString());
                        txt_realname.setEnabled(false);
                        txt_idcard.setText(data.get("identity").getAsString());
                        txt_idcard.setEnabled(false);
                        btn_save.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Mario", "onFailure: "+t);
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
    public void onValidationSucceeded() {
        String realname = txt_realname.getText().toString().trim();
        String idcard = txt_idcard.getText().toString().trim();
        txt_realname.clearFocus();
        txt_idcard.clearFocus();
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.verifyIdentity(Constant.BEARER+" "+Constant.access_token,realname,idcard);
        dialog.setMessage("正在提交...");
        dialog.show();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        Snackbar snackbar = Snackbar.make(back,"实名认证成功", Snackbar.LENGTH_SHORT);
                        ColoredSnackbar.confirm(snackbar).show();

                    } else {
                        Toast.makeText(RealNameVerifiedActivity.this, json.get("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Snackbar snackbar = Snackbar.make(btn_save,"认证出错", Snackbar.LENGTH_SHORT);
                    ColoredSnackbar.warning(snackbar).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(RealNameVerifiedActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
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
}
