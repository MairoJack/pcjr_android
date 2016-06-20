package com.pcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
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
 * 银行卡适配器
 * Created by Mario on 2016/5/26.
 */
public class BankCardListViewAdapter extends BaseAdapter {
    private List<BankCard> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private String realName;
    private static class ViewHolder{
        public TextView
                bank,
                card_no,
                status,
                delete,
                realname;
    }
    public BankCardListViewAdapter(List<BankCard> list, Context context,String realName) {
        this.list = list;
        this.context = context;
        this.realName = realName;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder  viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_bank_card,null);

            viewHolder.bank = (TextView)convertView.findViewById(R.id.bank);
            viewHolder.card_no = (TextView)convertView.findViewById(R.id.card_no);
            viewHolder.status = (TextView)convertView.findViewById(R.id.status);
            viewHolder.realname = (TextView)convertView.findViewById(R.id.realname);
            viewHolder.delete = (TextView)convertView.findViewById(R.id.delete);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.bank.setText(list.get(position).getBank());
        viewHolder.card_no.setText(list.get(position).getCard_no());
        viewHolder.status.setText("已认证");
        viewHolder.realname.setText(realName);
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确认删除尾号为"+list.get(position).getCard_no().substring(list.get(position).getCard_no().length()-4)+"的银行卡吗?")
                        .setConfirmText("确认")
                        .setCancelText("取消")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                delete(position,v);
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        }).
                        show();


            }
        });

        return convertView;
    }

    public void delete(final int position, final View v){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.delBankCard(Constant.BEARER+" "+Constant.access_token,list.get(position).getId());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        list.remove(position);
                        Toast.makeText(context,json.get("message").getAsString(),Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }else{
                        Toast.makeText(context,json.get("message").getAsString(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setList(List<BankCard> list) {
        this.list = list;
    }
}
