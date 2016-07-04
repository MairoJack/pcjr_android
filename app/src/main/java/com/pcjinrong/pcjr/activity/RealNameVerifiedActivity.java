package com.pcjinrong.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.pcjinrong.pcjr.plugins.IosDialog;
import com.pcjinrong.pcjr.R;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.service.ApiService;
import com.pcjinrong.pcjr.utils.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 实名认证
 * Created by Mario on 2016/5/24.
 */
public class RealNameVerifiedActivity extends Activity {
    private RelativeLayout back;
    private EditText txt_realname;
    private EditText txt_idcard;
    private Button btn_save;

    private ProgressDialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realname);
        initView();
        initData();
    }

    public void initView() {
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        btn_save = (Button) findViewById(R.id.btn_save);
        txt_realname = (EditText) findViewById(R.id.txt_realname);
        txt_idcard = (EditText) findViewById(R.id.txt_idcard);
        back = (RelativeLayout) findViewById(R.id.back);


        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
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
        dialog.setMessage("正在加载...");
        dialog.show();
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
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(RealNameVerifiedActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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

    public void verify() {
        String realname = txt_realname.getText().toString().trim();
        String idcard = txt_idcard.getText().toString().trim();
        txt_realname.clearFocus();
        txt_idcard.clearFocus();
        if (realname.equals("")) {
            IosDialog.show("真实姓名不能为空", this);
            return;
        }
        if (idcard.equals("")) {
            IosDialog.show("身份证号码不能为空", this);
            return;
        }
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.verifyIdentity(Constant.BEARER + " " + Constant.access_token, realname, idcard);
        dialog.setMessage("正在提交...");
        dialog.show();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        Toast.makeText(RealNameVerifiedActivity.this, "实名认证成功", Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    } else {
                        if(json.get("message").isJsonNull()){
                            IosDialog.show("实名认证失败,请联系客服", RealNameVerifiedActivity.this);
                        }else {
                            IosDialog.show(json.get("message").getAsString(), RealNameVerifiedActivity.this);
                        }
                    }
                } else {
                    IosDialog.show("认证出错", RealNameVerifiedActivity.this);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(RealNameVerifiedActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
