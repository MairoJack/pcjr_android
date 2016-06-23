package com.pcjinrong.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.plugins.IosDialog;
import com.pcjinrong.pcjr.service.ApiService;
import com.pcjinrong.pcjr.utils.SharedPreferenceUtil;
import com.pcjr.R;
import com.pcjinrong.pcjr.utils.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 修改密码
 * Created by Mario on 2016/5/24.
 */
public class ChangePasswordActivity extends Activity {
    private RelativeLayout back;
    private EditText txt_old_password;
    private EditText txt_password;
    private EditText txt_confirm_password;

    private Button btn_save;

    private ProgressDialog dialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        initView();
    }

    public void initView(){
        txt_old_password = (EditText) findViewById(R.id.txt_old_password);
        txt_password = (EditText) findViewById(R.id.txt_password);
        txt_confirm_password = (EditText) findViewById(R.id.txt_confirm_password);
        btn_save = (Button) findViewById(R.id.btn_save);
        back = (RelativeLayout) findViewById(R.id.back);
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
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

    public void change() {
        final String oldPassword = txt_old_password.getText().toString().trim();
        final String newPassword = txt_password.getText().toString().trim();
        final String confirm_password = txt_confirm_password.getText().toString().trim();
        if (oldPassword.equals("")) {
            IosDialog.show("原密码不能为空",this);
            return;
        }
        if (newPassword.equals("")) {
            IosDialog.show("新密码不能为空",this);
            return;
        }
        if (confirm_password.equals("")) {
            IosDialog.show("确认密码不能为空",this);
            return;
        }
        if (!newPassword.equals(confirm_password)) {
            IosDialog.show("两次密码不相同",this);
            return;
        }
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.changePassword(Constant.BEARER+" "+Constant.access_token,oldPassword,newPassword);
        dialog.setMessage("正在提交...");
        dialog.show();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        SharedPreferenceUtil spu = new SharedPreferenceUtil(ChangePasswordActivity.this,Constant.FILE);
                        spu.setPassword(newPassword);
                        Toast.makeText(ChangePasswordActivity.this,json.get("message").getAsString(),Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    } else {
                        IosDialog.show(json.get("message").getAsString(),ChangePasswordActivity.this);              }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
               Toast.makeText(ChangePasswordActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
