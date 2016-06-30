package com.pcjinrong.pcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pcjinrong.pcjr.utils.DateUtil;
import com.pcjinrong.pcjr.R;
import com.pcjinrong.pcjr.model.Product;
import com.pcjinrong.pcjr.plugins.ProgressWheel;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by Mario on 2016/5/4.
 */
public class ProductListViewAdapter extends BaseAdapter {
    private List<Product> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private long current_time;
    private long sys_time = System.currentTimeMillis();
    private static class ListItemView {
        public ImageView rtv;
        public ProgressWheel progressWheel;
        public CountdownView cdv;
        public TextView
                time,
                name,
                repayment,
                income,
                amount,
                month,
                month1,
                rate;
    }

    public ProductListViewAdapter(List<Product> list, Context context,long current_time,long sys_time) {
        this.list = list;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.current_time = current_time;
        this.sys_time = sys_time;
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
        ListItemView listItemView = null;
        listItemView = new ListItemView();
        Product product = list.get(position);
        if (product.getStatus() == 2 || product.getStatus() == 3) {
            convertView = layoutInflater.inflate(R.layout.item_product_success, null);

        } else if (product.getStatus() == 4) {
            convertView = layoutInflater.inflate(R.layout.item_product_over, null);
        } else {
            current_time = current_time +  System.currentTimeMillis() - sys_time ;
            Date date = new Date(current_time);
            Date pub_date = new Date(product.getPub_date()*1000);
            try {
                if(DateUtil.isStartDateBeforeEndDate(date,pub_date)){
                    if(DateUtil.getHoursOfTowDiffDate(date,pub_date)>1){
                        convertView = layoutInflater.inflate(R.layout.item_product_coming, null);
                        listItemView.time = (TextView) convertView.findViewById(R.id.time);
                        listItemView.time.setText(DateUtil.transferLongToDate("MM-dd HH:mm",product.getPub_date()*1000));
                    }else{
                        convertView = layoutInflater.inflate(R.layout.item_product_countdown, null);
                        listItemView.cdv = (CountdownView) convertView.findViewById(R.id.cv_countdown);
                        listItemView.cdv.start(DateUtil.getMinusMillisOfDate(date,pub_date));
                    }
                }else{
                    convertView = layoutInflater.inflate(R.layout.item_product, null);
                    listItemView.progressWheel = (ProgressWheel) convertView.findViewById(R.id.pw_spinner);
                    listItemView.rate = (TextView) convertView.findViewById(R.id.rate);
                    listItemView.progressWheel.setProgress(18*product.getRate()/5);
                    listItemView.rate.setText(product.getRate()+"%");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }



        }

        listItemView.rtv = (ImageView) convertView.findViewById(R.id.rtv);
        listItemView.name = (TextView) convertView.findViewById(R.id.name);
        listItemView.repayment = (TextView) convertView.findViewById(R.id.repayment);
        listItemView.income = (TextView) convertView.findViewById(R.id.income);
        listItemView.amount = (TextView) convertView.findViewById(R.id.amount);
        listItemView.month = (TextView) convertView.findViewById(R.id.month);
        listItemView.month1 = (TextView) convertView.findViewById(R.id.month1);

        convertView.setTag(listItemView);


        int repayment = product.getRepayment();
        int preview_repayment = product.getIs_preview_repayment();
        if (repayment == 0) {
            listItemView.repayment.setText("一次还本付息");
        } else if (repayment == 1) {
            listItemView.repayment.setText("先息后本(按月付息)");
        } else if (repayment == 2) {
            listItemView.repayment.setText("等额本息");
        } else if (repayment == 3) {
            listItemView.repayment.setText("先息后本(按季付息)");
        }
        if(product.getFinish_preview_repayment()==1){
            listItemView.rtv.setImageResource(R.drawable.tqhk);
        }else {
            if (preview_repayment == 0) {
                listItemView.rtv.setVisibility(View.GONE);
            } else if (preview_repayment == 1) {
                if(product.getStatus() == 1) {
                    listItemView.rtv.setImageResource(R.drawable.kntqhk);
                }else{
                    listItemView.rtv.setImageResource(R.drawable.kntqhk2);
                }
            }
        }
        listItemView.name.setText(product.getName());

        listItemView.income.setText(String.valueOf(product.getYear_income()));
        listItemView.amount.setText(String.format("%.2f",product.getAmount() / 10000));
        String month = product.getMonth();
        if(month!=null) {
            String regEx = "[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher number = p.matcher(month);
            Pattern pattern = Pattern.compile("[0-9]");
            Matcher str = pattern.matcher(month);
            listItemView.month.setText(number.replaceAll("").trim());
            listItemView.month1.setText(str.replaceAll("").trim());
        }
        return convertView;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }
    public void setCurrent_time(long current_time){
        this.current_time = current_time;
    }
    public void setSys_time(long sys_time){
        this.sys_time = sys_time;
    }
}
