package com.pcjr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.pcjr.common.Constant;
import com.pcjr.model.InvestRecords;
import com.pcjr.model.Pager;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mario on 2016/5/20.
 */
public class NewsActivity extends Activity implements OnRefreshListener, OnLoadMoreListener {
    private PtrClassicFrameLayout mPtrFrame;
    private SwipeToLoadLayout swipeToLoadLayout;
    private ListView listView;
    private TextView nodata;
    private LinearLayout empty;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);
        initView();
    }

    public void initView(){

        nodata = (TextView) findViewById(R.id.list_view_with_empty_view_fragment_empty_view);
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.list_view_with_empty_view_fragment_ptr_frame);

        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        listView = (ListView) findViewById(R.id.list_view);

        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);

        empty = (LinearLayout) findViewById(R.id.empty);
        empty.setVisibility(View.VISIBLE);
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

        return list;
    }

    @Override
    public void onRefresh() {
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getMemberInvestData(Constant.access_token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                swipeToLoadLayout.setRefreshing(false);
                if (response.body() != null) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    Pager pager = null;
                    List<InvestRecords> investRecordses = null;
                    if (json.get("pager") != null) {
                        pager = gson.fromJson(json.get("pager"), Pager.class);
                    }
                    if (json.get("data") != null) {
                        investRecordses = gson.fromJson(json.get("data"), new TypeToken<List<InvestRecords>>() {
                        }.getType());
                    }

                    SimpleAdapter simpleAdapter = new SimpleAdapter(NewsActivity.this, getData(), R.layout.item_news, new String[]{"newstitle", "newstime"},
                            new int[]{R.id.newstitle, R.id.newstime});
                    listView.setAdapter(simpleAdapter);
                    listView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
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
