package com.pcjr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.InputType;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.model.BankCard;
import com.pcjr.model.InvestRecords;
import com.pcjr.model.Product;
import com.pcjr.plugins.ColoredSnackbar;
import com.pcjr.service.ApiService;
import com.pcjr.utils.DateUtil;
import com.pcjr.utils.RetrofitUtils;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 投资
 * Created by Mario on 2016/5/24.
 */
public class InvestActivity extends Activity {
    private RelativeLayout back;
    private TextView txt_preview_repayment, txt_balance,txt_repayment,txt_month,txt_year_income,txt_repayment_date;
    private EditText txt_invest_amount,txt_password;
    private Button btn_invest;
    private ApiService service;
    private Product product;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.invest);
        service = RetrofitUtils.createApi(ApiService.class);
        initView();
        initData();
    }

    public void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        txt_balance = (TextView) findViewById(R.id.txt_balance);
        txt_preview_repayment = (TextView) findViewById(R.id.txt_preview_repayment);
        txt_repayment = (TextView) findViewById(R.id.txt_repayment);
        txt_month = (TextView) findViewById(R.id.txt_month);
        txt_year_income = (TextView) findViewById(R.id.txt_year_income);
        txt_repayment_date = (TextView) findViewById(R.id.txt_repayment_date);
        txt_invest_amount = (EditText) findViewById(R.id.txt_invest_amount);
        txt_password = (EditText) findViewById(R.id.txt_password);
        btn_invest = (Button) findViewById(R.id.btn_invest);



        btn_invest.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(InvestActivity.this)
                        .title("输入密码")
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .input("请输入密码", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                if(input.equals("")){
                                    Snackbar snackbar = Snackbar.make(back,"请输入密码", Snackbar.LENGTH_SHORT);
                                    ColoredSnackbar.warning(snackbar).show();
                                }else{
                                    invest(input.toString());
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


    }

    /**
     * 初始化数据
     */
    public void initData(){
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
                        txt_balance.setText(data.get("available_balance").getAsString());
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
                        txt_year_income.setText(product.getYear_income());
                        txt_repayment_date.setText(DateUtil.transferLongToDate("yyyy-MM-dd",product.getRepayment_date()*1000));
                    } else{
                        Snackbar snackbar = Snackbar.make(back,"获取用户投资信息失败", Snackbar.LENGTH_SHORT);
                        ColoredSnackbar.alert(snackbar).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(InvestActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * 确认投资
     */
    public void invest(String password){

        String amount = txt_invest_amount.getText().toString().trim();
        if(amount.equals("")){
            Snackbar snackbar = Snackbar.make(back,"请输入投资金额", Snackbar.LENGTH_SHORT);
            ColoredSnackbar.warning(snackbar).show();
            return;
        }
        Call<JsonObject> call = service.investProduct(Constant.access_token,amount,product.getId(),password);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        Snackbar snackbar = Snackbar.make(back,json.get("message").getAsString(), Snackbar.LENGTH_SHORT);
                        ColoredSnackbar.confirm(snackbar).show();
                        finish();
                        startActivity(new Intent(InvestActivity.this, InvestRecordsActivity.class));
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    }else{
                        Snackbar snackbar = Snackbar.make(back,json.get("message").getAsString(), Snackbar.LENGTH_SHORT);
                        ColoredSnackbar.warning(snackbar).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(InvestActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
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
