package com.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.pcjr.R;
import com.pcjr.fragment.LoginFragment;
import com.pcjr.fragment.PersonFragment;
import com.pcjr.plugins.ColoredSnackbar;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 注册
 * Created by Mario on 2016/6/1.
 */
public class RegistActivity extends Activity implements View.OnClickListener,Validator.ValidationListener
{

    public static final String TAG = RegistActivity.class.getSimpleName();

	private TextView login,syxy,ystk;


    @NotEmpty(message="用户名不能为空")
    private EditText text_username;
    @NotEmpty(message="密码不能为空")
    @Password
    private EditText text_password;
    @NotEmpty(message="确认密码不能为空")
    @ConfirmPassword(message="两次密码不同")
    private EditText text_confirm_password;
    private EditText text_recommend;

    private Button but_regist;
    private Validator validator;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        validator = new Validator(this);
        validator.setValidationListener(this);
        initView();
    }



	public void initView() {
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
		login = (TextView) findViewById(R.id.login);
		syxy = (TextView) findViewById(R.id.syxy);
		ystk = (TextView) findViewById(R.id.ystk);

        text_username = (EditText) findViewById(R.id.username);
        text_password = (EditText) findViewById(R.id.password);
        text_confirm_password = (EditText) findViewById(R.id.text_confirm_password);
        text_recommend = (EditText) findViewById(R.id.txt_recommend);

		login.setOnClickListener(this);
		syxy.setOnClickListener(this);
		ystk.setOnClickListener(this);

        but_regist = (Button)findViewById(R.id.registbtn);
        but_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
	}

	@Override
	public void onClick(View v) {
        Intent intent;
		switch (v.getId()){
			case R.id.login:
                intent = new Intent(RegistActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
				break;
			case R.id.syxy:
                intent = new Intent(RegistActivity.this,AgreementActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
				break;
			case R.id.ystk:
                intent = new Intent(RegistActivity.this,PrivacyPolicyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
				break;
		}
	}

    @Override
    public void onValidationSucceeded() {
        String username = text_username.getText().toString().trim();
        String password = text_password.getText().toString().trim();
        String recommend = text_recommend.getText().toString().trim();
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.register(username,password,recommend);
        dialog.setMessage("正在提交...");
        dialog.show();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        Snackbar snackbar = Snackbar.make(login,json.get("message").getAsString(), Snackbar.LENGTH_SHORT);
                        ColoredSnackbar.confirm(snackbar).show();
                        finish();
                        startActivity(new Intent(RegistActivity.this, LoginFragment.class));
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    } else {
                        Snackbar snackbar = Snackbar.make(login,json.get("message").getAsString(), Snackbar.LENGTH_SHORT);
                        ColoredSnackbar.warning(snackbar).show();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(RegistActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;

    }
}
