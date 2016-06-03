package com.pcjr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjr.R;
import com.pcjr.adapter.InvestTicketListViewAdapter;
import com.pcjr.adapter.RedPacketListViewAdapter;
import com.pcjr.common.Constant;
import com.pcjr.model.InvestTicket;
import com.pcjr.model.Pager;
import com.pcjr.model.RedPacket;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 红包
 * Created by Mario on 2016/5/12.
 */
public class RedPacketFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {

    private ListView listView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RedPacketListViewAdapter adapter;
    private int type;
    private int pageNow = 1;
    private List<RedPacket> redPackets = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ic_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        listView = (ListView) view.findViewById(R.id.swipe_target);
        type = getArguments().getInt("type");
        autoRefresh();
    }

    @Override
    public void onRefresh() {
        redPackets.clear();
        pageNow = 1;
        loadData();
    }

    public void loadData() {
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getRedPacketList(Constant.access_token, type, pageNow,Constant.PAGESIZE);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    Pager pager = null;
                    List<RedPacket> temps = new ArrayList<>();
                    if (json.get("pager") != null) {
                        pager = gson.fromJson(json.get("pager"), Pager.class);
                    }
                    if (json.get("data") != null) {
                        temps = gson.fromJson(json.get("data"), new TypeToken<List<RedPacket>>() {
                        }.getType());
                    }
                    redPackets.addAll(temps);
                    if (redPackets.size() > 0) {
                        if (pageNow == 1) {
                            adapter = new RedPacketListViewAdapter(redPackets, getContext(),type);
                            listView.setAdapter(adapter);
                        } else {
                            if (adapter == null) {
                                adapter = new RedPacketListViewAdapter(redPackets, getContext(),type);
                                listView.setAdapter(adapter);
                            }
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

    public static Fragment newInstance(int type) {
        RedPacketFragment fragment = new RedPacketFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }
}
