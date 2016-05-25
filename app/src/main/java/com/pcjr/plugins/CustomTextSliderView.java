package com.pcjr.plugins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

/**
 * Created by Mario on 2016/5/25.
 */
public class CustomTextSliderView extends BaseSliderView {
    public CustomTextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(com.daimajia.slider.library.R.layout.render_type_default, null);
        ImageView target = (ImageView) v.findViewById(com.daimajia.slider.library.R.id.daimajia_slider_image);
        bindEventAndShow(v, target);
        return v;
    }
}

