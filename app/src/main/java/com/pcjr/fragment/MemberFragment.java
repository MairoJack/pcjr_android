package com.pcjr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.model.Users;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MemberFragment extends Fragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.usercenter, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initData();
	}

	private void initData(){
		ApiService service = RetrofitUtils.createApi(ApiService.class);
		Call<Users> callUsers = service.getUserInfo(Constant.access_token);
		callUsers.enqueue(new Callback<Users>() {
			@Override
			public void onResponse(Call<Users> call, Response<Users> response) {
				Users u = response.body();
			}

			@Override
			public void onFailure(Call<Users> call, Throwable t) {
				Log.d("Mario", "onResponse:Throwable " + t);
			}
		});
	}
}
