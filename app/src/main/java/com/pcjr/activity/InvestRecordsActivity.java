package com.pcjr.activity;

import android.app.Activity;
import android.graphics.Color;
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
import com.jayfang.dropdownmenu.DropDownMenu;
import com.pcjr.R;
import com.pcjr.adapter.InvestRecordsListViewAdapter;
import com.pcjr.adapter.TradeRecordsListViewAdapter;
import com.pcjr.common.Constant;
import com.pcjr.model.InvestRecords;
import com.pcjr.model.Pager;
import com.pcjr.model.TradeRecords;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

;import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 投资记录
 * Created by Mario on 2016/5/24.
 */
public class InvestRecordsActivity extends Activity implements OnRefreshListener, OnLoadMoreListener {
    private RelativeLayout back;
    private ListView listView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private InvestRecordsListViewAdapter adapter;
    private DropDownMenu mMenu;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invest_records);
        initView();
        initMenu();
    }

    public void initView() {
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        listView = (ListView) findViewById(R.id.swipe_target);
        back = (RelativeLayout) findViewById(R.id.back);
        mMenu = (DropDownMenu) findViewById(R.id.drop_down_menu);
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

    public void initMenu() {

        String[] menuTitls = new String[]{"全部记录"};
        String[] mnuItems = new String[]{"全部记录", "正在募集", "募集成功", "正在回款", "回款完毕"};

        mMenu.setmMenuCount(1);
        mMenu.setmShowCount(6);
        mMenu.setShowCheck(true);
        mMenu.setmMenuTitleTextSize(16);
        mMenu.setmMenuTitleTextColor(Color.parseColor("#777777"));
        mMenu.setmMenuListTextSize(16);
        mMenu.setmMenuListTextColor(Color.BLACK);
        mMenu.setmMenuBackColor(Color.parseColor("#eeeeee"));
        mMenu.setmMenuPressedBackColor(Color.WHITE);
        mMenu.setmMenuPressedTitleTextColor(Color.BLACK);
        mMenu.setmCheckIcon(R.drawable.ico_make);
        mMenu.setmUpArrow(R.drawable.arrow_up);
        mMenu.setmDownArrow(R.drawable.arrow_down);
        mMenu.setDefaultMenuTitle(menuTitls);
        mMenu.setShowDivider(true);
        mMenu.setmMenuListBackColor(Color.parseColor("#FFFFFF"));
        mMenu.setmMenuListSelectorRes(R.color.white);
        mMenu.setmArrowMarginTitle(20);
        List<String[]> items = new ArrayList<>();
        items.add(mnuItems);
        mMenu.setmMenuItems(items);
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

                    InvestRecordsListViewAdapter adapter = new InvestRecordsListViewAdapter(investRecordses, InvestRecordsActivity.this);
                    listView.setAdapter(adapter);
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
