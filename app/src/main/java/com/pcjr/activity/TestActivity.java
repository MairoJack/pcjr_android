
package com.pcjr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;
import com.pcjr.R;
import com.pcjr.plugins.ProgressWheel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * Created by Mario on 2016/5/5.
 */

public class TestActivity extends Activity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout sliderLayout;
    private PtrClassicFrameLayout mPtrFrame;
    private ProgressWheel progressWheel;
    private DropDownMenu mMenu;
    private ScrollView scrollView;

    private TextView but1,but2,but3,but4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.test);
        //sliderLayout = (SliderLayout)findViewById(R.id.slider3);
        progressWheel = (ProgressWheel) findViewById(R.id.pw_spinner);
        //intiSlider();
        progressWheel.setProgress(90);

        final String[] arr1=new String[]{"全部城市","北京","上海","广州","深圳"};
        final String[] arr2=new String[]{"性别","男","女"};
        final String[] arr3=new String[]{"全部年龄","10","20","30","40","50","60","70"};

        final String[] strings=new String[]{"选择城市"};

       /* mMenu=(DropDownMenu)findViewById(R.id.menu);
        mMenu.setmMenuCount(1);//Menu的个数
        mMenu.setmShowCount(6);//Menu展开list数量太多是只显示的个数
        mMenu.setShowCheck(true);//是否显示展开list的选中项
        mMenu.setmMenuTitleTextSize(16);//Menu的文字大小
        mMenu.setmMenuTitleTextColor(Color.WHITE);//Menu的文字颜色
        mMenu.setmMenuListTextSize(16);//Menu展开list的文字大小
        mMenu.setmMenuListTextColor(Color.BLACK);//Menu展开list的文字颜色
        mMenu.setmMenuBackColor(Color.parseColor("#0099CC"));//Menu的背景颜色
        mMenu.setmMenuPressedBackColor(Color.parseColor("#0099CC"));//Menu按下的背景颜色
        mMenu.setmCheckIcon(R.drawable.ico_make);//Menu展开list的勾选图片
        mMenu.setmUpArrow(R.drawable.arrow_up);//Menu默认状态的箭头
        mMenu.setmDownArrow(R.drawable.arrow_down);//Menu按下状态的箭头
        mMenu.setDefaultMenuTitle(strings);//默认未选择任何过滤的Menu title
        mMenu.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            //Menu展开的list点击事件  RowIndex：list的索引  ColumnIndex：menu的索引
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {


            }
        });

        List<String[]> items = new ArrayList<>();
        items.add(arr1);
        //items.add(arr2);
        //items.add(arr3);
        mMenu.setmMenuItems(items);*/

        //scrollView = (ScrollView) findViewById(R.id.scroll_view);
        //mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.list_view_with_empty_view_fragment_ptr_frame);

        //mPtrFrame.disableWhenHorizontalMove(true);
        //mPtrFrame.setLastUpdateTimeRelateObject(this);

        //sliderLayout = (SliderLayout) findViewById(R.id.slider);

        //initSlider();
       /* mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                // here check $mListView instead of $content
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, scrollView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });*/

        CountdownView mCvCountdownView = (CountdownView)findViewById(R.id.cv_countdown);
        mCvCountdownView.start(995550000); // Millisecond


        but1 = (TextView) findViewById(R.id.but1);
        but2 = (TextView) findViewById(R.id.but2);
        but3 = (TextView) findViewById(R.id.but3);
        but4 = (TextView) findViewById(R.id.but4);

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restButton();
                but1.setBackgroundResource(R.drawable.border_bg_orange_left_radius);
                but1.getBackground().setAlpha(255);
                but1.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restButton();
                but2.setBackgroundColor(Color.parseColor("#FF6602"));
                but2.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });
        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restButton();
                but3.setBackgroundColor(Color.parseColor("#FF6602"));
                but3.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });
        but4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restButton();
                but4.setBackgroundResource(R.drawable.border_bg_orange_right_radius);
                but4.getBackground().setAlpha(255);
                but4.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });
        super.onCreate(savedInstanceState);
    }

    public void restButton(){
        but1.getBackground().setAlpha(0);
        but2.getBackground().setAlpha(0);
        but3.getBackground().setAlpha(0);
        but4.getBackground().setAlpha(0);
        but1.setTextColor(Color.parseColor("#FF6602"));
        but2.setTextColor(Color.parseColor("#FF6602"));
        but3.setTextColor(Color.parseColor("#FF6602"));
        but4.setTextColor(Color.parseColor("#FF6602"));
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

