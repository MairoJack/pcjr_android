package com.pcjinrong.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pcjinrong.pcjr.R;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.model.Letter;
import com.pcjinrong.pcjr.service.ApiService;
import com.pcjinrong.pcjr.utils.DateUtil;
import com.pcjinrong.pcjr.utils.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消息详情
 * Created by Mario on 2016/5/24.
 */
public class MsgDetailActivity extends Activity {
    private TextView msg_title,msg_time,msg_content;
    private RelativeLayout back;
    private ProgressDialog dialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_detail);
        initView();
        initData();
    }

    public void initView(){
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        msg_title = (TextView) findViewById(R.id.msg_title);
        msg_time = (TextView) findViewById(R.id.msg_time);
        msg_content = (TextView) findViewById(R.id.msg_content);
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
        Call<JsonObject> call = service.getLetterDetail(Constant.access_token,id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    Letter letter = null;
                    if (json.get("success").getAsBoolean()) {
                        letter = gson.fromJson(json.get("data"), Letter.class);
                        msg_title.setText(letter.getTitle());
                        msg_time.setText(DateUtil.transferLongToDate("yyyy-MM-dd HH:mm:ss",letter.getSend_date()*1000));
                        msg_content.setText(letter.getContent());
                    }else{
                        Toast.makeText(MsgDetailActivity.this,"获取站内信详情失败",Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MsgDetailActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
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
