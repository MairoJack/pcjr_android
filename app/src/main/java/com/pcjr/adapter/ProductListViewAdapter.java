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

    private static class ListItemView {
        public RotateTextView rtv;
        public TextView
                name,
                repayment,
                income,
                amount,
                month,
                month1;
    }

    public ProductListViewAdapter(List<Product> list, Context context) {
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
        ListItemView listItemView = null;
        listItemView = new ListItemView();
        Product product = list.get(position);
        if (product.getStatus() == 2) {
            convertView = layoutInflater.inflate(R.layout.item_product_success, null);
        } else if (product.getStatus() == 4) {
            convertView = layoutInflater.inflate(R.layout.item_product_over, null);
        } else {
            convertView = layoutInflater.inflate(R.layout.item_product, null);
        }

        listItemView.rtv = (RotateTextView) convertView.findViewById(R.id.rtv);
        listItemView.rtv.setDegrees(45);
        listItemView.name = (TextView) convertView.findViewById(R.id.name);
        listItemView.repayment = (TextView) convertView.findViewById(R.id.repayment);
        listItemView.income = (TextView) convertView.findViewById(R.id.income);
        listItemView.amount = (TextView) convertView.findViewById(R.id.amount);
        listItemView.month = (TextView) convertView.findViewById(R.id.month);
        listItemView.month1 = (TextView) convertView.findViewById(R.id.month1);

        convertView.setTag(listItemView);


        int repayment = list.get(position).getRepayment();
        int preview_repayment = list.get(position).getIs_preview_repayment();
        if (repayment == 0) {
            listItemView.rtv.setText("一次还本付息");
        } else if (repayment == 1) {
            listItemView.rtv.setText("先息后本(月)");
        } else if (repayment == 2) {
            listItemView.rtv.setText("等额本息");
        } else if (repayment == 3) {
            listItemView.rtv.setText("先息后本(季)");
        }
        if (preview_repayment == 0) {
            listItemView.repayment.setVisibility(View.GONE);
        } else if (preview_repayment == 1) {
            listItemView.repayment.setText("可能提前回款");
        }
        listItemView.name.setText(list.get(position).getName());

        listItemView.income.setText(String.valueOf(list.get(position).getYear_income()));
        listItemView.amount.setText(String.valueOf(list.get(position).getAmount() / 10000));
        listItemView.month.setText(list.get(position).getMonth());
        //listItemView.month1.setText("天");

        return convertView;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }
}
