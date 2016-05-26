package com.pcjr.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pcjr.R;
import com.pcjr.model.InvestRecords;
import com.pcjr.model.TradeRecords;
import com.pcjr.utils.DateUtil;
import com.pcjr.utils.RotateTextView;

import java.util.List;

/**
 * 投资记录适配器
 * Created by Mario on 2016/5/26.
 */
public class InvestRecordsListViewAdapter extends BaseAdapter {
    private List<InvestRecords> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private static class ViewHolder{
        public TextView
                product_name,
                repayment,
                amount,
                year_income,
                income,
                date;
    }
    public InvestRecordsListViewAdapter(List<InvestRecords> list, Context context) {
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
            convertView = layoutInflater.inflate(R.layout.item_invest_records,null);

            viewHolder.product_name = (TextView)convertView.findViewById(R.id.product_name);
            viewHolder.date = (TextView)convertView.findViewById(R.id.date);
            viewHolder.amount = (TextView)convertView.findViewById(R.id.amount);
            viewHolder.income = (TextView)convertView.findViewById(R.id.income);
            viewHolder.year_income = (TextView)convertView.findViewById(R.id.year_income);
            viewHolder.repayment = (TextView)convertView.findViewById(R.id.repayment);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        int type = list.get(position).getRepayment();
        String amount = list.get(position).getAmount();
        String interestTotal = list.get(position).getInterestTotal();
        String joinDate = DateUtil.transferLongToDate("yyyy年MM月dd日",list.get(position).getJoinDate()*1000);
        String deadline = DateUtil.transferLongToDate("yyyy年MM月dd日",list.get(position).getDeadline()*1000);
        if(type == 0){
            viewHolder.repayment.setText("一次性还本付息");
        }else if(type == 1){
            viewHolder.repayment.setText("按月付息，到期还本");
        }else{
            viewHolder.repayment.setText("等额本息");
        }
        viewHolder.product_name.setText(list.get(position).getProductName());

        viewHolder.amount.setText(amount);
        viewHolder.year_income.setText(list.get(position).getYearIncome());
        viewHolder.income.setText(String.valueOf(Double.parseDouble(amount)+Double.parseDouble(interestTotal)));
        viewHolder.date.setText(joinDate+"起投-"+deadline+"到期");

        return convertView;
    }

    public void setList(List<InvestRecords> list) {
        this.list = list;
    }
}
