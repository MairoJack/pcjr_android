package com.pcjr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjr.R;
import com.pcjr.activity.InvestTicketDetailActivity;
import com.pcjr.adapter.InvestTicketListViewAdapter;
import com.pcjr.common.Constant;
import com.pcjr.model.InvestTicket;
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
 * 投资券
 * Created by Mario on 2016/5/12.
 */
public class InvestTicketFragment extends Fragment  {
    private PtrClassicFrameLayout mPtrFrame;
    private LoadMoreListViewContainer loadMoreListViewContainer;
    private LinearLayout empty;

    private ListView listView;
    private InvestTicketListViewAdapter adapter;
    private int type;
    private int pageNow = 1;
    private List<InvestTicket> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ic_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        empty = (LinearLayout) view.findViewById(R.id.empty);
        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_frame);
        loadMoreListViewContainer = (LoadMoreListViewContainer) view.findViewById(R.id.load_more);

        listView = (ListView) view.findViewById(R.id.list_view);
        type = getArguments().getInt("type");

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


        adapter = new InvestTicketListViewAdapter(list, getContext(),type);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),InvestTicketDetailActivity.class);
                intent.putExtra("id",list.get(position).getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }


    public void loadData(){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getInvestTicketList(Constant.access_token,type,pageNow,5);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (pageNow == 1)
                    list.clear();
                mPtrFrame.refreshComplete();
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    Pager pager = null;
                    List<InvestTicket> temps;
                    if (json.get("pager") != null) {
                        pager = gson.fromJson(json.get("pager"), Pager.class);
                    }
                    if (json.get("data") != null) {
                        temps = gson.fromJson(json.get("data"), new TypeToken<List<InvestTicket>>() {
                        }.getType());
                        list.addAll(temps);
                    }
                    int totalPage = (pager.getTotal() + pager.getPageSize() - 1) / pager.getPageSize();
                    if (pageNow >= totalPage) {
                        loadMoreListViewContainer.loadMoreFinish(false, false);
                    } else {
                        loadMoreListViewContainer.loadMoreFinish(false, true);
                    }
                    if (list.isEmpty()) {
                        empty.setVisibility(View.VISIBLE);
                        loadMoreListViewContainer.setVisibility(View.INVISIBLE);
                    } else {
                        empty.setVisibility(View.INVISIBLE);
                        loadMoreListViewContainer.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loadMoreListViewContainer.loadMoreError(1, "加载失败.");
                mPtrFrame.refreshComplete();
                Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static Fragment newInstance(int type) {
        InvestTicketFragment fragment = new InvestTicketFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setType(int type){
        this.type = type;
    }
}
