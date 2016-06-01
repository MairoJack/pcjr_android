package com.pcjr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.JsonObject;
import com.pcjr.R;
import com.pcjr.model.FocusImg;
import com.pcjr.plugins.CustomTextSliderView;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mario on 2016/5/20.
 */
public class NewsActivity extends Activity {
    private PtrClassicFrameLayout mPtrFrame;
    private ListView listView;
    private LinearLayout empty;
    private SliderLayout sliderLayout;
    private ScrollView scrollView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news2);
        initView();
    }

    public void initView(){

        empty = (LinearLayout) findViewById(R.id.empty);
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.list_view_with_empty_view_fragment_ptr_frame);
        scrollView = (ScrollView) findViewById(R.id.rotate_header_scroll_view);
        listView = (ListView) findViewById(R.id.list_view_with_empty_view_fragment_list_view);

        mPtrFrame.disableWhenHorizontalMove(true);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        listView.setVisibility(View.INVISIBLE);
        empty.setVisibility(View.VISIBLE);
        sliderLayout = (SliderLayout) findViewById(R.id.slider);

        initSlider();
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                // here check $mListView instead of $content
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, scrollView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                updateData();
            }
        });

    }

    public void initSlider() {

        List<FocusImg> focusImgs = new ArrayList<>();
        FocusImg img = new FocusImg();
        img.setImg_url("http://m.pcjinrong.test/images/wapFocus/6.jpg");
        focusImgs.add(img);
        img.setImg_url("http://m.pcjinrong.test/images/wapFocus/3.jpg");
        focusImgs.add(img);
        img.setImg_url("http://m.pcjinrong.test/images/wapFocus/2.jpg");
        focusImgs.add(img);
        if(focusImgs!=null && focusImgs.size()>0) {
            for (FocusImg focusImg : focusImgs) {
                CustomTextSliderView textSliderView = new CustomTextSliderView(this);
                textSliderView
                        .image(focusImg.getImg_url())
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("url", focusImg.getUrl());
                sliderLayout.addSlider(textSliderView);
            }
            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
            sliderLayout.setDuration(4000);

        }

    }
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("newstitle", "J***g");
        map.put("newstime", "2016-05-16 13:30");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("newstitle", "a***7");
        map.put("newstime", "2016-05-16 13:30");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("newstitle", "y***6");
        map.put("newstime", "2016-05-16 13:30");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("newstitle", "y***2");
        map.put("newstime", "2016-05-16 13:30");
        list.add(map);list.add(map);list.add(map);list.add(map);list.add(map);list.add(map);list.add(map);list.add(map);

        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("newstitle", "mario");
        map.put("newstime", "2016-05-16 13:30");
        list.add(map);
        return list;
    }

    public void updateData() {
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getBankCardList();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {

                    SimpleAdapter simpleAdapter = new SimpleAdapter(NewsActivity.this, getData(), R.layout.item_news, new String[]{"newstitle", "newstime"},
                            new int[]{R.id.newstitle, R.id.newstime});
                    listView.setAdapter(simpleAdapter);
                    listView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                    mPtrFrame.refreshComplete();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }


}
