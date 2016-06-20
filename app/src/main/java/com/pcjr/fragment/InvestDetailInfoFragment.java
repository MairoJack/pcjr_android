package com.pcjr.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.pcjr.R;
import com.pcjr.model.Product;
import com.pcjr.plugins.ProgressWheel;
import com.pcjr.utils.DateUtil;


/**
 * Created by Mario on 2016/5/12.
 */
public class InvestDetailInfoFragment extends Fragment {

    private ProgressWheel progressWheel;
    private float progress;
    private ProgressDialog dialog;
    private LinearLayout preview_repayment,debx;
    private TextView txt_preview_repayment,txt_debx, year_income, name, threshold_amount,
            increasing_amount, txt_repayment, amount, month, invest_amount,
            product_no, repayment_date, value_date, guarantors_name, intro, borrower_intro;

    private String id;
    private int index = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.invest_detail_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = new ProgressDialog(getContext(), ProgressDialog.STYLE_SPINNER);
        progress = 0;
        progressWheel = (ProgressWheel) view.findViewById(R.id.pw_spinner);
        preview_repayment = (LinearLayout) view.findViewById(R.id.preview_repayment);
        debx = (LinearLayout) view.findViewById(R.id.debx);
        txt_debx = (TextView) view.findViewById(R.id.txt_debx);
        txt_repayment = (TextView) view.findViewById(R.id.txt_repayment);
        txt_preview_repayment = (TextView) view.findViewById(R.id.txt_preview_repayment);
        year_income = (TextView) view.findViewById(R.id.year_income);
        name = (TextView) view.findViewById(R.id.name);
        threshold_amount = (TextView) view.findViewById(R.id.threshold_amount);
        increasing_amount = (TextView) view.findViewById(R.id.increasing_amount);
        amount = (TextView) view.findViewById(R.id.amount);
        month = (TextView) view.findViewById(R.id.month);
        invest_amount = (TextView) view.findViewById(R.id.invest_amount);
        product_no = (TextView) view.findViewById(R.id.product_no);
        repayment_date = (TextView) view.findViewById(R.id.repayment_date);
        value_date = (TextView) view.findViewById(R.id.value_date);
        guarantors_name = (TextView) view.findViewById(R.id.guarantors_name);
        intro = (TextView) view.findViewById(R.id.intro);
        borrower_intro = (TextView) view.findViewById(R.id.company_intro);

        initData();

    }

    public void initData() {
        Bundle bundle = getArguments();
        final Product product = (Product) bundle.getSerializable("product");
        if(product.getIs_preview_repayment() == 1){
            String html_preview_repayment = "* 本产品具有 <font color='#dc4d07'>提前回款</font> 可能，平台确保此产品最短借款时长为 <font color='#dc4d07'>"+product.getMin_repayment_date()+"</font> ，如提前回款则补偿本产品 <font color='#dc4d07'>"+product.getPay_interest_day()+"天利息</font> 于投资人";
            preview_repayment.setVisibility(View.VISIBLE);
            txt_preview_repayment.setText(Html.fromHtml(html_preview_repayment));
        }
        if(product.getRepayment() == 2){
            String html_debx = "* 本产品为 <font color='#dc4d07'>等额本息</font> 产品，每投资1000元预期收益为 <font color='#dc4d07'>"+product.getEstimate_interest()+"</font> 元，按月还本付息，资金更灵活，理财更安心";
            debx.setVisibility(View.VISIBLE);
            txt_debx.setText(Html.fromHtml(html_debx));
        }
        int repayment = product.getRepayment();
        if (repayment == 0) {
            txt_repayment.setText("一次还本付息");
        } else if (repayment == 1) {
            txt_repayment.setText("先息后本(按月付息)");
        } else if (repayment == 2) {
            txt_repayment.setText("等额本息");
        } else if (repayment == 3) {
            txt_repayment.setText("先息后本(按季付息)");
        }
        year_income.setText(product.getYear_income()+"%");
        name.setText(product.getName());
        threshold_amount.setText(product.getThreshold_amount() + "元起购");
        increasing_amount.setText(product.getIncreasing_amount() + "元递增");
        amount.setText(String.format("%.2f",product.getAmount() / 10000));
        month.setText(product.getMonth());
        double can_invest_amount = (product.getAmount() - product.getProduct_amount()) / 10000;
        invest_amount.setText(String.format("%.2f",can_invest_amount));
        product_no.setText(product.getProduct_no());
        repayment_date.setText(DateUtil.transferLongToDate("yyyy-MM-dd", product.getValue_date()*1000));
        value_date.setText(DateUtil.transferLongToDate("yyyy-MM-dd", product.getValue_date()*1000));
        if (product.getGuarantors_name().equals("null")) {
            guarantors_name.setVisibility(View.GONE);
        } else {
            guarantors_name.setText(product.getGuarantors_name());
        }
        intro.setText(product.getIntro());
        borrower_intro.setText(product.getBorrower_intro());
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run()
            {
                if(index<=(int)(product.getRate()*18/5)){
                    progressWheel.setProgress(index);
                    index++;
                    mHandler.postDelayed(this, 5);
                }else{
                    mHandler.removeCallbacks(this);
                }
            }
        },1000);
    }

    public static Fragment newInstance(Product product) {
        InvestDetailInfoFragment fragment = new InvestDetailInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        fragment.setArguments(bundle);
        return fragment;
    }
}
