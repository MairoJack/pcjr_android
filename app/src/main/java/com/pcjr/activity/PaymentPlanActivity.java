package com.pcjr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.pcjr.R;
import com.pcjr.adapter.InvestRecordsListViewAdapter;
import com.pcjr.adapter.PaymentPlanListViewAdapter;
import com.pcjr.common.Constant;
import com.pcjr.model.InvestRecords;
import com.pcjr.model.Pager;
import com.pcjr.model.PaymentPlan;
import com.pcjr.model.TradeRecords;
import com.pcjr.service.ApiService;
import com.pcjr.utils.DateUtil;
import com.pcjr.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 回款计划
 * Created by Mario on 2016/5/24.
 */
public class PaymentPlanActivity extends Activity implements OnRefreshListener, OnLoadMoreListener {

    private SwipeToLoadLayout swipeToLoadLayout;
    private PaymentPlanListViewAdapter adapter;
    private DatePicker datePicker;
    private RelativeLayout back;
    private ListView listView;

    private int year, month;
    private int pageNow = 1;
    private List<PaymentPlan> paymentPlens = new ArrayList<>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_plan);
        initView();
        initData();
    }

    public void initView() {
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        listView = (ListView) findViewById(R.id.swipe_target);
        datePicker = (DatePicker) findViewById(R.id.datepicker);
        back = (RelativeLayout) findViewById(R.id.back);

        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                year = datePicker.getYear();
                month = datePicker.getMonth() + 1;
                autoRefresh();
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
        autoRefresh();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;

    }

    @Override
    public void onRefresh() {
        paymentPlens.clear();
        pageNow = 1;
        loadData();
    }

    @Override
    public void onLoadMore() {
        pageNow++;
        loadData();
    }

    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    public void loadData(){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getMemberRepaymentData(Constant.access_token, year, month,pageNow,Constant.PAGESIZE);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    List<PaymentPlan> temps = null;
                    if (json.get("data") != null) {
                        temps = gson.fromJson(json.get("data"), new TypeToken<List<PaymentPlan>>() {
                        }.getType());
                    }
                    paymentPlens.addAll(temps);
                    if(paymentPlens.size()>0) {
                        if (adapter == null) {
                            adapter = new PaymentPlanListViewAdapter(paymentPlens, PaymentPlanActivity.this);
                            listView.setAdapter(adapter);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
            }
        });
    }
}
