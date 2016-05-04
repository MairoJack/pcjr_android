package com.pcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pcjr.R;
import com.pcjr.model.Product;
import com.pcjr.utils.RotateTextView;

import java.util.List;

/**
 * Created by Mario on 2016/5/4.
 */
public class ProductListViewAdapter extends BaseAdapter {
    private List<Product> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private final class ListItemView{
        public RotateTextView rtv;
        public TextView
                product_name,
                seriesname,
                income,
                income1,
                amount,
                month,
                month1;

    }
    public ProductListViewAdapter(List<Product> list, LayoutInflater layoutInflater, Context context) {
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
        ListItemView  listItemView = null;
        if(convertView == null){
            listItemView = new ListItemView();
            convertView = layoutInflater.inflate(R.layout.itemblue,null);

            listItemView.rtv = (RotateTextView)convertView.findViewById(R.id.rtv);
            listItemView.rtv.setDegrees(45);
            listItemView.product_name = (TextView)convertView.findViewById(R.id.product_name);
            listItemView.seriesname = (TextView)convertView.findViewById(R.id.seriesname);
            listItemView.income = (TextView)convertView.findViewById(R.id.income);
            listItemView.income1 = (TextView)convertView.findViewById(R.id.income1);
            listItemView.amount = (TextView)convertView.findViewById(R.id.amount);
            listItemView.month = (TextView)convertView.findViewById(R.id.month);
            listItemView.month1 = (TextView)convertView.findViewById(R.id.month1);

            convertView.setTag(listItemView);
        }else{
            listItemView = (ListItemView)convertView.getTag();
        }

        listItemView.seriesname.setText("大城小爱");
        listItemView.product_name.setText(list.get(position).getName());
        listItemView.income.setText(String.valueOf(list.get(position).getYearIncome()));
        listItemView.income1.setText("00%");
        listItemView.amount.setText(String.valueOf(list.get(position).getAmount()));
        listItemView.month.setText(list.get(position).getMonth());
        listItemView.month1.setText("天");

        return convertView;
    }
}
