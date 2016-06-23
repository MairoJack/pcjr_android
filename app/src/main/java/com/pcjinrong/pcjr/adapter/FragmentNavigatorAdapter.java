package com.pcjinrong.pcjr.adapter;

import android.support.v4.app.Fragment;

/**
 * Created by Mario on 16/6/1.
 */
public interface FragmentNavigatorAdapter {

    public Fragment onCreateFragment(int position);

    public String getTag(int position);

    public int getCount();

}
