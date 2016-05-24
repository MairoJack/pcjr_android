package com.pcjr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.pcjr.R;

/**
 * 安全设置
 * Created by Mario on 2016/5/24.
 */
public class SafeSettingActivity extends Activity {
    private RelativeLayout back,realname,bindphone,changepswd;
    private Button btn_logout;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_setting);
        initView();
    }

    public void initView(){
        back = (RelativeLayout) findViewById(R.id.back);
        realname = (RelativeLayout) findViewById(R.id.realname);
        bindphone = (RelativeLayout) findViewById(R.id.bindphone);
        changepswd = (RelativeLayout) findViewById(R.id.changepswd);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        realname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SafeSettingActivity.this, RealNameVerifiedActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        bindphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SafeSettingActivity.this, BindPhoneActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        changepswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SafeSettingActivity.this, ChangePasswordActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Mario", "onClick: logout");
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
