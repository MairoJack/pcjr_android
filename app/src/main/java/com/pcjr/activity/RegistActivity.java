package com.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.androidadvance.topsnackbar.TSnackbar;
import com.google.gson.JsonObject;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.pcjr.R;
import com.pcjr.fragment.LoginFragment;
import com.pcjr.plugins.ColoredSnackbar;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 注册
 * Created by Mario on 2016/6/1.
 */
public class RegistActivity extends Activity implements View.OnClickListener
{

    public static final String TAG = RegistActivity.class.getSimpleName();

	private TextView login,syxy,ystk;


    private EditText text_username;
    @Password
    private EditText text_password;
    private EditText text_confirm_password;
    private EditText text_recommend;

    private Button but_regist;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

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
                regist();
            }
        });
	}

	@Override
	public void onClick(View v) {
        Intent intent;
		switch (v.getId()){
			case R.id.login:
                finish();
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

    public void regist() {
        String username = text_username.getText().toString().trim();
        String password = text_password.getText().toString().trim();
        String confirm_password = text_confirm_password.getText().toString().trim();
        String recommend = text_recommend.getText().toString().trim();
        if (username.equals("")) {
            TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), "用户名不能为空", TSnackbar.LENGTH_SHORT);
            ColoredSnackbar.warning(snackbar).show();
            return;
        }
        if (password.equals("")) {
            TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), "密码不能为空", TSnackbar.LENGTH_SHORT);
            ColoredSnackbar.warning(snackbar).show();
            return;
        }
        if (confirm_password.equals("")) {
            TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), "确认密码不能为空", TSnackbar.LENGTH_SHORT);
            ColoredSnackbar.warning(snackbar).show();
            return;
        }
        if (!password.equals(confirm_password)) {
            TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), "两次密码不相同", TSnackbar.LENGTH_SHORT);
            ColoredSnackbar.warning(snackbar).show();
            return;
        }
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
                        TSnackbar snackbar = TSnackbar.make(login,json.get("message").getAsString(), TSnackbar.LENGTH_SHORT);
                        ColoredSnackbar.confirm(snackbar).show();
                        finish();
                        startActivity(new Intent(RegistActivity.this, LoginFragment.class));
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    } else {
                        TSnackbar snackbar = TSnackbar.make(login,json.get("message").getAsString(), TSnackbar.LENGTH_SHORT);
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
