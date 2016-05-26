package com.pcjr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.pcjr.R;

/**
 * 优惠券
 * Created by Mario on 2016/5/24.
 */
public class CouponActivity extends Activity {
    private RelativeLayout invest_certificate,red_packet,back;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon);
        initView();
    }

    public void initView() {
        invest_certificate = (RelativeLayout) findViewById(R.id.invest_certificate);
        red_packet = (RelativeLayout) findViewById(R.id.red_packet);
        back = (RelativeLayout) findViewById(R.id.back);

        invest_certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CouponActivity.this, InvestmentCertificatesActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        red_packet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CouponActivity.this, InvestmentCertificatesActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;

    }

}
