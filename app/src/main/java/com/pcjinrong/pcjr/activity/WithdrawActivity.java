package com.pcjinrong.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.pcjinrong.pcjr.plugins.IosDialog;
import com.pcjinrong.pcjr.R;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.model.BankCard;
import com.pcjinrong.pcjr.service.ApiService;
import com.pcjinrong.pcjr.utils.RetrofitUtils;
import com.pcjinrong.pcjr.utils.ViewUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 提现
 * Created by Mario on 2016/5/24.
 */
public class WithdrawActivity extends Activity {
    private RelativeLayout back, btn_recharge, btn_bank;
    private TextView txt_balance, txt_realname, txt_fee, txt_mobile, explain;
    private EditText txt_mention_amount, txt_verify;
    private ImageView info;
    private Button btn_verify, btn_apply;
    private Spinner bank_spinner;
    private List<BankCard> bankCards;
    private String bank_id;
    private ApiService service;
    private TimeCount time;
    private double free_withdraw = 0.00;
    private double available_balance = 0.00;
    private ProgressDialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.withdraw);
        service = RetrofitUtils.createApi(ApiService.class);
        initView();
    }

    public void initView() {
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
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
        time = new TimeCount(60000, 1000);

        txt_mention_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().equals("")) {
                    try {
                        double amount = Double.parseDouble(s.toString());
                        if(amount > available_balance){
                            txt_mention_amount.setText(String.valueOf(available_balance));
                        }
                        if (amount > free_withdraw) {
                            double fee = (amount - free_withdraw) * 0.15 / 100;
                            BigDecimal b = new BigDecimal(String.valueOf(fee));
                            double f = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
                            if(f<=0){
                                txt_fee.setText("0.01");
                            }else {
                                txt_fee.setText(String.valueOf(f));
                            }
                        } else {
                            txt_fee.setText("0.00");
                        }
                    } catch (Exception e) {
                        txt_fee.setText("0.00");
                    }
                } else {
                    txt_fee.setText("0.00");
                }
            }
        });
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
                IosDialog.show("温馨提示", "皮城金融APP暂不支持此操作 ，请在电脑上充值，谢谢您的合作，给您带来的不便，敬请谅解", WithdrawActivity.this);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IosDialog.show("提现金额", "已赚取利息与到期本金之和即为您可免费提现的总额，充值未投资金额则需收取0.15%手续费", WithdrawActivity.this);
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
                String amount = txt_mention_amount.getText().toString().trim();
                String verify = txt_verify.getText().toString().trim();
                if (amount.equals("")) {
                    IosDialog.show("请输入提现金额", WithdrawActivity.this);
                    return;
                }
                if (Double.parseDouble(amount) <= 0) {
                    IosDialog.show("提现金额必须大于0", WithdrawActivity.this);
                    return;
                }
                btn_verify.setClickable(false);
                btn_verify.setBackgroundResource(R.drawable.button_gray);
                time.start();
                send_verify();
            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
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

        explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
                Intent intent = new Intent(WithdrawActivity.this, WebViewActivity.class);
                intent.putExtra("title", "提现细则");
                intent.putExtra("url", "https://m.pcjr.com/appdeal/mention");
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        initData();
    }

    /**
     * 初始化数据
     */
    public void initData() {
        dialog.setMessage("正在加载...");
        dialog.show();
        Call<JsonObject> call = null;
        call = service.getWithdrawInvestInfo(Constant.access_token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        JsonObject data = json.get("data").getAsJsonObject();
                        available_balance = data.get("available_balance").getAsDouble();
                        txt_balance.setText(data.get("available_balance").getAsString());
                        free_withdraw = data.get("free_withdraw").getAsDouble();
                        txt_mention_amount.setHint("免费可提" + data.get("free_withdraw").getAsString() + "元");
                        txt_realname.setText(data.get("realname").getAsString());
                        txt_mobile.setText(data.get("mobile").getAsString());
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("error", json.get("message").getAsString());
                        setResult(RESULT_FIRST_USER, intent);
                        finish();
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(WithdrawActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
                        for (int i = 0; i < bankCards.size(); i++) {
                            mItems[i] = bankCards.get(i).getBank() + " " + bankCards.get(i).getCard_no();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(WithdrawActivity.this, android.R.layout.simple_spinner_item, mItems);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        bank_spinner.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(WithdrawActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 发送提现验证码
     */
    public void send_verify() {


        Call<JsonObject> call = service.withdrawVerify(Constant.BEARER + " " + Constant.access_token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (!json.get("success").isJsonNull() && json.get("success").getAsBoolean()) {
                        IosDialog.show(json.get("message").getAsString(), WithdrawActivity.this);
                    } else {
                        IosDialog.show(json.get("message").getAsString(), WithdrawActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(WithdrawActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 申请提现
     */
    public void apply() {

        String amount = txt_mention_amount.getText().toString().trim();
        String verify = txt_verify.getText().toString().trim();
        if (amount.equals("")) {
            IosDialog.show("请输入提现金额", this);
            return;
        }
        if (Double.parseDouble(amount) <= 0) {
            IosDialog.show("提现金额必须大于0", this);
            return;
        }
        if (verify.equals("")) {
            IosDialog.show("请输入验证码", this);
            return;
        }
        Call<JsonObject> call = service.withdraw(Constant.BEARER + " " + Constant.access_token, amount, bank_id, verify);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        Toast.makeText(WithdrawActivity.this, json.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    } else {
                        IosDialog.show(json.get("message").getAsString(), WithdrawActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(WithdrawActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
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

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            btn_verify.setText("获取验证码");
            btn_verify.setClickable(true);
            btn_verify.setBackgroundResource(R.drawable.orangebtn);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_verify.setText(millisUntilFinished / 1000 + "秒后可重新获取");
        }
    }
}
