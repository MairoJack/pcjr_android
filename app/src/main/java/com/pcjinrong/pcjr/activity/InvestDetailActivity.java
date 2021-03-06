package com.pcjinrong.pcjr.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pcjinrong.pcjr.fragment.InvestDetailRecordFragment;
import com.pcjinrong.pcjr.R;
import com.pcjinrong.pcjr.adapter.TabFragmentAdapter;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.fragment.InvestDetailInfoFragment;
import com.pcjinrong.pcjr.fragment.InvestDetailRiskFragment;
import com.pcjinrong.pcjr.model.Product;
import com.pcjinrong.pcjr.plugins.IosDialog;
import com.pcjinrong.pcjr.service.ApiService;
import com.pcjinrong.pcjr.utils.DateUtil;
import com.pcjinrong.pcjr.utils.RetrofitUtils;
import com.pcjinrong.pcjr.utils.SharedPreferenceUtil;
import com.pcjinrong.pcjr.utils.ViewUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 投资产品详情
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
    private  Product product;
    private ProgressDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invest_detail);
        initView();
        initData();
    }

	private void initView() {
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
		tabLayout = (TabLayout) findViewById(R.id.invest_tab_layout);
		viewPager = (ViewPager) findViewById(R.id.invest_tab_viewpager);
        back = (RelativeLayout) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.product_name);
        btn_status = (Button) findViewById(R.id.btn_status);
        layout_cdv = (LinearLayout) findViewById(R.id.layout_cv_countdown);
        cdv = (CountdownView) findViewById(R.id.cv_countdown);

        cdv.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                layout_cdv.setVisibility(View.GONE);
                btn_status.setVisibility(View.VISIBLE);
                btn_status.setBackgroundResource(R.drawable.redbtn);
                btn_status.setText("立即投资");
                btn_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ViewUtil.isFastDoubleClick()){
                            return;
                        }
                        Intent intent;
                        if(Constant.isLogin) {
                            intent = new Intent(InvestDetailActivity.this, InvestActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("product", product);
                            intent.putExtras(bundle);
                            startActivityForResult(intent,0);
                        }else{
                            intent = new Intent(InvestDetailActivity.this, LoginActivity.class);
                            intent.putExtra("tag","invest");
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    }
                });
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

    public void initData(){
        dialog.setMessage("正在加载...");
        dialog.show();
        ApiService service = RetrofitUtils.createNoTokenApi(ApiService.class);
        Call<JsonObject> call = service.getProductDetail(getIntent().getStringExtra("id"));
        final long request_time = DateUtil.getMillisOfDate(new Date());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();

                    if (json.get("success").getAsBoolean()) {
                        product = gson.fromJson(json.get("data"), Product.class);
                        initTablayout(product);
                        title.setText(product.getName());
                        if(product.getStatus() == 1){
                            long response_time = DateUtil.getMillisOfDate(new Date());
                            long time = response_time - request_time;
                            long current_time = json.get("current_time").getAsLong()*1000 + time;
                            Date date = new Date(current_time);
                            Date pub_date = new Date(product.getPub_date()*1000);
                            try {
                                if (DateUtil.isStartDateBeforeEndDate(date, pub_date)) {
                                    if (DateUtil.getHoursOfTowDiffDate(date, pub_date) > 1) {
                                        btn_status.setBackgroundResource(R.drawable.orangebtn);
                                        btn_status.setText(DateUtil.transferLongToDate("MM月dd日 HH:mm",product.getPub_date()*1000)+"开抢");
                                    }else{
                                        btn_status.setVisibility(View.GONE);
                                        layout_cdv.setVisibility(View.VISIBLE);
                                        cdv.start(DateUtil.getMinusMillisOfDate(date,pub_date));
                                    }
                                }else{
                                    btn_status.setBackgroundResource(R.drawable.redbtn);
                                    btn_status.setText("立即投资");
                                    btn_status.setClickable(true);
                                    btn_status.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent;
                                            boolean flag = false;
                                            if (Constant.isLogin && Constant.isGestureLogin) {
                                                flag = true;
                                            } else if (!Constant.isLogin) {
                                                intent = new Intent(InvestDetailActivity.this, LoginActivity.class);
                                                intent.putExtra("tag","invest");
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            } else if (!Constant.isGestureLogin) {
                                                SharedPreferenceUtil spu = new SharedPreferenceUtil(InvestDetailActivity.this, Constant.FILE);
                                                if(spu.getOpenGesture()) {
                                                    startActivity(new Intent(InvestDetailActivity.this, GestureVerifyActivity.class));
                                                }else{
                                                    flag = true;
                                                }
                                            }

                                            if(flag) {
                                                intent = new Intent(InvestDetailActivity.this, InvestActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("product", product);
                                                intent.putExtras(bundle);
                                                startActivityForResult(intent,0);
                                                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
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
                            btn_status.setClickable(false);
                        }else{
                            btn_status.setBackgroundResource(R.drawable.graybtn);
                            btn_status.setText("项目结束");
                            btn_status.setClickable(false);
                        }
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(InvestDetailActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK){
            IosDialog.show(data.getStringExtra("error"),this);
        }
    }


    public void refreshButton(final Product product, long current_time){
        btn_status.setClickable(false);
        if(product.getStatus() == 1){
            Date date = new Date(current_time);
            Date pub_date = new Date(product.getPub_date()*1000);
            try {
                if (DateUtil.isStartDateBeforeEndDate(date, pub_date)) {
                    if (DateUtil.getHoursOfTowDiffDate(date, pub_date) > 1) {
                        btn_status.setBackgroundResource(R.drawable.orangebtn);
                        btn_status.setText(DateUtil.transferLongToDate("MM月dd日 HH:mm",product.getPub_date()*1000)+"开抢");
                    }else{
                        btn_status.setVisibility(View.GONE);
                        layout_cdv.setVisibility(View.VISIBLE);
                        cdv.start(DateUtil.getMinusMillisOfDate(date,pub_date));
                    }
                }else{
                    btn_status.setBackgroundResource(R.drawable.redbtn);
                    btn_status.setText("立即投资");
                    btn_status.setClickable(true);
                    btn_status.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(ViewUtil.isFastDoubleClick()){
                                return;
                            }
                            Intent intent;
                            boolean flag = false;
                            if (Constant.isLogin && Constant.isGestureLogin) {
                                flag = true;
                            } else if (!Constant.isLogin) {
                                intent = new Intent(InvestDetailActivity.this, LoginActivity.class);
                                intent.putExtra("tag","invest");
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            } else if (!Constant.isGestureLogin) {
                                SharedPreferenceUtil spu = new SharedPreferenceUtil(InvestDetailActivity.this, Constant.FILE);
                                if(spu.getOpenGesture()) {
                                    startActivity(new Intent(InvestDetailActivity.this, GestureVerifyActivity.class));
                                }else{
                                    flag = true;
                                }
                            }

                            if(flag) {
                                intent = new Intent(InvestDetailActivity.this, InvestActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("product", product);
                                intent.putExtras(bundle);
                                startActivityForResult(intent,0);
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
            btn_status.setClickable(false);
        }else{
            btn_status.setBackgroundResource(R.drawable.graybtn);
            btn_status.setText("项目结束");
            btn_status.setClickable(false);
        }
    }
}
