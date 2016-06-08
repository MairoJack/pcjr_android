package com.pcjr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjr.R;
import com.pcjr.adapter.InvestRecordsListViewAdapter;
import com.pcjr.adapter.ProductListViewAdapter;
import com.pcjr.adapter.TradeRecordsListViewAdapter;
import com.pcjr.common.Constant;
import com.pcjr.model.Announce;
import com.pcjr.model.FocusImg;
import com.pcjr.model.InvestRecords;
import com.pcjr.model.Pager;
import com.pcjr.model.Product;
import com.pcjr.model.TradeRecords;
import com.pcjr.model.Users;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 交易记录
 * Created by Mario on 2016/5/24.
 */
public class TradeRecordsActivity extends Activity implements OnRefreshListener, OnLoadMoreListener {

    private RelativeLayout back;
    private ListView listView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private TradeRecordsListViewAdapter adapter;
    private int pageNow = 1;
    private List<TradeRecords> tradeRecordses = new ArrayList<>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_records);
        initView();
    }

    public void initView(){
        swipeToLoadLayout = (SwipeToLoadLayout)findViewById(R.id.swipeToLoadLayout);
        listView = (ListView) findViewById(R.id.swipe_target);
        back = (RelativeLayout) findViewById(R.id.back);

        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        autoRefresh();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;

    }

    @Override
    public void onRefresh() {
        tradeRecordses.clear();
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
        Call<JsonObject> call = service.getMemberLogData(Constant.access_token,pageNow,Constant.PAGESIZE);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    Pager pager = null;
                    List<TradeRecords> temps = null;
                    if (json.get("pager") != null) {
                        pager = gson.fromJson(json.get("pager"), Pager.class);
                    }
                    if (json.get("data") != null) {
                        temps = gson.fromJson(json.get("data"), new TypeToken<List<TradeRecords>>() {
                        }.getType());
                    }

                    tradeRecordses.addAll(temps);
                    if(tradeRecordses.size()>0) {
                        if (adapter == null) {
                            adapter = new TradeRecordsListViewAdapter(tradeRecordses, TradeRecordsActivity.this);
                            listView.setAdapter(adapter);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
            }
        });
    }
}
