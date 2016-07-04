package com.pcjinrong.pcjr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjinrong.pcjr.R;
import com.pcjinrong.pcjr.adapter.BankCardListViewAdapter;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.model.BankCard;
import com.pcjinrong.pcjr.service.ApiService;
import com.pcjinrong.pcjr.utils.RetrofitUtils;
import com.pcjinrong.pcjr.utils.ViewUtil;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 银行卡
 * Created by Mario on 2016/5/24.
 */
public class BankCardActivity extends Activity {
    private PtrClassicFrameLayout mPtrFrame;
    private RelativeLayout back;
    private Button btn_addbankcard;
    private ListView listView;
    private BankCardListViewAdapter adapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_card);
        initView();
        initData();
    }

    public void initView(){
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.ptr_frame);
        listView = (ListView) findViewById(R.id.list_view);
        back = (RelativeLayout) findViewById(R.id.back);
        btn_addbankcard = (Button) findViewById(R.id.btn_addbankcard);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        btn_addbankcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ViewUtil.isFastDoubleClick()){
                    return;
                }
                startActivityForResult(new Intent(BankCardActivity.this, AddBankCardActivity.class),Constant.REQUSET);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
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
                initData();
            }
        });

    }

    public void initData(){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getMemberBankCardInfo(Constant.access_token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mPtrFrame.refreshComplete();
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    List<BankCard> bankCards = null;
                    if (json.get("data") != null) {
                        bankCards = gson.fromJson(json.get("data"), new TypeToken<List<BankCard>>() {
                        }.getType());
                    }
                    if (json.get("realName") != null) {
                        Constant.realname = json.get("realName").getAsString();
                    }
                    adapter = new BankCardListViewAdapter(bankCards, BankCardActivity.this,json.get("realName").getAsString());
                    listView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mPtrFrame.refreshComplete();
                Toast.makeText(BankCardActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
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
        if(requestCode == Constant.REQUSET && resultCode == RESULT_OK){
            initData();
        }
    }
}
