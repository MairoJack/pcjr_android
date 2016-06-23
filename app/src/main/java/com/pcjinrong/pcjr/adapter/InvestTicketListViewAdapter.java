package com.pcjinrong.pcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pcjinrong.pcjr.model.InvestTicket;
import com.pcjinrong.pcjr.utils.DateUtil;
import com.pcjr.R;

import java.util.List;


/**
 * 投资券适配器
 * Created by Mario on 2016/5/26.
 */
public class InvestTicketListViewAdapter extends BaseAdapter {
    private List<InvestTicket> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private int type;
    private static class ViewHolder{
        public TextView
                amount,
                msg,
                title,
                end_time;
    }
    public InvestTicketListViewAdapter(List<InvestTicket> list, Context context,int type) {
        this.list = list;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.type = type;
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
            if(type==0) {
                convertView = layoutInflater.inflate(R.layout.item_invest_ticket, null);
            }else{
                convertView = layoutInflater.inflate(R.layout.item_invest_ticket_gray, null);
            }
            viewHolder.amount = (TextView)convertView.findViewById(R.id.amount);
            viewHolder.msg = (TextView)convertView.findViewById(R.id.msg);
            viewHolder.title = (TextView)convertView.findViewById(R.id.title);
            viewHolder.end_time = (TextView)convertView.findViewById(R.id.end_time);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.amount.setText(list.get(position).getAmount().replace(".00",""));
        viewHolder.msg.setText("满 "+list.get(position).getReach_amount().replace(".00","")+" 元返 "+list.get(position).getAmount().replace(".00","")+" 元");
        viewHolder.title.setText("来源:"+list.get(position).getTitle());
        viewHolder.end_time.setText("有效期至:"+ DateUtil.transferLongToDate("yyyy-MM-dd",list.get(position).getEnd_time()*1000));

        return convertView;
    }

    public void setList(List<InvestTicket> list) {
        this.list = list;
    }
}
