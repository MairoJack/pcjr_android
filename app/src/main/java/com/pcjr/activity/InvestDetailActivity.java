package com.pcjr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pcjr.R;
import com.pcjr.adapter.TabFragmentAdapter;
import com.pcjr.common.Constant;
import com.pcjr.fragment.InvestDetailInfoFragment;
import com.pcjr.fragment.InvestDetailRecordFragment;
import com.pcjr.fragment.InvestDetailRiskFragment;
import com.pcjr.fragment.LoginFragment;
import com.pcjr.model.Product;
import com.pcjr.service.ApiService;
import com.pcjr.utils.DateUtil;
import com.pcjr.utils.RetrofitUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mario on 2016/5/16.
 */
public class InvestDetailActivity extends FragmentActivity
{

    private RelativeLayout back;
    private TextView title;
    private Button btn_status;
	private TabLayout tabLayout;
	private ViewPager viewPager;
	private FragmentPagerAdapter fragmentPagerAdapter;

	private List<Fragment> fragmentList;
	private List<String> titleList;
    private LinearLayout layout_cdv;
    private CountdownView cdv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invest_detail);
        initView();
        initData();
    }

	private void initView() {
		tabLayout = (TabLayout) findViewById(R.id.invest_tab_layout);
		viewPager = (ViewPager) findViewById(R.id.invest_tab_viewpager);
        back = (RelativeLayout) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.product_name);
        btn_status = (Button) findViewById(R.id.btn_status);
        layout_cdv = (LinearLayout) findViewById(R.id.layout_cv_countdown);
        cdv = (CountdownView) findViewById(R.id.cv_countdown);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
	}

    public void initData(){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getProductDetail(getIntent().getStringExtra("id"));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    final Product product;
                    if (json.get("success").getAsBoolean()) {
                        product = gson.fromJson(json.get("data"), Product.class);
                        initTablayout(product);
                        title.setText(product.getName());
                        if(product.getStatus() == 1){
                            Date date = new Date();
                            Date pub_date = new Date(product.getPub_date()*1000);
                            try {
                                if (DateUtil.isStartDateBeforeEndDate(date, pub_date)) {
                                    if (DateUtil.getHoursOfTowDiffDate(date, pub_date) > 2) {
                                        btn_status.setBackgroundResource(R.drawable.orangebtn);
                                        btn_status.setText(DateUtil.transferLongToDate("MM月dd日 HH:mm",product.getPub_date()*1000)+"开抢");
                                    }else{
                                        btn_status.setVisibility(View.GONE);
                                        layout_cdv.setVisibility(View.VISIBLE);
                                        cdv.start(DateUtil.getMinusMillisOfDate(date,pub_date));
                                    }
                                }else{
                                    btn_status.setBackgroundResource(R.drawable.redbtn);
                                    btn_status.setText("立即加入");
                                    btn_status.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent;
                                            if(Constant.isLogin) {
                                                intent = new Intent(InvestDetailActivity.this, InvestActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("product", product);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }else{
                                                intent = new Intent(InvestDetailActivity.this, LoginActivity.class);
                                                intent.putExtra("tag","invest");
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
                                            }
                                        }
                                    });
                                }
                            }catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }else if(product.getStatus() == 2 || product.getStatus() == 3){
                            btn_status.setBackgroundResource(R.drawable.bluebtn);
                            btn_status.setText("募集成功");
                        }else{
                            btn_status.setBackgroundResource(R.drawable.graybtn);
                            btn_status.setText("项目结束");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(InvestDetailActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initTablayout(Product product){
        fragmentList = new ArrayList<>();
        fragmentList.add(InvestDetailInfoFragment.newInstance(product));
        fragmentList.add(InvestDetailRiskFragment.newInstance(product.getRisk_control()));
        fragmentList.add(InvestDetailRecordFragment.newInstance(product.getId()));

        titleList = new ArrayList<>();
        titleList.add("项目情况");
        titleList.add("风控措施");
        titleList.add("投资记录");

        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(2)));

        fragmentPagerAdapter = new TabFragmentAdapter(getSupportFragmentManager(),fragmentList,titleList);

        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
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

}
