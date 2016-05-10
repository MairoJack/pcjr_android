
package com.pcjr.activity;

import android.app.Activity;
import android.os.Bundle;


import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.pcjr.R;

import java.util.HashMap;


/**
 * Created by Mario on 2016/5/5.
 */

public class TestActivity extends Activity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout sliderLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.test);
        sliderLayout = (SliderLayout)findViewById(R.id.slider3);
        intiSlider();
        super.onCreate(savedInstanceState);
    }

    public void intiSlider() {


        HashMap<String, String> url_maps = new HashMap<String, String>();
        url_maps.put("1", "https://www.pcjr.com/images/focus_img2.jpg");
        url_maps.put("2", "https://www.pcjr.com/images/focus_img3.jpg");
        url_maps.put("3", "https://www.pcjr.com/images/focus_img4.jpg");
        url_maps.put("4", "https://www.pcjr.com/images/focus_img5.jpg");


        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            //textSliderView.bundle(new Bundle());

            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
       // sliderLayout.addOnPageChangeListener(this);
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
    public void onSliderClick(BaseSliderView slider) {

    }
}

