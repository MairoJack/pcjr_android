package com.pcjinrong.pcjr.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;
import com.pcjinrong.pcjr.model.InvestRecords;
import com.pcjinrong.pcjr.service.ApiService;
import com.pcjr.R;
import com.pcjinrong.pcjr.adapter.InvestRecordsListViewAdapter;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.model.Pager;
import com.pcjinrong.pcjr.utils.RetrofitUtils;
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
 * 投资记录
 * Created by Mario on 2016/5/24.
 */
public class InvestRecordsActivity extends Activity{
    private PtrClassicFrameLayout mPtrFrame;
    private LoadMoreListViewContainer loadMoreListViewContainer;
    private LinearLayout empty;
    private DropDownMenu mMenu;
    private RelativeLayout back;
    private ListView listView;
    private InvestRecordsListViewAdapter adapter;
    private int pageNow = 1;
    private int type = 0;
    private List<InvestRecords> list = new ArrayList<>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invest_records);
        initView();
        initMenu();
    }

    public void initView() {
        empty = (LinearLayout) findViewById(R.id.empty);
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.ptr_frame);
        loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more);

        listView = (ListView) findViewById(R.id.list_view);
        back = (RelativeLayout) findViewById(R.id.back);
        mMenu = (DropDownMenu) findViewById(R.id.menu);

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
                empty.setVisibility(View.INVISIBLE);
                loadMoreListViewContainer.setVisibility(View.INVISIBLE);
                pageNow = 1;
                loadData();
            }
        });
        //自动刷新
        mPtrFrame.post(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        });


        //上拉加载
        //loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(final LoadMoreContainer loadMoreContainer) {
                pageNow++;
                loadData();
            }
        });


        adapter = new InvestRecordsListViewAdapter(list, InvestRecordsActivity.this);
        listView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    public void initMenu() {

        final String[] arr=new String[]{"全部记录", "正在募集", "募集成功", "正在回款", "回款完毕"};

        final String[] strings=new String[]{"投资记录"};

        mMenu=(DropDownMenu)findViewById(R.id.menu);
        mMenu.setmMenuCount(1);//Menu的个数
        mMenu.setmShowCount(6);//Menu展开list数量太多是只显示的个数
        mMenu.setShowCheck(true);//是否显示展开list的选中项
        mMenu.setmMenuTitleTextSize(20);//Menu的文字大小
        mMenu.setmMenuTitleTextColor(Color.WHITE);//Menu的文字颜色
        mMenu.setmMenuPressedTitleTextColor(Color.WHITE);
        mMenu.setmMenuListTextSize(16);//Menu展开list的文字大小
        mMenu.setmMenuListTextColor(Color.BLACK);//Menu展开list的文字颜色
        mMenu.setmMenuBackColor(Color.parseColor("#FF6602"));//Menu的背景颜色
        mMenu.setmMenuPressedBackColor(Color.parseColor("#FF6602"));//Menu按下的背景颜色
        mMenu.setmCheckIcon(R.drawable.ico_make);//Menu展开list的勾选图片
        mMenu.setmUpArrow(R.drawable.arrow_up);//Menu默认状态的箭头
        mMenu.setmDownArrow(R.drawable.arrow_down);//Menu按下状态的箭头
        mMenu.setDefaultMenuTitle(strings);//默认未选择任何过滤的Menu title
        mMenu.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            //Menu展开的list点击事件  RowIndex：list的索引  ColumnIndex：menu的索引
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {
                type = RowIndex;
                mPtrFrame.post(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.autoRefresh();
                    }
                });
            }
        });

        List<String[]> items = new ArrayList<>();
        items.add(arr);
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





    public void loadData(){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getMemberInvestData(Constant.access_token,type,pageNow,Constant.PAGESIZE);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(pageNow == 1)
                    list.clear();
                mPtrFrame.refreshComplete();
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    Pager pager = null;
                    List<InvestRecords> temps;
                    if (json.get("pager") != null) {
                        pager = gson.fromJson(json.get("pager"), Pager.class);
                    }
                    if (json.get("data") != null) {
                        temps = gson.fromJson(json.get("data"), new TypeToken<List<InvestRecords>>() {
                        }.getType());
                        list.addAll(temps);
                    }

                    int totalPage = (pager.getTotal() + pager.getPageSize() -1) / pager.getPageSize();
                    if(pageNow>=totalPage){
                        loadMoreListViewContainer.loadMoreFinish(false,false);
                    }else{
                        loadMoreListViewContainer.loadMoreFinish(false,true);
                    }
                    if(list.isEmpty()){
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
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loadMoreListViewContainer.loadMoreError(1,"加载失败.");
                mPtrFrame.refreshComplete();
                Toast.makeText(InvestRecordsActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
