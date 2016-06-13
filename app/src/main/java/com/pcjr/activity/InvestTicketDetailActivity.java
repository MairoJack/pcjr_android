package com.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.model.InvestTicket;
import com.pcjr.model.Letter;
import com.pcjr.plugins.ColoredSnackbar;
import com.pcjr.service.ApiService;
import com.pcjr.utils.DateUtil;
import com.pcjr.utils.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 投资券详情
 * Created by Mario on 2016/5/24.
 */
public class InvestTicketDetailActivity extends Activity {
    private TextView msg,title,end_time,introduction;
    private RelativeLayout back;
    private ProgressDialog dialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invest_ticket_detail);
        initView();
        initData();
    }

    public void initView(){
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        msg = (TextView) findViewById(R.id.msg);
        title = (TextView) findViewById(R.id.title);
        end_time = (TextView) findViewById(R.id.end_time);
        introduction = (TextView) findViewById(R.id.introduction);
        back = (RelativeLayout) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    public void initData(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        dialog.setMessage("正在加载...");
        dialog.show();
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getInvestTicketDetail(Constant.access_token,id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    InvestTicket investTicket = null;
                    if (json.get("success").getAsBoolean()) {
                        investTicket = gson.fromJson(json.get("data"), InvestTicket.class);
                        msg.setText("满 "+investTicket.getReach_amount()+" 元返 "+investTicket.getAmount()+" 元");
                        title.setText("来源:"+investTicket.getTitle());
                        end_time.setText(DateUtil.transferLongToDate("yyyy-MM-dd HH:mm:ss",investTicket.getEnd_time()*1000));
                        introduction.setText(investTicket.getActivity().getIntroduction().replace("</br>",""));
                    }else{
                        TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content),"获取投资券详情失败", TSnackbar.LENGTH_SHORT);
                        ColoredSnackbar.warning(snackbar).show();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(InvestTicketDetailActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
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
