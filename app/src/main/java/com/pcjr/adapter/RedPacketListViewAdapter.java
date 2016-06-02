package com.pcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pcjr.R;
import com.pcjr.model.InvestTicket;
import com.pcjr.model.RedPacket;
import com.pcjr.utils.DateUtil;

import java.util.List;

/**
 * 红包适配器
 * Created by Mario on 2016/5/26.
 */
public class RedPacketListViewAdapter extends BaseAdapter {
    private List<RedPacket> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private static class ViewHolder{
        public TextView
                amount,
                title,
                join_date;
    }
    public RedPacketListViewAdapter(List<RedPacket> list, Context context) {
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
            convertView = layoutInflater.inflate(R.layout.item_red_packet,null);

            viewHolder.amount = (TextView)convertView.findViewById(R.id.amount);
            viewHolder.title = (TextView)convertView.findViewById(R.id.title);
            viewHolder.join_date = (TextView)convertView.findViewById(R.id.join_date);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.amount.setText("￥"+list.get(position).getAmount());
        viewHolder.title.setText("来源:"+list.get(position).getTitle());
        viewHolder.join_date.setText("红包获得时间:"+ DateUtil.transferLongToDate("yyyy-MM-dd",list.get(position).getJoin_date()*1000));

        return convertView;
    }

    public void setList(List<RedPacket> list) {
        this.list = list;
    }
}
