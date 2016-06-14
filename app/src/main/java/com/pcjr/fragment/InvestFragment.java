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

	public static final String TAG = InvestFragment.class.getSimpleName();

	private TabLayout tabLayout;
	private ViewPager viewPager;
	private FragmentPagerAdapter fragmentPagerAdapter;

	private List<Fragment> fragmentList;
	private List<String> titleList;


	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View viwe = inflater.inflate(R.layout.main_tab_invest, container, false);
		initView(viwe);
		return viwe;
	}

	private void initView(View v) {
		tabLayout = (TabLayout) v.findViewById(R.id.invest_tab_layout);
		viewPager = (ViewPager) v.findViewById(R.id.invest_tab_viewpager);

		fragmentList = new ArrayList<>();
		fragmentList.add(InvestListFragment.newInstance(0));
		fragmentList.add(InvestListFragment.newInstance(1));
		fragmentList.add(InvestListFragment.newInstance(2));
		fragmentList.add(InvestListFragment.newInstance(3));
	;

		titleList = new ArrayList<>();
		titleList.add("全部");
		titleList.add("大城小爱");
		titleList.add("国泰民安");
		titleList.add("珠联璧合");

		tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)),false);
		tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)),true );
		tabLayout.addTab(tabLayout.newTab().setText(titleList.get(2)),false);
		tabLayout.addTab(tabLayout.newTab().setText(titleList.get(3)),false);
		fragmentPagerAdapter = new TabFragmentAdapter(getActivity().getSupportFragmentManager(),fragmentList,titleList);

		viewPager.setAdapter(fragmentPagerAdapter);
		viewPager.setOffscreenPageLimit(4);
		tabLayout.setupWithViewPager(viewPager);
	}

	public static Fragment newInstance(String text) {
		InvestFragment fragment = new InvestFragment();
		return fragment;
	}

}
