package com.pcjr.activity;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;

import com.google.gson.JsonObject;
import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.fragment.ForgetFragment;
import com.pcjr.fragment.IndexFragment;
import com.pcjr.fragment.InvestFragment;
import com.pcjr.fragment.LoginFragment;
import com.pcjr.fragment.MemberFragment;
import com.pcjr.fragment.MoreFragment;
import com.pcjr.fragment.RegistFragment;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;
import com.pcjr.utils.SharedPreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mario on 2016/5/16.
 */
public class MainActivity extends FragmentActivity implements OnClickListener {

    private IndexFragment indexFragment;
    private InvestFragment investFragment;
    private MemberFragment memberFragment;
    private MoreFragment moreFragment;
    private LoginFragment loginFragment;

    private LinearLayout mTabBtnIndex;
    private LinearLayout mTabBtnInvest;
    private LinearLayout mTabBtnMember;
    private LinearLayout mTabBtnMore;


    private FragmentManager fragmentManager;

    private static boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);

    }


    private void initView() {

        mTabBtnIndex = (LinearLayout) findViewById(R.id.id_tab_bottom_index);
        mTabBtnInvest = (LinearLayout) findViewById(R.id.id_tab_bottom_invest);
        mTabBtnMember = (LinearLayout) findViewById(R.id.id_tab_bottom_member);
        mTabBtnMore = (LinearLayout) findViewById(R.id.id_tab_bottom_more);

        mTabBtnIndex.setOnClickListener(this);
        mTabBtnInvest.setOnClickListener(this);
        mTabBtnMember.setOnClickListener(this);
        mTabBtnMore.setOnClickListener(this);

    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     */
    private void setTabSelection(int index) {
        // 重置按钮
        resetBtn();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                ((ImageButton) mTabBtnIndex.findViewById(R.id.btn_tab_bottom_index))
                        .setImageResource(R.drawable.index_pressed);
                if (indexFragment == null) {
                    indexFragment = new IndexFragment();
                    transaction.add(R.id.id_content, indexFragment);
                } else {
                    transaction.show(indexFragment);
                }
                break;
            case 1:
                ((ImageButton) mTabBtnInvest.findViewById(R.id.btn_tab_bottom_invest))
                        .setImageResource(R.drawable.invest_pressed);
                if (investFragment == null) {
                    investFragment = new InvestFragment();
                    transaction.add(R.id.id_content, investFragment);
                } else {
                    transaction.show(investFragment);
                }
                break;
            case 2:
                ((ImageButton) mTabBtnMember.findViewById(R.id.btn_tab_bottom_member))
                        .setImageResource(R.drawable.member_pressed);

                if (Constant.isLogin) {
                    memberFragment = new MemberFragment();
                    transaction.add(R.id.id_content, memberFragment);
                } else {
                    loginFragment = new LoginFragment();
                    transaction.add(R.id.id_content, loginFragment);
                }
                break;
            case 3:
                ((ImageButton) mTabBtnMore.findViewById(R.id.btn_tab_bottom_more))
                        .setImageResource(R.drawable.more_pressed);
                if (moreFragment == null) {
                    moreFragment = new MoreFragment();
                    transaction.add(R.id.id_content, moreFragment);
                } else {
                    transaction.show(moreFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void resetBtn() {
        ((ImageButton) mTabBtnIndex.findViewById(R.id.btn_tab_bottom_index))
                .setImageResource(R.drawable.index_normal);
        ((ImageButton) mTabBtnInvest.findViewById(R.id.btn_tab_bottom_invest))
                .setImageResource(R.drawable.invest_normal);
        ((ImageButton) mTabBtnMember.findViewById(R.id.btn_tab_bottom_member))
                .setImageResource(R.drawable.member_normal);
        ((ImageButton) mTabBtnMore.findViewById(R.id.btn_tab_bottom_more))
                .setImageResource(R.drawable.more_normal);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (fragmentManager.getFragments() != null) {
            for (Fragment f : fragmentManager.getFragments()) {
                if (f instanceof RegistFragment || f instanceof LoginFragment || f instanceof ForgetFragment || f instanceof MemberFragment) {
                    transaction.remove(f);
                }
            }
        }
        if (indexFragment != null) {
            transaction.hide(indexFragment);
        }
        if (investFragment != null) {
            transaction.hide(investFragment);
        }
        if (loginFragment != null) {
            transaction.hide(loginFragment);
        }
        if (moreFragment != null) {
            transaction.hide(moreFragment);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_tab_bottom_index:
                setTabSelection(0);
                break;
            case R.id.id_tab_bottom_invest:
                setTabSelection(1);
                break;
            case R.id.id_tab_bottom_member:
                setTabSelection(2);
                break;
            case R.id.id_tab_bottom_more:
                setTabSelection(3);
                break;

            default:
                break;
        }
    }

    public LinearLayout getmTabBtnInvest() {
        return mTabBtnInvest;
    }


    @Override
    protected void onResume() {
        super.onResume();
        tryLogin();
    }

    private void tryLogin() {
        final SharedPreferenceUtil spu = new SharedPreferenceUtil(this, Constant.FILE);
        if(!spu.getisFirst()) {
            ApiService service = RetrofitUtils.createApi(ApiService.class);
            Call<JsonObject> call = service.getAccessToken("password", spu.getUsernam(), spu.getPassword(), Constant.CLIENTID, Constant.CLIENTSECRET);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        JsonObject json = response.body();
                        if (json.get("access_token") != null) {
                            String accessToken = json.get("access_token").getAsString();
                            String refreshToken = json.get("refresh_token").getAsString();
                            spu.setAccessToken(accessToken);
                            spu.setRefresToken(refreshToken);
                            Constant.isLogin = true;
                            Constant.access_token = accessToken;
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("Mario", "onResponse:Throwable:" + t);
                }
            });
        }
    }
}
