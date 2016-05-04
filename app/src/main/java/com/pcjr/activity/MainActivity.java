package com.pcjr.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;

import com.pcjr.R;
import com.pcjr.fragment.IndexFragment;
import com.pcjr.fragment.InvestFragment;
import com.pcjr.fragment.MemberFragment;
import com.pcjr.fragment.MoreFragment;

public class MainActivity extends FragmentActivity
{

    List<Fragment> mFragments = new ArrayList<Fragment>();

	private LinearLayout mTabBtnIndex;
	private LinearLayout mTabBtnInvest;
	private LinearLayout mTabBtnMember;
	private LinearLayout mTabBtnMore;


    private ViewPager  mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{


		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

		initView();

        tabClick();

		
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override
			public int getCount()
			{
				return mFragments.size();
			}
			@Override
			public Fragment getItem(int position)
			{
				return mFragments.get(position);
			}
		};
		
		mViewPager.setAdapter(mAdapter);


		mViewPager.addOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int position)
			{

				resetTabBtn();
				switch (position)
				{
				case 0:
					((ImageButton) mTabBtnIndex.findViewById(R.id.btn_tab_bottom_index))
							.setImageResource(R.drawable.index_pressed);
					break;
				case 1:
					((ImageButton) mTabBtnInvest.findViewById(R.id.btn_tab_bottom_invest))
							.setImageResource(R.drawable.invest_pressed);
					break;
				case 2:
					((ImageButton) mTabBtnMember.findViewById(R.id.btn_tab_bottom_member))
							.setImageResource(R.drawable.member_pressed);
					break;
				case 3:
					((ImageButton) mTabBtnMore.findViewById(R.id.btn_tab_bottom_more))
							.setImageResource(R.drawable.more_pressed);
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{

			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
			}
		});

	}

	protected void resetTabBtn()
	{

		((ImageButton) mTabBtnIndex.findViewById(R.id.btn_tab_bottom_index))
				.setImageResource(R.drawable.index_normal);
		((ImageButton) mTabBtnInvest.findViewById(R.id.btn_tab_bottom_invest))
				.setImageResource(R.drawable.invest_normal);
		((ImageButton) mTabBtnMember.findViewById(R.id.btn_tab_bottom_member))
				.setImageResource(R.drawable.member_normal);
		((ImageButton) mTabBtnMore.findViewById(R.id.btn_tab_bottom_more))
				.setImageResource(R.drawable.more_normal);
	}

	private void initView()
	{

		mTabBtnIndex = (LinearLayout) findViewById(R.id.id_tab_bottom_index);
		mTabBtnInvest = (LinearLayout) findViewById(R.id.id_tab_bottom_invest);
		mTabBtnMember = (LinearLayout) findViewById(R.id.id_tab_bottom_member);
		mTabBtnMore = (LinearLayout) findViewById(R.id.id_tab_bottom_more);

		IndexFragment indexFragment = new IndexFragment();
		InvestFragment investFragment = new InvestFragment();
		MemberFragment memberFragment = new MemberFragment();
		MoreFragment moreFragment = new MoreFragment();
		mFragments.add(indexFragment);
		mFragments.add(investFragment);
		mFragments.add(memberFragment);
		mFragments.add(moreFragment);
	}


    public void tabClick() {
        mTabBtnIndex.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });

        mTabBtnInvest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });

        mTabBtnMember.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
            }
        });

        mTabBtnMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(3);
            }
        });
    }
}
