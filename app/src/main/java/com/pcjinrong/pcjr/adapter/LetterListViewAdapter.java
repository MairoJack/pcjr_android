package com.pcjinrong.pcjr.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.pcjr.R;
import com.pcjinrong.pcjr.model.Letter;
import com.pcjinrong.pcjr.utils.DateUtil;
import java.util.List;


/**
 * 站内信适配器
 * Created by Mario on 2016/5/26.
 */
public class LetterListViewAdapter extends BaseAdapter {
    private List<Letter> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private static class ViewHolder{
        public TextView
                title,
                send_date;
    }
    public LetterListViewAdapter(List<Letter> list, Context context) {
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
        Letter letter = list.get(position);
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_msg_center,null);

            viewHolder.title = (TextView)convertView.findViewById(R.id.title);
            viewHolder.send_date = (TextView)convertView.findViewById(R.id.send_date);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        if(letter.getRead_status() == 1){
            viewHolder.title.setTextColor(Color.parseColor("#ABABAB"));
            viewHolder.send_date.setTextColor(Color.parseColor("#ABABAB"));
        }else{
            viewHolder.title.setTextColor(Color.parseColor("#333333"));
            viewHolder.send_date.setTextColor(Color.parseColor("#333333"));
        }
        viewHolder.title.setText(letter.getTitle());
        viewHolder.send_date.setText(DateUtil.transferLongToDate(DateUtil.DATE_FORMAT_YYYY_MM_DD,letter.getSend_date()*1000));

        return convertView;
    }

    public void setList(List<Letter> list) {
        this.list = list;
    }
}
