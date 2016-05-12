package com.pcjr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.pcjr.R;
import com.pcjr.adapter.ProductListViewAdapter;
import com.pcjr.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mario on 2016/5/12.
 */
public class InvestItemOverFragment extends Fragment {
    private ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invest_list,container,false);
        listView = (ListView) view.findViewById(R.id.swipe_target);
        Product p = new Product();
        p.setName("ddddd");
        p.setAmount("44444");
        p.setMonth("4");
        p.setYearIncome("4");
        p.setSeries(1);
        List<Product> list = new ArrayList<>();
        list.add(p);
        list.add(p);
        ListAdapter adapter = new ProductListViewAdapter(list,getActivity());
        listView.setAdapter(adapter);
        return view;
    }
}
