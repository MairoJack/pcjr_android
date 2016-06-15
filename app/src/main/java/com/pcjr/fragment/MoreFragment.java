package com.pcjr.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.bigkoo.alertview.OnItemClickListener;
import com.pcjr.R;
import com.pcjr.activity.AboutusActivity;
import com.pcjr.activity.NewsActivity;
import com.pcjr.activity.TestActivity;
import com.pcjr.activity.WebViewActivity;
import com.pcjr.plugins.AlertView;


public class MoreFragment extends Fragment {
    public static final String TAG = MoreFragment.class.getSimpleName();

    private RelativeLayout news, aboutus, kefu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "平台公告");
                intent.putExtra("url", "https://m.pcjr.com/platformnews");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutusActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),TestActivity.class));
            }
        });
        kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("皮城金融竭诚为您服务", "服务时间：9:00-20:00", "取消", null,
                        new String[]{"拨打电话 400-101-3339"},
                        getContext(), AlertView.Style.ActionSheet, new OnItemClickListener() {
                    public void onItemClick(Object o, int position) {
                        if(position == 0) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:400-101-3339"));
                            startActivity(intent);
                        }
                    }
                }).show();
            }
        });
    }

    public static Fragment newInstance(String text) {
        MoreFragment fragment = new MoreFragment();
        return fragment;
    }
}
