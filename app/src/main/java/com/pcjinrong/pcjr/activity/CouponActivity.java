package com.pcjinrong.pcjr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.pcjinrong.pcjr.R;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.service.ApiService;
import com.pcjinrong.pcjr.utils.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 优惠券
 * Created by Mario on 2016/5/24.
 */
public class CouponActivity extends Activity {
    private RelativeLayout invest_certificate,red_packet,back;
    private TextView invest_ticket_number,red_packet_number;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon);
        initView();

    }

    public void initView() {
        invest_certificate = (RelativeLayout) findViewById(R.id.invest_certificate);
        red_packet = (RelativeLayout) findViewById(R.id.red_packet);
        invest_ticket_number = (TextView) findViewById(R.id.invest_ticket_number);
        red_packet_number = (TextView) findViewById(R.id.red_packet_number);

        back = (RelativeLayout) findViewById(R.id.back);

        invest_certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CouponActivity.this, InvesTicketActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        red_packet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CouponActivity.this, RedPacketActivity.class));
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


        initData();
    }

    public void initData(){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getUnusedCouponsNum(Constant.access_token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("invest_ticket_num") != null) {
                        int  investTicketNum = json.get("invest_ticket_num").getAsInt();
                        if(investTicketNum!=0){
                            invest_ticket_number.setVisibility(View.VISIBLE);
                            invest_ticket_number.setText(String.valueOf(investTicketNum));
                        }
                    }
                    if (json.get("red_packet_num") != null) {
                        int  redPacketNum = json.get("red_packet_num").getAsInt();
                        if(redPacketNum!=0){
                            red_packet_number.setVisibility(View.VISIBLE);
                            red_packet_number.setText(String.valueOf(redPacketNum));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(CouponActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
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
