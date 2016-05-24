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

	RelativeLayout financial_records,invest_records,trade_records,safe_setting,bank_card,msg_center,payment_plan,withdraw_recharge,coupon;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.usercenter, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
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
		Call<Users> callUsers = service.getUserInfo(Constant.access_token);
		callUsers.enqueue(new Callback<Users>() {
			@Override
			public void onResponse(Call<Users> call, Response<Users> response) {
				Users u = response.body();
			}

			@Override
			public void onFailure(Call<Users> call, Throwable t) {
				Log.d("Mario", "onResponse:Throwable " + t);
			}
		});
	}
}
