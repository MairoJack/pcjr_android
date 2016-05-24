package com.pcjr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.pcjr.R;

/**
 * 银行卡
 * Created by Mario on 2016/5/24.
 */
public class BankCardActivity extends Activity {
    private RelativeLayout back;
    private Button btn_addbankcard;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_card);
        initView();
    }

    public void initView(){
        back = (RelativeLayout) findViewById(R.id.back);
        btn_addbankcard = (Button) findViewById(R.id.btn_addbankcard);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        btn_addbankcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BankCardActivity.this, AddBankCardActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
