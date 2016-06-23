package com.pcjinrong.pcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pcjinrong.pcjr.model.PaymentPlan;
import com.pcjinrong.pcjr.utils.DateUtil;
import com.pcjr.R;

import java.util.List;

/**
 * 回款计划适配器
 * Created by Mario on 2016/5/26.
 */
public class PaymentPlanListViewAdapter extends BaseAdapter {
    private List<PaymentPlan> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private static class ViewHolder{
        public TextView
                product_name,
                amount,
                date,
                status;
        public ImageView icon;
    }
    public PaymentPlanListViewAdapter(List<PaymentPlan> list, Context context) {
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
            convertView = layoutInflater.inflate(R.layout.item_payment_plan,null);

            viewHolder.product_name = (TextView)convertView.findViewById(R.id.product_name);
            viewHolder.date = (TextView)convertView.findViewById(R.id.date);
            viewHolder.amount = (TextView)convertView.findViewById(R.id.amount);
            viewHolder.icon = (ImageView)convertView.findViewById(R.id.icon);
            viewHolder.status = (TextView)convertView.findViewById(R.id.status);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        int status = list.get(position).getStatus();
        String estimated_interest = list.get(position).getEstimated_interest();
        String estimated_capital = list.get(position).getEstimated_capital();
        if(status == 0){
            viewHolder.date.setText(DateUtil.transferLongToDate("yyyy-MM-dd",list.get(position).getEstimated_time()*1000));
            viewHolder.amount.setText("未回款");
            viewHolder.icon.setImageResource(R.mipmap.error);
        }else if(status == 1){
            viewHolder.date.setText(DateUtil.transferLongToDate("yyyy-MM-dd",list.get(position).getActual_time()*1000));
            viewHolder.amount.setText("已回款");
            viewHolder.icon.setImageResource(R.mipmap.right);
        }
        viewHolder.product_name.setText(list.get(position).getProduct_name());
        viewHolder.amount.setText(String.format("%.2f",Double.parseDouble(estimated_interest)+Double.parseDouble(estimated_capital))+"元");

        return convertView;
    }

    public void setList(List<PaymentPlan> list) {
        this.list = list;
    }
}
