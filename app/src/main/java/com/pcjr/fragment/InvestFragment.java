package com.pcjr.fragment;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pcjr.R;
import com.pcjr.adapter.ProductListViewAdapter;
import com.pcjr.model.Product;

import java.util.ArrayList;
import java.util.List;

public class InvestFragment extends Fragment
{

	private ViewPager mPager;//页卡内容
	private List<View> listViews; // Tab页面列表
	private ImageView cursor;// 动画图片
	private TextView sell_all, sell_ing, sell_over,item_over;// 页卡头标
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View viwe = inflater.inflate(R.layout.main_tab_invest, container, false);
		InitImageView(viwe);
		InitTextView(viwe);
		InitViewPager(viwe,inflater);
		return viwe;
	}

	private void InitTextView(View v) {
		sell_all = (TextView) v.findViewById(R.id.sell_all);
		sell_ing = (TextView) v.findViewById(R.id.sell_ing);
		sell_over = (TextView) v.findViewById(R.id.sell_over);
		item_over = (TextView) v.findViewById(R.id.item_over);

		sell_all.setOnClickListener(new MyOnClickListener(0));
		sell_ing.setOnClickListener(new MyOnClickListener(1));
		sell_over.setOnClickListener(new MyOnClickListener(2));
	}

	private void InitViewPager(View v,LayoutInflater inflater) {
		mPager = (ViewPager) v.findViewById(R.id.vPager);
		listViews = new ArrayList<View>();
		ListView listView = (ListView) inflater.inflate(R.layout.invest_list, null);

        ListAdapter adapter = new ProductListViewAdapter(getData(),inflater,getActivity());
        listView.setAdapter(adapter);
		listViews.add(listView);
		listViews.add(inflater.inflate(R.layout.invest_list, null));
		listViews.add(inflater.inflate(R.layout.invest_list, null));
        listViews.add(inflater.inflate(R.layout.invest_list, null));

		mPager.setAdapter(new MyPagerAdapter(listViews,inflater));
		mPager.setCurrentItem(0);
		mPager.addOnPageChangeListener(new MyOnPageChangeListener());
	}

	private void InitImageView(View v) {
		cursor = (ImageView) v.findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.underline)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 4 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;
        public LayoutInflater inflater;
		public MyPagerAdapter(List<View> mListViews,LayoutInflater inflater) {
			this.mListViews = mListViews;
            this.inflater = inflater;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(mListViews.get(position));
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(mListViews.get(position),0);
            /*ListView list = (ListView) mListViews.get(position);
            ListAdapter adapter = new ProductListViewAdapter(getData(),inflater,getActivity());
            list.setAdapter(adapter);*/
            getData();
            return mListViews.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}




	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量
		int three = one * 3;// 页卡1 -> 页卡3 偏移量


		@Override
		public void onPageSelected(int position) {
			Animation animation = null;

			switch (position) {
				case 0:
					if (currIndex == 1) {
						animation = new TranslateAnimation(one, 0, 0, 0);
					} else if (currIndex == 2) {
						animation = new TranslateAnimation(two, 0, 0, 0);
					}
					break;
				case 1:
					if (currIndex == 0) {
						animation = new TranslateAnimation(offset, one, 0, 0);
					} else if (currIndex == 2) {
						animation = new TranslateAnimation(two, one, 0, 0);
					}
					break;
				case 2:
					if (currIndex == 0) {
						animation = new TranslateAnimation(offset, two, 0, 0);
					} else if (currIndex == 1) {
						animation = new TranslateAnimation(one, two, 0, 0);
					}
					break;
				case 3:
					if (currIndex == 0) {
						animation = new TranslateAnimation(offset, three, 0, 0);
					} else if (currIndex == 1) {
						animation = new TranslateAnimation(one, three, 0, 0);
					}
					break;
			}
			currIndex = position;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

    public List<Product> getData(){
        List<Product> list = new ArrayList<Product>();
        Product p = new Product();
        p.setName("sss");
        p.setAmount("50000");
        p.setMonth("3");
        p.setYearIncome("7");
        p.setSeries(1);
        list.add(p);
        list.add(p);

        Log.d("Mario", "getData: ");
        return list;
    }
}
