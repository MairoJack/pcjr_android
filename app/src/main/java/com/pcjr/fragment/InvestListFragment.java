package com.pcjr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjr.R;
import com.pcjr.activity.InvestDetailActivity;
import com.pcjr.adapter.ProductListViewAdapter;
import com.pcjr.adapter.RedPacketListViewAdapter;
import com.pcjr.common.Constant;
import com.pcjr.model.Pager;
import com.pcjr.model.Product;
import com.pcjr.model.RedPacket;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mario on 2016/5/12.
 */
public class InvestListFragment extends Fragment  implements OnRefreshListener, OnLoadMoreListener {
    private ListView listView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private ProductListViewAdapter adapter;
    private int type;
    private int pageNow = 1;
    private List<Product> products = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invest_list,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        listView = (ListView) view.findViewById(R.id.swipe_target);
        type = getArguments().getInt("type");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InvestDetailActivity.class);
                intent.putExtra("id",products.get(position).getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        autoRefresh();
    }

    @Override
    public void onRefresh() {
        products.clear();
        pageNow = 1;
        loadData();
    }

    public void loadData() {
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getInvestProductList(type,pageNow,8);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    Pager pager = null;
                    List<Product> temps = new ArrayList<>();
                    if (json.get("pager") != null) {
                        pager = gson.fromJson(json.get("pager"), Pager.class);
                    }
                    if (json.get("data") != null) {
                        temps = gson.fromJson(json.get("data"), new TypeToken<List<Product>>() {
                        }.getType());
                    }
                    products.addAll(temps);
                    if (products.size() > 0) {
                        if (pageNow == 1) {
                            adapter = new ProductListViewAdapter(products, getContext());
                            listView.setAdapter(adapter);
                        } else {
                            if (adapter == null) {
                                adapter = new ProductListViewAdapter(products, getContext());
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
        InvestListFragment fragment = new InvestListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }
}
