package com.pcjinrong.pcjr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pcjinrong.pcjr.activity.BankCardActivity;
import com.pcjinrong.pcjr.activity.CouponActivity;
import com.pcjinrong.pcjr.activity.TradeRecordsActivity;
import com.pcjinrong.pcjr.plugins.IosDialog;
import com.pcjinrong.pcjr.R;
import com.pcjinrong.pcjr.activity.FinancialRecordsActivity;
import com.pcjinrong.pcjr.activity.InvestRecordsActivity;
import com.pcjinrong.pcjr.activity.MsgCenterActivity;
import com.pcjinrong.pcjr.activity.PaymentPlanActivity;
import com.pcjinrong.pcjr.activity.SafeSettingActivity;
import com.pcjinrong.pcjr.activity.WithdrawActivity;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.model.BankCard;
import com.pcjinrong.pcjr.service.ApiService;
import com.pcjinrong.pcjr.utils.RetrofitUtils;
import com.pcjinrong.pcjr.utils.ViewUtil;

import java.util.List;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用户中心
 * Created by Mario on 2016/5/23.
 */
public class MemberFragment extends Fragment {

    public static final String TAG = MemberFragment.class.getSimpleName();
    private PtrClassicFrameLayout mPtrFrame;
    private ScrollView scrollView;
    private RelativeLayout financial_records, invest_records, trade_records, safe_setting, bank_card, msg_center, payment_plan, withdraw_recharge, coupon;
    private TextView username, available_balance, sum_assets, uncollected_interest_sum;
    private ApiService service;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = RetrofitUtils.createApi(ApiService.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.usercenter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_frame);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
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
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
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
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
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
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
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
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
                getActivity().startActivityForResult(new Intent(getActivity(), SafeSettingActivity.class),Constant.SAFESETTING);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        /**
         * 银行卡
         */
        bank_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
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
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
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
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
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
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
                Call<JsonObject> call = service.getMemberBankCardInfo(Constant.access_token);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            Gson gson = new Gson();
                            List<BankCard> bankCards = null;
                            if (json.get("data").getAsJsonArray().size() <= 0) {
                                IosDialog.show("请先添加银行卡",getContext());
                            } else {
                                getActivity().startActivityForResult(new Intent(getActivity(), WithdrawActivity.class), Constant.REQUSET);
                                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                    }
                });

            }
        });
        /**
         * 优惠券
         */
        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
                startActivity(new Intent(getActivity(), CouponActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        //下拉刷新
        mPtrFrame.disableWhenHorizontalMove(true);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, scrollView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initData();
            }
        });

        initData();
    }

    private void initData() {
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> callUsers = service.getMemberIndex(Constant.access_token);
        callUsers.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mPtrFrame.refreshComplete();
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    username.setText(json.get("user_name").getAsString());
                    available_balance.setText(json.get("available_balance").getAsString());
                    sum_assets.setText(json.get("total").getAsString());
                    uncollected_interest_sum.setText(json.get("interest").getAsString());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mPtrFrame.refreshComplete();
                if(getActivity()!=null)
                    Toast.makeText(getActivity(),"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }


    public static Fragment newInstance(String text) {
        return new MemberFragment();
    }



}
