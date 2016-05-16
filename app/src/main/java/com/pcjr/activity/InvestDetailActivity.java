package com.pcjr.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pcjr.R;
import com.pcjr.adapter.TabFragmentAdapter;
import com.pcjr.fragment.InvestDetailInfoFragment;
import com.pcjr.fragment.InvestDetailRecordFragment;
import com.pcjr.fragment.InvestDetailRiskFragment;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Mario on 2016/5/16.
 */
public class InvestDetailActivity extends FragmentActivity
{

	private TabLayout tabLayout;
	private ViewPager viewPager;
	private FragmentPagerAdapter fragmentPagerAdapter;

	private List<Fragment> fragmentList;
	private List<String> titleList;

	private Fragment investDetailInfoFragment,investDetailRiskFragment,investDetailRecordFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invest_detail);
        initView();
    }

	private void initView() {
		tabLayout = (TabLayout) findViewById(R.id.invest_tab_layout);
		viewPager = (ViewPager) findViewById(R.id.invest_tab_viewpager);

		investDetailInfoFragment = new InvestDetailInfoFragment();
		investDetailRiskFragment = new InvestDetailRiskFragment();
		investDetailRecordFragment = new InvestDetailRecordFragment();


		fragmentList = new ArrayList<>();
		fragmentList.add(investDetailInfoFragment);
		fragmentList.add(investDetailRiskFragment);
		fragmentList.add(investDetailRecordFragment);

		titleList = new ArrayList<>();
		titleList.add("项目情况");
		titleList.add("风控措施");
		titleList.add("投资记录");

		tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
		tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));
		tabLayout.addTab(tabLayout.newTab().setText(titleList.get(2)));

		fragmentPagerAdapter = new TabFragmentAdapter(getSupportFragmentManager(),fragmentList,titleList);

		viewPager.setAdapter(fragmentPagerAdapter);
		tabLayout.setupWithViewPager(viewPager);
	}


}
