package com.pcjr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.model.FinanceRecords;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;
;import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 资金记录
 * Created by Mario on 2016/5/20.
 */
public class FinancialRecordsActivity extends Activity {
    private RelativeLayout back;

    private TextView available_balance,capital,interest,earned_interest,
                     reward_amount,total_reward_amount,total_amount,recharge_success_amount,
                     invest_success_amount,withdraw_success_amount;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.financial_records);
        initView();
    }

    public void initView(){
        available_balance = (TextView) findViewById(R.id.available_balance);
        capital = (TextView) findViewById(R.id.capital);
        interest = (TextView) findViewById(R.id.interest);
        earned_interest = (TextView) findViewById(R.id.earned_interest);
        reward_amount = (TextView) findViewById(R.id.reward_amount);
        total_reward_amount = (TextView) findViewById(R.id.total_reward_amount);
        recharge_success_amount = (TextView) findViewById(R.id.recharge_success_amount);
        invest_success_amount = (TextView) findViewById(R.id.invest_success_amount);
        withdraw_success_amount = (TextView) findViewById(R.id.withdraw_success_amount);

        back = (RelativeLayout) findViewById(R.id.back);
        initData();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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

    private void initData(){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<FinanceRecords> callUsers = service.getMemberFinanceData(Constant.access_token);
        callUsers.enqueue(new Callback<FinanceRecords>() {
            @Override
            public void onResponse(Call<FinanceRecords> call, Response<FinanceRecords> response) {
                if(response.isSuccessful()){
                    FinanceRecords finance = response.body();
                    available_balance.setText(finance.getAvailable_balance());
                    capital.setText(finance.getCapital());
                    interest.setText(finance.getInterest());
                    earned_interest.setText(finance.getEarned_interest());
                    reward_amount.setText(finance.getReward_amount());
                    total_reward_amount.setText(finance.getTotal_reward_amount());
                    recharge_success_amount.setText(finance.getRecharge_success_amount());
                    invest_success_amount.setText(finance.getInvest_success_amount());
                    withdraw_success_amount.setText(finance.getWithdraw_success_amount());
                }
            }

            @Override
            public void onFailure(Call<FinanceRecords> call, Throwable t) {
                Log.d("Mario", "onResponse:Throwable " + t);
            }
        });
    }

}
