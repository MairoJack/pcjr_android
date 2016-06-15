package com.pcjr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjr.R;
import com.pcjr.adapter.PaymentPlanListViewAdapter;
import com.pcjr.common.Constant;
import com.pcjr.decorators.DotDecorator;
import com.pcjr.decorators.DotGrayDecorator;
import com.pcjr.model.PaymentPlan;
import com.pcjr.service.ApiService;
import com.pcjr.utils.DateUtil;
import com.pcjr.utils.RetrofitUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 回款计划
 * Created by Mario on 2016/5/24.
 */
public class PaymentPlanActivity extends Activity {
    private PtrClassicFrameLayout mPtrFrame;
    private LoadMoreListViewContainer loadMoreListViewContainer;
    private LinearLayout empty;

    private PaymentPlanListViewAdapter adapter;
    private MaterialCalendarView calendarView;
    private RelativeLayout back;
    private ListView listView;

    private int year, month;
    private int pageNow = 1;
    private List<PaymentPlan> list = new ArrayList<>();
    private List<PaymentPlan> day_list = new ArrayList<>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_plan);
        initView();
        initData();
    }

    public void initView() {
        empty = (LinearLayout) findViewById(R.id.empty);
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.ptr_frame);
        loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more);


        listView = (ListView) findViewById(R.id.list_view);
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        back = (RelativeLayout) findViewById(R.id.back);

        //下拉刷新
        mPtrFrame.disableWhenHorizontalMove(true);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, listView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageNow = 1;
                loadData();
            }
        });



        //上拉加载
        //loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.loadMoreFinish(false,false);
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(final LoadMoreContainer loadMoreContainer) {
                pageNow++;
                loadData();
            }
        });


        DotGrayDecorator dotGrayDecorator = new DotGrayDecorator();
        calendarView.addDecorator(dotGrayDecorator);
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                year = date.getYear();
                month = date.getMonth()+1;
                mPtrFrame.post(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.autoRefresh();
                    }
                });
            }
        });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                loadData(date.getDay());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    public void initData() {
        Date date = new Date();
        year = DateUtil.getYearOfDate(date);
        month = DateUtil.getMonthOfDate(date);
        adapter = new PaymentPlanListViewAdapter(day_list, PaymentPlanActivity.this);
        listView.setAdapter(adapter);

        //自动刷新
        mPtrFrame.post(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;

    }


    public void loadData(int day){
        day_list.clear();
        for(PaymentPlan pp :list){
            Date date = new Date(pp.getEstimated_time()*1000);
            if(DateUtil.getDayOfDate(date) == day){
                day_list.add(pp);
                adapter.notifyDataSetChanged();
            }
        }
        if (day_list.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            loadMoreListViewContainer.setVisibility(View.INVISIBLE);
        } else {
            empty.setVisibility(View.INVISIBLE);
            loadMoreListViewContainer.setVisibility(View.VISIBLE);
        }
    }
    public void loadData(){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getMemberRepaymentData(Constant.access_token, year, month,pageNow,Constant.PAGESIZE);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (pageNow == 1) {
                    day_list.clear();
                    list.clear();
                }
                mPtrFrame.refreshComplete();
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    List<PaymentPlan> temps = null;
                    if (json.get("data") != null) {
                        temps = gson.fromJson(json.get("data"), new TypeToken<List<PaymentPlan>>() {
                        }.getType());
                        list.addAll(temps);
                        day_list.addAll(temps);


                    }
                    if (day_list.isEmpty()) {
                        empty.setVisibility(View.VISIBLE);
                        loadMoreListViewContainer.setVisibility(View.INVISIBLE);
                    } else {
                        for(PaymentPlan pp:list){
                            DotDecorator dot  = new DotDecorator();
                            dot.setDate(new Date(pp.getEstimated_time()*1000));
                            calendarView.addDecorator(dot);
                        }

                        empty.setVisibility(View.INVISIBLE);
                        loadMoreListViewContainer.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                loadMoreListViewContainer.loadMoreError(1, "加载失败.");
                mPtrFrame.refreshComplete();
                Toast.makeText(PaymentPlanActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
