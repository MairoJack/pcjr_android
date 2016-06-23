package com.pcjinrong.pcjr.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.pcjr.R;
import com.pcjinrong.pcjr.model.TradeRecords;
import com.pcjinrong.pcjr.utils.DateUtil;
import com.pcjinrong.pcjr.utils.RotateTextView;

import java.util.List;

/**
 * 交易记录适配器
 * Created by Mario on 2016/5/26.
 */
public class TradeRecordsListViewAdapter extends BaseAdapter {
    private List<TradeRecords> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private static class ViewHolder{
        public RotateTextView rtv;
        public TextView
                remark,
                date,
                amount,
                fee,
                balance;
    }
    public TradeRecordsListViewAdapter(List<TradeRecords> list, Context context) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_trade_records,null);

            viewHolder.remark = (TextView)convertView.findViewById(R.id.remark);
            viewHolder.date = (TextView)convertView.findViewById(R.id.date);
            viewHolder.amount = (TextView)convertView.findViewById(R.id.amount);
            viewHolder.fee = (TextView)convertView.findViewById(R.id.fee);
            viewHolder.balance = (TextView)convertView.findViewById(R.id.balance);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(list.get(position).getDirection() == 0){
            viewHolder.amount.setTextColor(Color.parseColor("#fd4c4a"));
            viewHolder.fee.setTextColor(Color.parseColor("#fd4c4a"));
            viewHolder.amount.setText("- "+list.get(position).getAmount()+"元");
            viewHolder.fee.setText("- "+list.get(position).getFee()+"元");
        }else{
            viewHolder.amount.setTextColor(Color.parseColor("#0FB61E"));
            viewHolder.fee.setTextColor(Color.parseColor("#0FB61E"));
            viewHolder.amount.setText("+ "+list.get(position).getAmount()+"元");
            viewHolder.fee.setText("+ "+list.get(position).getFee()+"元");
        }
        viewHolder.remark.setText(list.get(position).getRemark());
        viewHolder.date.setText(DateUtil.transferLongToDate(DateUtil.DATE_FORMAT_YYYY_MM_DD,list.get(position).getJoin_date()*1000));
        viewHolder.balance.setText(String.valueOf(list.get(position).getBalance())+"元");

        return convertView;
    }

    public void setList(List<TradeRecords> list) {
        this.list = list;
    }
}
