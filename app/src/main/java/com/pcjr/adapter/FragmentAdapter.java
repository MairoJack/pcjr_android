package com.pcjr.adapter;

import android.support.v4.app.Fragment;

import com.pcjr.fragment.IndexFragment;
import com.pcjr.fragment.InvestFragment;
import com.pcjr.fragment.MemberFragment;
import com.pcjr.fragment.MoreFragment;


/**
 * Created by aspsine on 16/3/31.
 */
public class FragmentAdapter implements FragmentNavigatorAdapter {

    private static final String TABS[] = {"Index", "Invest", "Login", "More"};

    @Override
    public Fragment onCreateFragment(int position) {
        if (position == 0){
            return IndexFragment.newInstance("IndexFragment");
        }else if (position == 1){
            return InvestFragment.newInstance("InvestFragment");
        }else if (position == 2){
           return MemberFragment.newInstance("MemberFragment");
        }else{
            return MoreFragment.newInstance("MoreFragment");
        }

    }

    @Override
    public String getTag(int position) {
        if (position == 0){
            return IndexFragment.TAG;
        }else if (position == 1){
            return InvestFragment.TAG;
        }else if (position == 2){
            return MemberFragment.TAG;
        }else{
            return MoreFragment.TAG;
        }
    }

    @Override
    public int getCount() {
        return TABS.length;
    }
}
