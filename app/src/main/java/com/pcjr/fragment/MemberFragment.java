package com.pcjr.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.pcjr.R;

import java.util.HashMap;


public class MemberFragment extends Fragment implements OnRefreshListener, BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener
{

	private SwipeToLoadLayout swipeToLoadLayout;
	private SliderLayout sliderLayout;
	private long lastRefreshTime;
	@TargetApi(Build.VERSION_CODES.M)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		View view = inflater.inflate(R.layout.main_tab_member, container, false);
		sliderLayout = (SliderLayout)view.findViewById(R.id.slider);
		swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
		swipeToLoadLayout.setOnRefreshListener(this);
		Log.d("Error", "onCreateView: sds");

		intiSlider();


		return view;
	}


	@TargetApi(Build.VERSION_CODES.M)
	public void intiSlider(){


		HashMap<String,String> url_maps = new HashMap<String, String>();
		url_maps.put("1", "https://www.pcjr.com/images/focus_img2.jpg");
		url_maps.put("2", "https://www.pcjr.com/images/focus_img3.jpg");
		url_maps.put("3", "https://www.pcjr.com/images/focus_img4.jpg");
		url_maps.put("4", "https://www.pcjr.com/images/focus_img5.jpg");


		for(String name : url_maps.keySet()){
			TextSliderView textSliderView = new TextSliderView(getActivity());
			// initialize a SliderLayout
			textSliderView
					.image(url_maps.get(name))
					.setScaleType(BaseSliderView.ScaleType.Fit)
					.setOnSliderClickListener(this);

			//add your extra information
			textSliderView.bundle(new Bundle());
			textSliderView.getBundle()
					.putString("extra",name);

			sliderLayout.addSlider(textSliderView);
		}
	}

	@Override
	public void onStop() {
		// To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
		sliderLayout.stopAutoCycle();
		super.onStop();
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onRefresh() {
		swipeToLoadLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeToLoadLayout.setRefreshing(false);
			}
		}, 2000);
	}
}
