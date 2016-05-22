package com.pcjr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.pcjr.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mario on 2016/5/20.
 */
public class NewsActivity extends Activity {
    private ListView listView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);
        initView();
    }

    public void initView(){
        listView = (ListView) findViewById(R.id.listview);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, getData(), R.layout.itemnews, new String[]{"newstitle", "newstime"},
                        new int[]{R.id.newstitle, R.id.newstime});
        listView.setAdapter(simpleAdapter);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("newstitle", "J***g");
        map.put("newstime", "2016-05-16 13:30");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("newstitle", "a***7");
        map.put("newstime", "2016-05-16 13:30");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("newstitle", "y***6");
        map.put("newstime", "2016-05-16 13:30");
        list.add(map);

        return list;
    }
}
