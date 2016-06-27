package com.pcjinrong.pcjr.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by Mario on 2016/6/27.
 */
public abstract class BaseFragment extends Fragment {
    protected  boolean isVisible;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }

    protected abstract void lazyLoad();

}
