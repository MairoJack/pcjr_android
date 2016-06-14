package com.pcjr.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjr.R;
import com.pcjr.activity.InvestDetailActivity;
import com.pcjr.activity.MainActivity;
import com.pcjr.activity.WebViewActivity;
import com.pcjr.adapter.ProductListViewAdapter;
import com.pcjr.common.Constant;
import com.pcjr.model.Announce;
import com.pcjr.model.FocusImg;
import com.pcjr.model.Product;
import com.pcjr.plugins.CustomTextSliderView;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndexFragment extends Fragment implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener {

    public static final String TAG = IndexFragment.class.getSimpleName();

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private SliderLayout sliderLayout, sliderLayoutSmall;
    private ProgressDialog dialog;
    private long lastRefreshTime;

    private RelativeLayout cpyg, dcxa, gtma, zlbh;
    private LinearLayout all_invest;
    private ImageView img1;
    private TextView login_but;
    private ListView listView;
    private TextSwitcher announce;
    private List<Announce> announces;
    private int mCounter;
    private  List<Product> products;
    private Handler handler = new Handler();

    private Runnable announcesRunnable = new Runnable() {
        public void run() {
            if(announces !=null && announces.size()>0) {
                handler.postDelayed(this, 3000);
                announce.setInAnimation(AnimationUtils.loadAnimation(
                        getContext(), R.anim.slide_up_in));
                announce.setOutAnimation(AnimationUtils.loadAnimation(
                        getContext(), R.anim.slide_up_out));
                mCounter = mCounter >= announces.size() - 1 ? 0 : mCounter + 1;
                announce.setText(announces.get(mCounter).getTitle());
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_tab_index, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        dialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = (ListView) view.findViewById(R.id.list);
        announce = (TextSwitcher) view.findViewById(R.id.announce);
        cpyg = (RelativeLayout) view.findViewById(R.id.cpyg);
        dcxa = (RelativeLayout) view.findViewById(R.id.dcxa);
        gtma = (RelativeLayout) view.findViewById(R.id.gtma);
        zlbh = (RelativeLayout) view.findViewById(R.id.zlbh);
        all_invest = (LinearLayout) view.findViewById(R.id.all_invest);
        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        sliderLayoutSmall = (SliderLayout) view.findViewById(R.id.slider_small);
        /*swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setHorizontalScrollBarEnabled(true);*/

        initData();
        announce.setFactory(new TextSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(getContext());
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.text));
                tv.setTextSize(16);
                return tv;
            }
        });

        cpyg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title","产品预告");
                intent.putExtra("url","https://m.pcjr.com/notice");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        dcxa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity  = (MainActivity) getContext();
                mainActivity.setCurrentTab(1);
            }
        });
        gtma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity  = (MainActivity) getContext();
                mainActivity.setCurrentTab(1);
            }
        });
        zlbh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity  = (MainActivity) getContext();
                mainActivity.setCurrentTab(1);
            }
        });
        all_invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity  = (MainActivity) getContext();
                mainActivity.setCurrentTab(1);
            }
        });

        announce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url",announces.get(mCounter).getUrl());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InvestDetailActivity.class);
                intent.putExtra("id",products.get(position).getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


        super.onViewCreated(view, savedInstanceState);


    }


    /**
     * 初始化数据
     */
    private void initData(){
        Constant constant = (Constant) getActivity().getApplication();
        initSlider(constant.getFocusImgs(),constant.getMidFocusImgs());
        announces = constant.getAnnounces();
        mCounter = constant.getmCounter();

        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getIndexProductList();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    products = gson.fromJson(json.get("data"), new TypeToken<List<Product>>() {
                    }.getType());
                    ListAdapter adapter = new ProductListViewAdapter(products, getActivity());
                    listView.setAdapter(adapter);
                    listView.setFocusable(false);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getActivity(),"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化图片轮播
     * @param focusImgs
     * @param midFocusImgs
     */
    public void initSlider(List<FocusImg> focusImgs,List<FocusImg> midFocusImgs) {

        if(focusImgs!=null && focusImgs.size()>0) {
            for (FocusImg focusImg : focusImgs) {
                CustomTextSliderView textSliderView = new CustomTextSliderView(getActivity());
                textSliderView
                        .image(focusImg.getImg_url())
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("url", focusImg.getUrl());
                sliderLayout.addSlider(textSliderView);
            }
            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setDuration(4000);
            sliderLayout.addOnPageChangeListener(this);

        }

        if(midFocusImgs!=null && midFocusImgs.size()>0) {
            for (FocusImg focusImg : midFocusImgs) {
                CustomTextSliderView textSliderView = new CustomTextSliderView(getActivity());
                textSliderView
                        .image(focusImg.getImg_url())
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("url", focusImg.getUrl());
                sliderLayoutSmall.addSlider(textSliderView);
            }
        }

        sliderLayoutSmall.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayoutSmall.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayoutSmall.setDuration(4000);
        sliderLayoutSmall.addOnPageChangeListener(this);
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("url",String.valueOf(slider.getBundle().get("url")));
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
    public void onPause() {
        handler.removeCallbacks(announcesRunnable);
        super.onPause();
    }

    @Override
    public void onResume() {
        handler.post(announcesRunnable);
        super.onResume();
    }

    public static Fragment newInstance(String text) {
        IndexFragment fragment = new IndexFragment();
        return fragment;
    }
}
