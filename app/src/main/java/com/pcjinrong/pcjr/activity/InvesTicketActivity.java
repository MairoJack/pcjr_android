package com.pcjinrong.pcjr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.pcjinrong.pcjr.adapter.TabFragmentAdapter;
import com.pcjinrong.pcjr.R;
import com.pcjinrong.pcjr.fragment.InvestTicketFragment;
import com.pcjinrong.pcjr.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 投资券
 * Created by Mario on 2016/5/24.
 */
public class InvesTicketActivity extends FragmentActivity {
    private RelativeLayout back,tips;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;

    private List<Fragment> fragmentList;
    private List<String> titleList;

    private Fragment iCUnusedFragment,iCUsedFragment,iCExpiredFragment;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invest_ticket);
        initView();
    }

    public void initView(){

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);

        fragmentList = new ArrayList<>();
        fragmentList.add(InvestTicketFragment.newInstance(0));
        fragmentList.add(InvestTicketFragment.newInstance(1));
        fragmentList.add(InvestTicketFragment.newInstance(2));

        titleList = new ArrayList<>();
        titleList.add("未使用");
        titleList.add("已使用");
        titleList.add("已过期");

        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(2)));

        fragmentPagerAdapter = new TabFragmentAdapter(getSupportFragmentManager(),fragmentList,titleList);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        back = (RelativeLayout) findViewById(R.id.back);
        tips = (RelativeLayout) findViewById(R.id.tips);
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
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
                Intent intent = new Intent(InvesTicketActivity.this, WebViewActivity.class);
                intent.putExtra("title", "温馨提示");
                intent.putExtra("url", "https://m.pcjr.com/appdeal/coupons");
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
