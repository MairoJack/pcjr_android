package com.pcjr.fragment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.pcjr.R;
import com.pcjr.activity.InvestDetailActivity;
import com.pcjr.adapter.ProductListViewAdapter;
import com.pcjr.model.Product;
import com.pcjr.model.Users;
import com.pcjr.service.ApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IndexFragment extends Fragment implements OnRefreshListener,OnLoadMoreListener,BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener
{

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private SwipeToLoadLayout swipeToLoadLayout;
    private SliderLayout sliderLayout;
    private ProgressDialog proDialog;
    private long lastRefreshTime;

    private RelativeLayout login,reg,invest,call;
    private ImageView img1;
    private TextView login_but;
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		View view = inflater.inflate(R.layout.main_tab_index, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        sliderLayout = (SliderLayout)view.findViewById(R.id.slider);

        Log.d("Error", "onCreateView: sds");

        intiSlider();

        List<Product> list = new ArrayList<Product>();

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
		ListAdapter adapter = new ProductListViewAdapter(list,getActivity());
		listView.setAdapter(adapter);
        listView.setFocusable(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), InvestDetailActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        });

		return view;
	}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        login = (RelativeLayout) view.findViewById(R.id.login);
        reg = (RelativeLayout) view.findViewById(R.id.reg);
        invest = (RelativeLayout) view.findViewById(R.id.invest);
        call = (RelativeLayout) view.findViewById(R.id.call);
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void intiSlider(){


        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("1", "https://www.pcjr.com/images/focus_img2.jpg");
        url_maps.put("2", "https://www.pcjr.com/images/focus_img3.jpg");
        url_maps.put("3", "https://www.pcjr.com/images/focus_img4.jpg");
        url_maps.put("4", "https://www.pcjr.com/images/focus_img5.jpg");


        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        //sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
        sliderLayout.addOnPageChangeListener(this);


    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onRefresh() {
        sliderLayout.stopAutoCycle();
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
                sliderLayout.startAutoCycle();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        sliderLayout.stopAutoCycle();
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
                sliderLayout.startAutoCycle();
            }
        }, 2000);
    }

    /*@Override
    public void onClick(View v) {
        Log.d("mario", "onClick: vvv");
        switch (v.getId()){
            case R.id.login:
                Log.d("mario", "onClick: vvv");
                transaction.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
                transaction.remove(this).add(R.id.id_content,new LoginFragment());
                break;
            case R.id.reg:
                transaction.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
                transaction.remove(this).add(R.id.id_content,new RegistFragment());
                break;
            case R.id.invest:
                transaction.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
                transaction.remove(this).add(R.id.id_content,new InvestFragment());
                break;
            case R.id.call:
                break;
        }
        transaction.commit();
    }*/
}
