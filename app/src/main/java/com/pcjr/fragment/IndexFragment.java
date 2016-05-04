package com.pcjr.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.pcjr.R;
import com.pcjr.adapter.ProductListViewAdapter;
import com.pcjr.model.Product;
import com.pcjr.model.Users;
import com.pcjr.service.ApiService;
import com.pcjr.utils.ProgressDialogUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IndexFragment extends Fragment
{

    private ProgressDialog proDialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
       /* proDialog = ProgressDialogUtil.showSpinnerProgressDialog(
                getContext(), "正在验证登录信息");*/

		View view = inflater.inflate(R.layout.main_tab_index, container, false);
        Log.d("Error", "onCreateView: sds");



        /*List<Product> list = new ArrayList<Product>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<Users> call = service.loadUsers("basil2style");
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                Users u = response.body();
                Log.e("Error", "onResponse: " + u.getCompany());
                proDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {

            }
        });
        Product p = new Product();
        p.setName("sss");
        p.setAmount("50000");
        p.setMonth("3");
        p.setYearIncome("7");
        p.setSeries(1);
        list.add(p);
        list.add(p);
        ListView listView = (ListView) view.findViewById(R.id.list);
		ListAdapter adapter = new ProductListViewAdapter(list,inflater,getContext());
		listView.setAdapter(adapter);*/
		return view;
	}

}
