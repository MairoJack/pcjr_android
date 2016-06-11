package com.pcjr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjr.R;
import com.pcjr.adapter.LetterListViewAdapter;
import com.pcjr.common.Constant;
import com.pcjr.model.Letter;
import com.pcjr.model.Pager;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

import java.util.ArrayList;
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
 * 消息中心
 * Created by Mario on 2016/5/24.
 */
public class MsgCenterActivity extends Activity{

    private PtrClassicFrameLayout mPtrFrame;
    private LoadMoreListViewContainer loadMoreListViewContainer;
    private LinearLayout empty;

    private ListView listView;
    private LetterListViewAdapter adapter;
    private RelativeLayout back;
    private int pageNow = 1;
    private List<Letter> letters = new ArrayList<>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_center);
        initView();
    }

    public void initView(){
        empty = (LinearLayout) findViewById(R.id.empty);
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.ptr_frame);
        loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more);

        listView = (ListView) findViewById(R.id.list_view);
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
        //自动刷新
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 1000);


        //上拉加载
        //loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(final LoadMoreContainer loadMoreContainer) {
                pageNow++;
                loadData();
            }
        });


        adapter = new LetterListViewAdapter(letters, MsgCenterActivity.this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MsgCenterActivity.this,MsgDetailActivity.class);
                intent.putExtra("id",letters.get(position).getId());
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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




    public void loadData(){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        //Call<JsonObject> call = service.getLetterList(Constant.access_token,0,pageNow,Constant.PAGESIZE);
        Call<JsonObject> call = service.getLetterList(pageNow,Constant.PAGESIZE);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(pageNow == 1)
                    letters.clear();
                mPtrFrame.refreshComplete();
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    Pager pager = null;
                    List<Letter> temps = new ArrayList<>();
                    if (json.get("pager") != null) {
                        pager = gson.fromJson(json.get("pager"), Pager.class);
                    }
                    if (!json.get("data").isJsonNull()) {
                        temps = gson.fromJson(json.get("data"), new TypeToken<List<Letter>>() {
                        }.getType());
                        letters.addAll(temps);
                    }
                    int totalPage = (pager.getTotal() + pager.getPageSize() -1) / pager.getPageSize();
                    if(pageNow>=totalPage){
                        loadMoreListViewContainer.loadMoreFinish(false,false);
                    }else{
                        loadMoreListViewContainer.loadMoreFinish(false,true);
                    }
                    if(letters.isEmpty()){
                        empty.setVisibility(View.VISIBLE);
                        loadMoreListViewContainer.setVisibility(View.INVISIBLE);
                    }else{
                        empty.setVisibility(View.INVISIBLE);
                        loadMoreListViewContainer.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                loadMoreListViewContainer.loadMoreError(1,"加载失败.");
                mPtrFrame.refreshComplete();
                Toast.makeText(MsgCenterActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
