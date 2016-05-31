package com.pcjr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjr.R;
import com.pcjr.adapter.BankCardListViewAdapter;
import com.pcjr.adapter.InvestRecordsListViewAdapter;
import com.pcjr.common.Constant;
import com.pcjr.model.BankCard;
import com.pcjr.model.InvestRecords;
import com.pcjr.model.Pager;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 银行卡
 * Created by Mario on 2016/5/24.
 */
public class BankCardActivity extends Activity {
    private RelativeLayout back;
    private Button btn_addbankcard;
    private ListView listView;
    private BankCardListViewAdapter adapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_card);
        initView();
        initData();
    }

    public void initView(){
        listView = (ListView) findViewById(R.id.list_view);
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

    public void initData(){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getMemberBankCardInfo(Constant.access_token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    List<BankCard> bankCards = null;
                    if (json.get("data") != null) {
                        bankCards = gson.fromJson(json.get("data"), new TypeToken<List<BankCard>>() {
                        }.getType());
                    }
                    if (json.get("realName") != null) {
                        Constant.realname = json.get("realName").getAsString();
                    }
                    BankCardListViewAdapter adapter = new BankCardListViewAdapter(bankCards, BankCardActivity.this,json.get("realName").getAsString());
                    listView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
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
