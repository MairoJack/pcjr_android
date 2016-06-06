package com.pcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pcjr.R;
import com.pcjr.model.InvestRecords;
import com.pcjr.model.ProductTradingRecord;
import com.pcjr.utils.DateUtil;

import java.util.List;

/**
 * 产品投资记录适配器
 * Created by Mario on 2016/5/26.
 */
public class ProductTradingRecordListViewAdapter extends BaseAdapter {
    private List<ProductTradingRecord> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private static class ViewHolder{
        public TextView
                member_name,
                amount,
                join_date;
    }
    public ProductTradingRecordListViewAdapter(List<ProductTradingRecord> list, Context context) {
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
            convertView = layoutInflater.inflate(R.layout.item_record,null);

            viewHolder.member_name = (TextView)convertView.findViewById(R.id.member_name);
            viewHolder.amount = (TextView)convertView.findViewById(R.id.amount);
            viewHolder.join_date = (TextView)convertView.findViewById(R.id.join_date);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.member_name.setText(list.get(position).getMember_name());

        viewHolder.amount.setText(list.get(position).getAmount());
        viewHolder.join_date.setText(DateUtil.transferLongToDate("yyyy-MM-dd",list.get(position).getJoin_date()*1000));

        return convertView;
    }

    public void setList(List<ProductTradingRecord> list) {
        this.list = list;
    }
}
