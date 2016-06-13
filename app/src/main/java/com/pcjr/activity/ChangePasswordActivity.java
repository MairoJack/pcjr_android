package com.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.gson.JsonObject;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.plugins.ColoredSnackbar;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;
import com.pcjr.utils.SharedPreferenceUtil;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 修改密码
 * Created by Mario on 2016/5/24.
 */
public class ChangePasswordActivity extends Activity  implements  Validator.ValidationListener{
    private RelativeLayout back;
    @NotEmpty(message = "原密码不能为空")
    private EditText txt_old_password;
    @NotEmpty(sequence=2,message = "新密码不能为空")
    @Password()
    private EditText txt_password;
    @NotEmpty(message = "确认密码不能为空")
    @ConfirmPassword(message="两次密码不同")
    private EditText txt_confirm_password;

    private Button btn_save;

    private Validator validator;

    private ProgressDialog dialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        validator = new Validator(this);
        validator.setValidationListener(this);
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
        final String oldPassword = txt_old_password.getText().toString().trim();
        final String newPassword = txt_password.getText().toString().trim();
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
                        TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content),json.get("message").getAsString(), TSnackbar.LENGTH_SHORT);
                        ColoredSnackbar.confirm(snackbar).show();
                        finish();
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    } else {
                        TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content),json.get("message").getAsString(), TSnackbar.LENGTH_SHORT);
                        ColoredSnackbar.warning(snackbar).show();                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
               Toast.makeText(ChangePasswordActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
