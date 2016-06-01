package com.pcjr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.JsonObject;
import com.pcjr.R;
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
public class News3Activity extends Activity implements OnRefreshListener, OnLoadMoreListener {
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

        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.list_view_with_empty_view_fragment_ptr_frame);

        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        listView = (ListView) findViewById(R.id.list_view);

        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);



        View v = LayoutInflater.from(this).inflate(R.layout.no_records,null);

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
        map.put("newstitle", "y***ss");
        map.put("newstime", "2016-05-16 13:30");
        list.add(map);list.add(map);list.add(map);list.add(map);list.add(map);list.add(map);list.add(map);list.add(map);

        list.add(map);
        list.add(map);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("newstitle", "mario");
        map.put("newstime", "2016-05-16 13:30");
        list.add(map);
        return list;
    }

    @Override
    public void onRefresh() {
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getBankCardList();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                swipeToLoadLayout.setRefreshing(false);
                if (response.body() != null) {
                    SimpleAdapter simpleAdapter = new SimpleAdapter(News3Activity.this, getData(), R.layout.item_news, new String[]{"newstitle", "newstime"},
                            new int[]{R.id.newstitle, R.id.newstime});
                    listView.setAdapter(simpleAdapter);
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
