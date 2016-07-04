
package com.pcjinrong.pcjr.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.pcjinrong.pcjr.model.FocusImg;
import com.pcjinrong.pcjr.plugins.NetworkImageHolderView;
import com.pcjinrong.pcjr.plugins.ProgressWheel;
import com.pcjinrong.pcjr.R;

import java.util.ArrayList;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;


/**
 * 测试
 * Created by Mario on 2016/5/5.
 */

public class TestActivity extends Activity implements ViewPager.OnPageChangeListener, OnItemClickListener

{

    private PtrClassicFrameLayout mPtrFrame;
    private ProgressWheel progressWheel;
    private DropDownMenu mMenu;
    private ScrollView scrollView;
    private ConvenientBanner convenientBanner;
    private TextView but1, but2, but3, but4;

    private String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://d.3987.com/sqmy_131219/001.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.test);
        //sliderLayout = (SliderLayout)findViewById(R.id.slider3);
        //progressWheel = (ProgressWheel) findViewById(R.id.pw_spinner);
        //intiSlider();
        //progressWheel.setProgress(90);

        final String[] arr1 = new String[]{"全部城市", "北京", "上海", "广州", "深圳"};
        final String[] arr2 = new String[]{"性别", "男", "女"};
        final String[] arr3 = new String[]{"全部年龄", "10", "20", "30", "40", "50", "60", "70"};

        final String[] strings = new String[]{"选择城市"};

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
        convenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
        initSlider();
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

        CountdownView mCvCountdownView = (CountdownView) findViewById(R.id.cv_countdown);
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

    public void restButton() {
        but1.getBackground().setAlpha(0);
        but2.getBackground().setAlpha(0);
        but3.getBackground().setAlpha(0);
        but4.getBackground().setAlpha(0);
        but1.setTextColor(Color.parseColor("#FF6602"));
        but2.setTextColor(Color.parseColor("#FF6602"));
        but3.setTextColor(Color.parseColor("#FF6602"));
        but4.setTextColor(Color.parseColor("#FF6602"));
    }

    public void initSlider() {

        initImageLoader();

        FocusImg focusImg = new FocusImg();
        focusImg.setImg_url("https://m.pcjr.com/images/wapFocus/6.jpg");
        FocusImg focusImg1 = new FocusImg();
        focusImg1.setImg_url("https://m.pcjr.com/images/wapFocus/3.jpg");
        FocusImg focusImg2 = new FocusImg();
        focusImg2.setImg_url("https://m.pcjr.com/images/wapFocus/4.jpg");
        List<FocusImg> networkImages = new ArrayList<>();
        networkImages.add(focusImg);
        networkImages.add(focusImg1);
        networkImages.add(focusImg2);
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages).setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setOnPageChangeListener(this)//监听翻页事件
                .setOnItemClickListener(this);


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


    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.drawable.error_img)
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }



    @Override
    public void onItemClick(int position) {
        Toast.makeText(this,"点击了第"+position+"个", Toast.LENGTH_SHORT).show();
    }
}



