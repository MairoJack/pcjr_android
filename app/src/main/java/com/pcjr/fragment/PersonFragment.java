package com.pcjr.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pcjr.R;
import com.pcjr.adapter.PersonFragmentAdapter;
import com.pcjr.common.Constant;
import com.pcjr.plugins.FragmentNavigator;


/**
 * 个人
 */
public class PersonFragment extends Fragment {

    public static final String TAG = PersonFragment.class.getSimpleName();

    private FragmentNavigator mNavigator;


    public PersonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNavigator = new FragmentNavigator(getChildFragmentManager(), new PersonFragmentAdapter(), R.id.person_container);
        mNavigator.setDefaultPosition(0);
        mNavigator.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_tab_person, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(Constant.isLogin) {
            mNavigator.showFragment(3);
        }else{
            mNavigator.showFragment(0);
        }
        Log.i(TAG, "onActivityCreated");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigator.onSaveInstanceState(outState);
    }



    public static Fragment newInstance(String position) {
        return new PersonFragment();
    }

    public  FragmentNavigator getNavigator() {
        return mNavigator;
    }
}

