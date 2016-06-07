package com.pcjr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjr.R;
import com.pcjr.adapter.BankCardListViewAdapter;
import com.pcjr.common.Constant;
import com.pcjr.model.BankCard;
import com.pcjr.plugins.ColoredSnackbar;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 提现
 * Created by Mario on 2016/5/24.
 */
public class WithdrawActivity extends Activity {
    private RelativeLayout back, btn_recharge, btn_bank;
    private TextView txt_balance, txt_realname, txt_fee, txt_mobile,explain;
    private EditText txt_mention_amount,txt_verify;
    private ImageView info;
    private Button btn_verify, btn_apply;
    private Spinner bank_spinner;
    private List<BankCard> bankCards;
    private String bank_id;
    private ApiService service;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.withdraw);
        service = RetrofitUtils.createApi(ApiService.class);
        initView();
    }

    public void initView() {
        btn_recharge = (RelativeLayout) findViewById(R.id.btn_recharge);
        btn_bank = (RelativeLayout) findViewById(R.id.btn_bank);
        back = (RelativeLayout) findViewById(R.id.back);
        txt_balance = (TextView) findViewById(R.id.txt_balance);
        txt_mention_amount = (EditText) findViewById(R.id.txt_mention_amount);
        txt_realname = (TextView) findViewById(R.id.txt_realname);
        txt_fee = (TextView) findViewById(R.id.txt_fee);
        txt_mobile = (TextView) findViewById(R.id.txt_mobile);
        txt_verify = (EditText) findViewById(R.id.txt_verify);
        explain = (TextView) findViewById(R.id.explain);
        info = (ImageView) findViewById(R.id.info);
        btn_verify = (Button) findViewById(R.id.btn_verify);
        btn_apply = (Button) findViewById(R.id.btn_apply);
        bank_spinner = (Spinner) findViewById(R.id.bank_spinner);

        bank_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bank_id = bankCards.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(WithdrawActivity.this)
                        .setTitleText("温馨提示")
                        .setContentText("皮城金融手机版暂不支持此操作 ，请在电脑上充值，谢谢您的合作，给您带来的不便，敬请谅解！")
                        .show();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(WithdrawActivity.this)
                        .setTitleText("提现金额")
                        .setContentText("已赚取利息与到期本金之和即为您可免费提现的总额，充值未投资金额则需收取0.15%手续费。")
                        .show();
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_verify();
            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apply();
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

    /**
     * 初始化数据
     */
    public void initData(){
        Call<JsonObject> call = null;
        call = service.getWithdrawInvestInfo(Constant.access_token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        JsonObject data = json.get("data").getAsJsonObject();
                        txt_balance.setText(data.get("available_balance").getAsString());
                        txt_mention_amount.setHint(data.get("free_withdraw").getAsString());
                        txt_realname.setText(data.get("realname").getAsString());
                        txt_mobile.setText(data.get("mobile").getAsString());
                    } else{
                        Snackbar snackbar = Snackbar.make(back,"获取用户提现信息失败", Snackbar.LENGTH_SHORT);
                        ColoredSnackbar.alert(snackbar).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(WithdrawActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });

        call = service.getMemberBankCardInfo(Constant.access_token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    if (json.get("data") != null) {
                        bankCards = gson.fromJson(json.get("data"), new TypeToken<List<BankCard>>() {
                        }.getType());
                        String[] mItems = new String[bankCards.size()];
                        for(int i=0;i<bankCards.size();i++){
                            mItems[i] = bankCards.get(i).getBank()+" "+bankCards.get(i).getCard_no();
                        }
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(WithdrawActivity.this,android.R.layout.simple_spinner_item, mItems);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        bank_spinner .setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(WithdrawActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 发送提现验证码
     */
    public void send_verify(){

        Call<JsonObject> call = service.withdrawVerify(Constant.access_token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (!json.get("success").isJsonNull() && json.get("success").getAsBoolean()) {
                        Snackbar snackbar = Snackbar.make(back, json.get("message").getAsString(), Snackbar.LENGTH_SHORT);
                        ColoredSnackbar.confirm(snackbar).show();
                    } else {
                        Snackbar snackbar = Snackbar.make(back, json.get("message").getAsString(), Snackbar.LENGTH_SHORT);
                        ColoredSnackbar.warning(snackbar).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(WithdrawActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 申请提现
     */
    public void apply(){

        String amount = txt_mention_amount.getText().toString().trim();
        String verify = txt_verify.getText().toString().trim();
        if(amount.equals("")){
            Snackbar snackbar = Snackbar.make(back,"请输入提现金额", Snackbar.LENGTH_SHORT);
            ColoredSnackbar.warning(snackbar).show();
            return;
        }
        if(verify.equals("")){
            Snackbar snackbar = Snackbar.make(back,"请输入验证码", Snackbar.LENGTH_SHORT);
            ColoredSnackbar.warning(snackbar).show();
            return;
        }
        Call<JsonObject> call = service.withdraw(Constant.access_token,amount,bank_id,verify);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        Snackbar snackbar = Snackbar.make(back,json.get("message").getAsString(), Snackbar.LENGTH_SHORT);
                        ColoredSnackbar.confirm(snackbar).show();
                        finish();
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    }else{
                        Snackbar snackbar = Snackbar.make(back,json.get("message").getAsString(), Snackbar.LENGTH_SHORT);
                        ColoredSnackbar.warning(snackbar).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(WithdrawActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
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
