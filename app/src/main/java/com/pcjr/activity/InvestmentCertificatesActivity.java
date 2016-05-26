package com.pcjr.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.pcjr.R;
import com.pcjr.adapter.TabFragmentAdapter;
import com.pcjr.fragment.ICExpiredFragment;
import com.pcjr.fragment.ICUnusedFragment;
import com.pcjr.fragment.ICUsedFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * 投资券
 * Created by Mario on 2016/5/24.
 */
public class InvestmentCertificatesActivity extends FragmentActivity {
    private RelativeLayout back;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;

    private List<Fragment> fragmentList;
    private List<String> titleList;

    private Fragment iCUnusedFragment,iCUsedFragment,iCExpiredFragment;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.investment_certificates);
        initView();
    }

    public void initView(){

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);

        iCUnusedFragment = new ICUnusedFragment();
        iCUsedFragment = new ICUsedFragment();
        iCExpiredFragment = new ICExpiredFragment();

        fragmentList = new ArrayList<>();
        fragmentList.add(iCUnusedFragment);
        fragmentList.add(iCUsedFragment);
        fragmentList.add(iCExpiredFragment);

        titleList = new ArrayList<>();
        titleList.add("未使用");
        titleList.add("已使用");
        titleList.add("已过期");

        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(2)));

        fragmentPagerAdapter = new TabFragmentAdapter(getSupportFragmentManager(),fragmentList,titleList);

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

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
