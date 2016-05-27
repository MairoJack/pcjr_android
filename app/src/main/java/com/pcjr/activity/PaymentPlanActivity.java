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
import com.pcjr.service.ApiService;
import com.pcjr.utils.DateUtil;
import com.pcjr.utils.RetrofitUtils;

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
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getMemberRepaymentData(Constant.access_token, year, month);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                swipeToLoadLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    List<PaymentPlan> paymentPlens = null;
                    if (json.get("data") != null) {
                        paymentPlens = gson.fromJson(json.get("data"), new TypeToken<List<PaymentPlan>>() {
                        }.getType());
                    }

                    paymentPlens.addAll(paymentPlens);
                    paymentPlens.addAll(paymentPlens);
                    paymentPlens.addAll(paymentPlens);
                    adapter = new PaymentPlanListViewAdapter(paymentPlens, PaymentPlanActivity.this);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                swipeToLoadLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
            }
        }, 2000);
    }

    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }
}
