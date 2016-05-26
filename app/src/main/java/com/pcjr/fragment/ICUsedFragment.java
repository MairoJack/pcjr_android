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
import com.pcjr.R;
import com.pcjr.adapter.ProductListViewAdapter;
import com.pcjr.model.Users;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 投资券-已使用
 * Created by Mario on 2016/5/12.
 */
public class ICUsedFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {

    private ListView listView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private ProductListViewAdapter adapter;

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
        autoRefresh();
    }

    @Override
    public void onRefresh() {
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<Users> call = service.loadUsers("basil2style");
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                Users u = response.body();
                swipeToLoadLayout.setRefreshing(false);
                
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
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
