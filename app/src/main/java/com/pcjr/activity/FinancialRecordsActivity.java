package com.pcjr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.pcjr.R;
;

/**
 * 资金记录
 * Created by Mario on 2016/5/20.
 */
public class FinancialRecordsActivity extends Activity {
    private RelativeLayout back;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.financial_records);
        initView();
    }

    public void initView(){
        back = (RelativeLayout) findViewById(R.id.back);
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

}
