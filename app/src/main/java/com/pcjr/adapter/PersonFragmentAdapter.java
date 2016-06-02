package com.pcjr.adapter;

import android.support.v4.app.Fragment;

import com.pcjr.fragment.ForgetFragment;
import com.pcjr.fragment.LoginFragment;
import com.pcjr.fragment.MemberFragment;
import com.pcjr.fragment.RegistFragment;


/**
 * Created by aspsine on 16/4/3.
 */
public class PersonFragmentAdapter implements FragmentNavigatorAdapter {

    public static final String[] TABS = {"login", "regist","forget","member"};

    @Override
    public Fragment onCreateFragment(int position) {
        if (position == 0){
            return LoginFragment.newInstance("LoginFragment");
        }else if (position == 1){
            return RegistFragment.newInstance("RegistFragment");
        }else if (position == 2){
            return ForgetFragment.newInstance("ForgetFragment");
        }else{
            return MemberFragment.newInstance("MemberFragment");
        }
    }

    @Override
    public String getTag(int position) {
        if (position == 0){
            return LoginFragment.TAG;
        }else if (position == 1){
            return RegistFragment.TAG;
        }else if (position == 2){
            return ForgetFragment.TAG;
        }else{
            return MemberFragment.TAG;
        }
    }

    @Override
    public int getCount() {
        return TABS.length;
    }
}
