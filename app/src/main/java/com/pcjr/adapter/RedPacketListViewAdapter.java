package com.pcjr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.model.RedPacket;
import com.pcjr.plugins.IosDialog;
import com.pcjr.service.ApiService;
import com.pcjr.utils.DateUtil;
import com.pcjr.utils.RetrofitUtils;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 红包适配器
 * Created by Mario on 2016/5/26.
 */
public class RedPacketListViewAdapter extends BaseAdapter {
    private List<RedPacket> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private int type;
    private static class ViewHolder{
        public LinearLayout btn_get_red_packet;
        public TextView
                amount,
                title,
                join_date;
    }
    public RedPacketListViewAdapter(List<RedPacket> list, Context context,int type) {
        this.list = list;
        this.context = context;
        this.type = type;
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
            if(type == 0) {
                convertView = layoutInflater.inflate(R.layout.item_red_packet, null);
                viewHolder.btn_get_red_packet = (LinearLayout) convertView.findViewById(R.id.btn_get_red_packet);
                viewHolder.btn_get_red_packet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRedPacket(position);
                    }
                });
            }else{
                convertView = layoutInflater.inflate(R.layout.item_red_packet_gray, null);
            }
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

    public void getRedPacket(final int position){
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getRedPacketReward(Constant.BEARER+" "+Constant.access_token,list.get(position).getId());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        list.remove(position);
                        IosDialog.show("领取成功","红包金额已转入您的账户余额内",context);
                        notifyDataSetChanged();
                    }else{
                        Toast.makeText(context,json.get("message").getAsString(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setList(List<RedPacket> list) {
        this.list = list;
    }
}
