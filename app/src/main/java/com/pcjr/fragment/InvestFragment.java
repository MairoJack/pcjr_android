package com.pcjr.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pcjr.R;
import com.pcjr.adapter.TabFragmentAdapter;
import java.util.ArrayList;
import java.util.List;

public class InvestFragment extends Fragment
{

	private TabLayout tabLayout;
	private ViewPager viewPager;
	private FragmentPagerAdapter fragmentPagerAdapter;

	private List<Fragment> fragmentList;
	private List<String> titleList;

	private Fragment investAllFragment,investSellingFragment,investSellOverFragment,investItemOverFragment;


	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View viwe = inflater.inflate(R.layout.main_tab_invest, container, false);
		initView(viwe);
		return viwe;
	}

	private void initView(View v) {
		tabLayout = (TabLayout) v.findViewById(R.id.invest_tab_layout);
		viewPager = (ViewPager) v.findViewById(R.id.invest_tab_viewpager);

        investAllFragment = new InvestAllFragment();
        investSellingFragment = new InvestSellingFragment();
        investSellOverFragment = new InvestSellOverFragment();
        investItemOverFragment = new InvestItemOverFragment();

		fragmentList = new ArrayList<>();
		fragmentList.add(investAllFragment);
		fragmentList.add(investSellingFragment);
		fragmentList.add(investSellOverFragment);
		fragmentList.add(investItemOverFragment);

		titleList = new ArrayList<>();
		titleList.add("全部");
		titleList.add("销售中");
		titleList.add("销售结束");
		titleList.add("项目结束");

		tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
		tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));
		tabLayout.addTab(tabLayout.newTab().setText(titleList.get(2)));
		tabLayout.addTab(tabLayout.newTab().setText(titleList.get(3)));

		fragmentPagerAdapter = new TabFragmentAdapter(getActivity().getSupportFragmentManager(),fragmentList,titleList);

		viewPager.setAdapter(fragmentPagerAdapter);
		tabLayout.setupWithViewPager(viewPager);
	}


}
