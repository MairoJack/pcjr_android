package com.pcjr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.pcjr.R;
import com.pcjr.activity.NewsActivity;


public class MoreFragment extends Fragment
{
	private RelativeLayout news,aboutus,kefu;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View newsLayout = inflater.inflate(R.layout.main_tab_more, container, false);
		return newsLayout;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		news = (RelativeLayout) view.findViewById(R.id.news);
		aboutus = (RelativeLayout) view.findViewById(R.id.aboutus);
		kefu = (RelativeLayout) view.findViewById(R.id.kefu);

		news.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), NewsActivity.class));
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

		aboutus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), NewsActivity.class));
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

		kefu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), NewsActivity.class));
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
	}
}
