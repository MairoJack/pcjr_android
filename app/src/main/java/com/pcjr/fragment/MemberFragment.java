package com.pcjr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pcjr.R;
import com.pcjr.activity.BankCardActivity;
import com.pcjr.activity.CouponActivity;
import com.pcjr.activity.FinancialRecordsActivity;
import com.pcjr.activity.InvestRecordsActivity;
import com.pcjr.activity.MsgCenterActivity;
import com.pcjr.activity.PaymentPlanActivity;
import com.pcjr.activity.SafeSettingActivity;
import com.pcjr.activity.TradeRecordsActivity;
import com.pcjr.activity.WithdrawRechargeActivity;
import com.pcjr.common.Constant;
import com.pcjr.model.Member;
import com.pcjr.model.Users;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用户中心
 * Created by Mario on 2016/5/23.
 */
public class MemberFragment extends Fragment
{

	private RelativeLayout financial_records,invest_records,trade_records,safe_setting,bank_card,msg_center,payment_plan,withdraw_recharge,coupon;
	private TextView username,available_balance,sum_assets,uncollected_interest_sum;
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.usercenter, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

        username = (TextView) view.findViewById(R.id.username);
        available_balance = (TextView) view.findViewById(R.id.available_balance);
        sum_assets = (TextView) view.findViewById(R.id.sum_assets);
        uncollected_interest_sum = (TextView) view.findViewById(R.id.uncollected_interest_sum);

		financial_records = (RelativeLayout) view.findViewById(R.id.financial_records);
        invest_records = (RelativeLayout) view.findViewById(R.id.invest_records);
        trade_records = (RelativeLayout) view.findViewById(R.id.trade_records);
        safe_setting = (RelativeLayout) view.findViewById(R.id.safe_setting);
        bank_card = (RelativeLayout) view.findViewById(R.id.bank_card);
        msg_center = (RelativeLayout) view.findViewById(R.id.msg_center);
        payment_plan = (RelativeLayout) view.findViewById(R.id.payment_plan);
        withdraw_recharge = (RelativeLayout) view.findViewById(R.id.withdraw_recharge);
        coupon = (RelativeLayout) view.findViewById(R.id.coupon);
        /**
         * 资金记录
         */
		financial_records.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), FinancialRecordsActivity.class));
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
        /**
         * 投资记录
         */
        invest_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InvestRecordsActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        /**
         * 交易记录
         */
        trade_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TradeRecordsActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        /**
         * 安全设置
         */
        safe_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SafeSettingActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        /**
         * 银行卡
         */
        bank_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BankCardActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        /**
         * 消息中心
         */
        msg_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MsgCenterActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        /**
         * 回款计划
         */
        payment_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PaymentPlanActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        /**
         * 提现充值
         */
        withdraw_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WithdrawRechargeActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        /**
         * 优惠券
         */
        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CouponActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
		initData();
	}

	private void initData(){
		ApiService service = RetrofitUtils.createApi(ApiService.class);
		Call<JsonObject> callUsers = service.getMemberIndex(Constant.access_token);
		callUsers.enqueue(new Callback<JsonObject>() {
			@Override
			public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject json = response.body();
                    username.setText(json.get("user_name").getAsString());
                    available_balance.setText(json.get("available_balance").getAsString());
                    sum_assets.setText(json.get("total").getAsString());
                    uncollected_interest_sum.setText(json.get("interest").getAsString());
                }
			}

			@Override
			public void onFailure(Call<JsonObject> call, Throwable t) {
				Log.d("Mario", "onResponse:Throwable " + t);
			}
		});
	}
}
