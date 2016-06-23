package com.pcjinrong.pcjr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;


import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.pcjinrong.pcjr.activity.InvestDetailActivity;
import com.pcjinrong.pcjr.activity.MainActivity;
import com.pcjinrong.pcjr.activity.WebViewActivity;
import com.pcjinrong.pcjr.adapter.ProductListViewAdapter;
import com.pcjinrong.pcjr.model.FocusImg;
import com.pcjinrong.pcjr.model.Product;
import com.pcjinrong.pcjr.plugins.NetworkImageHolderView;
import com.pcjinrong.pcjr.service.ApiService;
import com.pcjinrong.pcjr.utils.DateUtil;
import com.pcjr.R;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.model.Announce;
import com.pcjinrong.pcjr.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndexFragment extends Fragment{

    public static final String TAG = IndexFragment.class.getSimpleName();

    private ConvenientBanner sliderLayout, sliderLayoutSmall;
    private PtrClassicFrameLayout mPtrFrame;

    private RelativeLayout cpyg, dcxa, gtma, zlbh;
    private LinearLayout all_invest;
    private ScrollView scrollView;
    private ListView listView;
    private TextSwitcher announce;
    private List<Announce> announces;
    private int mCounter;
    private  List<Product> products = new ArrayList<>();
    private  List<FocusImg> focusImgs;
    private  List<FocusImg> midFocusImgs;

    private ApiService service;
    private ProductListViewAdapter adapter;
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
        service = RetrofitUtils.createApi(ApiService.class);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_frame);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        listView = (ListView) view.findViewById(R.id.list);
        announce = (TextSwitcher) view.findViewById(R.id.announce);
        cpyg = (RelativeLayout) view.findViewById(R.id.cpyg);
        dcxa = (RelativeLayout) view.findViewById(R.id.dcxa);
        gtma = (RelativeLayout) view.findViewById(R.id.gtma);
        zlbh = (RelativeLayout) view.findViewById(R.id.zlbh);
        all_invest = (LinearLayout) view.findViewById(R.id.all_invest);
        sliderLayout = (ConvenientBanner) view.findViewById(R.id.slider);
        sliderLayoutSmall = (ConvenientBanner) view.findViewById(R.id.slider_small);



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
                intent.putExtra("url","https://m.pcjinrong.pcjinrong.com.pcjinrong.com.pcjinrong.pcjr.com/notice");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        dcxa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity  = (MainActivity) getContext();
                mainActivity.setCurrentTab(1);
                Constant.TYPE = 1;
            }
        });
        gtma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity  = (MainActivity) getContext();
                mainActivity.setCurrentTab(1);
                Constant.TYPE = 2;

            }
        });
        zlbh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity  = (MainActivity) getContext();
                mainActivity.setCurrentTab(1);
                Constant.TYPE = 3;

            }
        });
        all_invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity  = (MainActivity) getContext();
                mainActivity.setCurrentTab(1);
                Constant.TYPE = 0;
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

        //下拉刷新
        mPtrFrame.disableWhenHorizontalMove(true);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, scrollView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refreshData();
            }
        });

        initData();

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

        Call<JsonObject> call = service.getIndexProductList();
        final long request_time = DateUtil.getMillisOfDate(new Date());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                long response_time = DateUtil.getMillisOfDate(new Date());
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    products = gson.fromJson(json.get("data"), new TypeToken<List<Product>>() {
                    }.getType());
                    long time = response_time - request_time;
                    long current_time = json.get("current_time").getAsLong()*1000 + time;
                    adapter = new ProductListViewAdapter(products,getContext(),current_time);
                    listView.setAdapter(adapter);
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

        initImageLoader();

        sliderLayout.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, focusImgs).setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(new SliderLayoutOnItemClick(focusImgs));


        sliderLayoutSmall.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, midFocusImgs).setPageIndicator(new int[]{R.mipmap.ic_page_red_indicator_focused, R.mipmap.ic_page_red_indicator})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(new SliderLayoutSmallOnItemClick(midFocusImgs));
    }

    @Override
    public void onStop() {
        sliderLayout.stopTurning();
        sliderLayoutSmall.stopTurning();
        super.onStop();
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.drawable.ic_default_adimage)
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getActivity().getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    class SliderLayoutOnItemClick implements OnItemClickListener{
        private List<FocusImg> focusImgs;
        public SliderLayoutOnItemClick(List<FocusImg> focusImgs){
            this.focusImgs = focusImgs;
        }
        @Override
        public void onItemClick(int position) {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("url",focusImgs.get(position).getUrl());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    class SliderLayoutSmallOnItemClick implements OnItemClickListener{
        private List<FocusImg> focusImgs;
        public SliderLayoutSmallOnItemClick(List<FocusImg> focusImgs){
            this.focusImgs = focusImgs;
        }
        @Override
        public void onItemClick(int position) {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("url",focusImgs.get(position).getUrl());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }


    @Override
    public void onPause() {
        handler.removeCallbacks(announcesRunnable);
        sliderLayout.stopTurning();
        sliderLayoutSmall.stopTurning();
        super.onPause();
    }

    @Override
    public void onResume() {
        handler.post(announcesRunnable);
        sliderLayout.startTurning(5000);
        sliderLayoutSmall.startTurning(5000);
        super.onResume();
    }

    public static Fragment newInstance(String text) {
        IndexFragment fragment = new IndexFragment();
        return fragment;
    }


    public void refreshData(){

        handler.removeCallbacks(announcesRunnable);
        sliderLayout.stopTurning();
        sliderLayoutSmall.stopTurning();

        Call<JsonObject> callImg = service.getIndexFocusInfo();
        callImg.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    if (json.get("top_focus_img") != null) {
                        focusImgs = gson.fromJson(json.get("top_focus_img"), new TypeToken<List<FocusImg>>() {
                        }.getType());
                    }
                    if (json.get("middle_focus_img") != null) {
                        midFocusImgs = gson.fromJson(json.get("middle_focus_img"), new TypeToken<List<FocusImg>>() {
                        }.getType());
                    }
                    if (json.get("announce") != null) {
                        announces = gson.fromJson(json.get("announce"), new TypeToken<List<Announce>>() {
                        }.getType());
                        mCounter = announces.size();
                    }

                    initSlider(focusImgs,midFocusImgs);
                    handler.post(announcesRunnable);
                    sliderLayout.startTurning(5000);
                    sliderLayoutSmall.startTurning(5000);
                }

            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Mario", "onResponse:Throwable:"+t.getMessage());
            }
        });

        Call<JsonObject> call = service.getIndexProductList();
        final long request_time = DateUtil.getMillisOfDate(new Date());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                long response_time = DateUtil.getMillisOfDate(new Date());
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    products = gson.fromJson(json.get("data"), new TypeToken<List<Product>>() {
                    }.getType());
                    long time = response_time - request_time;
                    long current_time = json.get("current_time").getAsLong()*1000 + time;
                    adapter = new ProductListViewAdapter(products,getContext(),current_time);
                    listView.setAdapter(adapter);
                    mPtrFrame.refreshComplete();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mPtrFrame.refreshComplete();
                Toast.makeText(getActivity(),"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
