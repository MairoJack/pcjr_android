package com.pcjr.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
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
import com.pcjr.model.InvestTicket;
import com.pcjr.plugins.ColoredSnackbar;
import com.pcjr.service.ApiService;
import com.pcjr.utils.DateUtil;
import com.pcjr.utils.RetrofitUtils;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 投资券适配器
 * Created by Mario on 2016/5/26.
 */
public class InvestTicketListViewAdapter extends BaseAdapter {
    private List<InvestTicket> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private static class ViewHolder{
        public TextView
                amount,
                msg,
                title,
                end_time;
    }
    public InvestTicketListViewAdapter(List<InvestTicket> list, Context context) {
        this.list = list;
        this.context = context;
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
            convertView = layoutInflater.inflate(R.layout.item_invest_ticket,null);

            viewHolder.amount = (TextView)convertView.findViewById(R.id.amount);
            viewHolder.msg = (TextView)convertView.findViewById(R.id.msg);
            viewHolder.title = (TextView)convertView.findViewById(R.id.title);
            viewHolder.end_time = (TextView)convertView.findViewById(R.id.end_time);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.amount.setText(list.get(position).getAmount());
        viewHolder.msg.setText("满 "+list.get(position).getReach_amount()+" 元返 "+list.get(position).getAmount()+" 元");
        viewHolder.title.setText("来源："+list.get(position).getTitle());
        viewHolder.end_time.setText("有效期至："+ DateUtil.transferLongToDate("yyyy-MM-dd",list.get(position).getEnd_time()*1000));

        return convertView;
    }

    public void setList(List<InvestTicket> list) {
        this.list = list;
    }
}
