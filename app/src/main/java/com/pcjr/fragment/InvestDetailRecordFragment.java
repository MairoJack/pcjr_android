package com.pcjr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjr.R;
import com.pcjr.adapter.InvestRecordsListViewAdapter;
import com.pcjr.adapter.LetterListViewAdapter;
import com.pcjr.adapter.ProductListViewAdapter;
import com.pcjr.adapter.ProductTradingRecordListViewAdapter;
import com.pcjr.common.Constant;
import com.pcjr.model.InvestRecords;
import com.pcjr.model.Letter;
import com.pcjr.model.Pager;
import com.pcjr.model.Product;
import com.pcjr.model.ProductTradingRecord;
import com.pcjr.model.TradeRecords;
import com.pcjr.model.Users;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 产品投资记录
 * Created by Mario on 2016/5/12.
 */
public class InvestDetailRecordFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {

    private ListView listView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private TextView total;
    private ProductTradingRecordListViewAdapter adapter;
    private int pageNow = 1;
    private List<ProductTradingRecord> records = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.invest_detail_record, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        total = (TextView) view.findViewById(R.id.total);
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        listView = (ListView) view.findViewById(R.id.swipe_target);
        autoRefresh();
    }

    @Override
    public void onRefresh() {
        records.clear();
        pageNow = 1;
        loadData();
    }

    @Override
    public void onLoadMore() {
        pageNow++;
        loadData();
    }


    public void loadData(){
        Bundle bundle = getArguments();
        String id = bundle.getString("id");
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getProductTradingRecordList(id,pageNow,10);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    Pager pager = null;
                    List<ProductTradingRecord> temps = new ArrayList<>();
                    if (json.get("pager") != null) {
                        pager = gson.fromJson(json.get("pager"), Pager.class);
                        total.setText(String.valueOf(pager.getTotal()));
                    }
                    if (json.get("data") != null) {
                        temps = gson.fromJson(json.get("data"), new TypeToken<List<ProductTradingRecord>>() {
                        }.getType());
                    }
                    records.addAll(temps);
                    if(records.size()>0) {
                        if (adapter == null) {
                            adapter = new ProductTradingRecordListViewAdapter(records, getContext());
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

    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    public static Fragment newInstance(String id) {
        InvestDetailRecordFragment fragment = new InvestDetailRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }
}
