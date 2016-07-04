package com.pcjinrong.pcjr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pcjinrong.pcjr.R;

/**
 * 产品风控
 * Created by Mario on 2016/5/12.
 */
public class InvestDetailRiskFragment extends Fragment{

    private TextView txt_risk_control;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.invest_detail_risk, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        txt_risk_control = (TextView) view.findViewById(R.id.risk_control);
        String risk_control = getArguments().getString("risk");
        txt_risk_control.setText(risk_control);
        super.onViewCreated(view, savedInstanceState);

    }

    public static Fragment newInstance(String risk) {
        InvestDetailRiskFragment fragment = new InvestDetailRiskFragment();
        Bundle bundle = new Bundle();
        bundle.putString("risk", risk);
        fragment.setArguments(bundle);
        return fragment;
    }


}
