package com.pcjr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pcjr.R;
import com.todddavies.components.progressbar.ProgressWheel;


/**
 * Created by Mario on 2016/5/12.
 */
public class InvestDetailInfoFragment extends Fragment{

    private ProgressWheel progressWheel;
    private float progress;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.invest_detail_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        progress = 0;
        super.onViewCreated(view, savedInstanceState);
        progressWheel = (ProgressWheel) view.findViewById(R.id.pw_spinner);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progress<=180) {
                    try {
                        progress++;
                        progressWheel.incrementProgress();
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

}
