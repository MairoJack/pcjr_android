package com.pcjinrong.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonObject;
import com.pcjinrong.pcjr.plugins.IosDialog;
import com.pcjinrong.pcjr.utils.DateUtil;
import com.pcjinrong.pcjr.R;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.model.Product;
import com.pcjinrong.pcjr.service.ApiService;
import com.pcjinrong.pcjr.utils.RetrofitUtils;
import com.pcjinrong.pcjr.utils.ViewUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 投资
 * Created by Mario on 2016/5/24.
 */
public class InvestActivity extends Activity {
    private RelativeLayout back, tips;
    private LinearLayout preview_repayment;
    private TextView product_name, txt_preview_repayment, txt_threshold_amount, txt_balance, txt_repayment, txt_month, txt_year_income, txt_repayment_date;
    private EditText txt_invest_amount, txt_password;
    private Button btn_invest, btn_allin;
    private ApiService service;
    private Product product;
    private double available_balance = 0;
    private ProgressDialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.invest);
        service = RetrofitUtils.createApi(ApiService.class);
        initView();
        initData();
    }

    public void initView() {
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        back = (RelativeLayout) findViewById(R.id.back);
        tips = (RelativeLayout) findViewById(R.id.tips);
        product_name = (TextView) findViewById(R.id.product_name);
        txt_balance = (TextView) findViewById(R.id.txt_balance);
        preview_repayment = (LinearLayout) findViewById(R.id.preview_repayment);
        txt_preview_repayment = (TextView) findViewById(R.id.txt_preview_repayment);
        txt_repayment = (TextView) findViewById(R.id.txt_repayment);
        txt_threshold_amount = (TextView) findViewById(R.id.txt_threshold_amount);
        txt_month = (TextView) findViewById(R.id.txt_month);
        txt_year_income = (TextView) findViewById(R.id.txt_year_income);
        txt_repayment_date = (TextView) findViewById(R.id.txt_repayment_date);
        txt_invest_amount = (EditText) findViewById(R.id.txt_invest_amount);
        txt_password = (EditText) findViewById(R.id.txt_password);
        btn_invest = (Button) findViewById(R.id.btn_invest);
        btn_allin = (Button) findViewById(R.id.btn_allin);


        btn_invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
                double can_invest_amount = product.getAmount() - product.getProduct_amount();
                final String amount = txt_invest_amount.getText().toString().trim();
                if (amount.equals("")) {
                    IosDialog.show("请输入投资金额", InvestActivity.this);
                    return;
                }
                if (Double.parseDouble(amount) > can_invest_amount) {
                    IosDialog.show("金额超过可投最大值", InvestActivity.this);
                    return;
                }
                if (Double.parseDouble(amount) < Double.parseDouble(product.getThreshold_amount())) {
                    IosDialog.show("金额不能小于起投金额", InvestActivity.this);
                    return;
                }
                if (Double.parseDouble(amount) > available_balance) {
                    IosDialog.show("可用余额不足", InvestActivity.this);
                    return;
                }
                if(Double.parseDouble(product.getIncreasing_amount()) != 0) {
                    if (Double.parseDouble(amount) % Double.parseDouble(product.getIncreasing_amount()) != 0) {
                        IosDialog.show("金额没有按递增金额填写", InvestActivity.this);
                        return;
                    }
                }
                new MaterialDialog.Builder(InvestActivity.this)
                        .title("输入密码")
                        .content("投资金额：" + amount + "元")
                        .positiveColor(Color.parseColor("#FF6602"))
                        .buttonsGravity(GravityEnum.CENTER)
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .input("请输入密码", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                if (input.toString().equals("")) {
                                    IosDialog.show("请输入密码", InvestActivity.this);
                                } else {
                                    invest(input.toString(), amount);
                                }
                            }
                        }).show();
            }
        });

        btn_allin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
                double can_invest_amount = product.getAmount() - product.getProduct_amount();
                final int amount;
                double increase = Double.parseDouble(product.getIncreasing_amount());
                if (can_invest_amount > available_balance) {
                    if(increase == 0){
                        amount = (int)available_balance;
                    }else if(available_balance%increase == 0){
                        amount = (int)available_balance;
                    }else{
                        amount = (int) (available_balance - available_balance % increase);
                    }
                    if (amount < Double.parseDouble(product.getThreshold_amount())) {
                        IosDialog.show("可用余额不足", InvestActivity.this);
                        return;
                    }
                } else {
                    if(increase == 0){
                        amount = (int)can_invest_amount;
                    }else if(can_invest_amount%increase == 0){
                        amount = (int)can_invest_amount;
                    }else{
                        amount = (int) (can_invest_amount - can_invest_amount % increase);
                    }

                }
                if(amount <= 0){
                    IosDialog.show("可用余额不足", InvestActivity.this);
                    return;
                }
                new MaterialDialog.Builder(InvestActivity.this)
                        .title("输入密码")
                        .content("投资金额：" + amount + "元")
                        .positiveColor(Color.parseColor("#FF6602"))
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .input("请输入密码", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                if (input.toString().equals("")) {
                                    IosDialog.show("请输入密码", InvestActivity.this);
                                } else {
                                    invest(input.toString(), String.valueOf(amount));
                                }
                            }
                        }).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IosDialog.show("投资提示", "若您选择全投，且您的可用余额大于该产品剩余可投金额，投资金额将自动填写为该产品剩余可投金额，有一定概率投资失败，请谨慎操作", InvestActivity.this);
            }
        });


    }

    /**
     * 初始化数据
     */
    public void initData() {
        dialog.setMessage("正在加载...");
        dialog.show();
        Bundle bundle = getIntent().getExtras();
        product = (Product) bundle.getSerializable("product");
        Call<JsonObject> call = null;
        call = service.getWithdrawInvestInfo(Constant.access_token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        JsonObject data = json.get("data").getAsJsonObject();
                        txt_balance.setText(data.get("available_balance").getAsString() + "元");
                        available_balance = data.get("available_balance").getAsDouble();
                        product_name.setText(product.getName());
                        if (product.getIs_preview_repayment() == 1) {
                            String html_preview_repayment = "* 本产品具有 <font color='#dc4d07'>提前回款</font> 可能，平台确保此产品最短借款时长为 <font color='#dc4d07'>" + product.getMin_repayment_date() + "</font> ，如提前回款则补偿本产品 <font color='#dc4d07'>" + product.getPay_interest_day() + "天利息</font> 于投资人";
                            preview_repayment.setVisibility(View.VISIBLE);
                            txt_preview_repayment.setText(Html.fromHtml(html_preview_repayment));
                        }
                        txt_invest_amount.setHint("可投" + String.format("%.2f", (product.getAmount() - product.getProduct_amount())) + "元");
                        txt_threshold_amount.setText("起投/递增金额:" + product.getThreshold_amount() + "元/" + product.getIncreasing_amount() + " 元");
                        int repayment = product.getRepayment();
                        if (repayment == 0) {
                            txt_repayment.setText("一次还本付息");
                        } else if (repayment == 1) {
                            txt_repayment.setText("先息后本(月)");
                        } else if (repayment == 2) {
                            txt_repayment.setText("等额本息");
                        } else if (repayment == 3) {
                            txt_repayment.setText("先息后本(季)");
                        }
                        txt_month.setText(product.getMonth());
                        txt_year_income.setText(product.getYear_income() + "%");
                        txt_repayment_date.setText(DateUtil.transferLongToDate("yyyy-MM-dd", product.getRepayment_date() * 1000));
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("error", json.get("message").getAsString());
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(InvestActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }

    /**
     * 确认投资
     */
    public void invest(String password, String amount) {
        dialog.setMessage("正在提交...");
        dialog.show();
        Call<JsonObject> call = service.investProduct(Constant.BEARER + " " + Constant.access_token, Constant.access_token, amount, product.getId(), password);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        Toast.makeText(InvestActivity.this, json.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(InvestActivity.this, InvestRecordsActivity.class));
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    } else {
                        IosDialog.show(json.get("message").getAsString(), InvestActivity.this);
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(InvestActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
