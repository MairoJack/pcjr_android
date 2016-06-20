package com.pcjr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.model.BankCard;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 添加银行卡
 * Created by Mario on 2016/5/24.
 */
public class AddBankCardActivity extends Activity {
    private RelativeLayout back;
    private ImageView img_info,img_clear;
    private TextView txt_cardholder;
    private EditText txt_card_no;
    private Spinner spinner;
    private Button btn_save;
    private ApiService service;
    private List<BankCard> bankCards;
    private String bank_id;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bank_card);

        initView();
        initData();
    }

    public void initView(){
        img_info = (ImageView) findViewById(R.id.img_info);
        img_clear = (ImageView) findViewById(R.id.img_clear);
        txt_cardholder = (TextView) findViewById(R.id.txt_cardholder);
        txt_card_no = (EditText) findViewById(R.id.txt_card_no);
        spinner = (Spinner) findViewById(R.id.spinner);
        btn_save = (Button) findViewById(R.id.btn_save);
        back = (RelativeLayout) findViewById(R.id.back);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bank_id = bankCards.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        img_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(AddBankCardActivity.this)
                        .setTitleText("持卡人说明")
                        .setContentText("为了你的账户资金安全，只能绑定持卡人的银行卡。")
                        .show();
            }
        });

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_card_no.setText("");
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
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
        service = RetrofitUtils.createApi(ApiService.class);
        txt_cardholder.setText(Constant.realname);
        Call<JsonObject> call = service.getBankCardList();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Gson gson = new Gson();
                    if (json.get("data") != null) {
                        bankCards = gson.fromJson(json.get("data"), new TypeToken<List<BankCard>>() {
                        }.getType());
                        String[] mItems = new String[bankCards.size()];
                        for(int i=0;i<bankCards.size();i++){
                            mItems[i] = bankCards.get(i).getName();
                        }
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(AddBankCardActivity.this,android.R.layout.simple_spinner_item, mItems);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner .setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(AddBankCardActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void save(){
        String card_no = txt_card_no.getText().toString().trim();
        service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.addBankCard(Constant.BEARER+" "+Constant.access_token,bank_id,card_no,Constant.realname);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        finish();
                        startActivity(new Intent(AddBankCardActivity.this,BankCardActivity.class));
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }else{
                        Toast.makeText(AddBankCardActivity.this,json.get("message").getAsString(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(AddBankCardActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
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

}
