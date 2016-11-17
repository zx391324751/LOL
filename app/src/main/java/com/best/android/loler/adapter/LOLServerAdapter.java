package com.best.android.loler.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.best.android.loler.R;
import com.best.android.loler.model.LOLServerInfo;

import java.util.List;

/**
 * Created by BL06249 on 2015/12/16.
 */
public class LOLServerAdapter extends BaseAdapter {

    private Context context;
    private LOLServerInfo[] listServer;
    private int selectPosition;

    public LOLServerAdapter(Context context, LOLServerInfo[] listServer, int selectPosition){
        this.context = context;
        this.listServer = listServer;
        this.selectPosition = selectPosition;
    }

    @Override
    public int getCount() {
        return listServer.length;
    }

    @Override
    public Object getItem(int position) {
        return listServer[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ServerViewHolder serverViewHolder = null;
        if(convertView == null){
            serverViewHolder = new ServerViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_item_lol_server, parent, false);

            serverViewHolder.tvName = (TextView) convertView.findViewById(R.id.adapter_item_lol_server_tv_server_name);
            serverViewHolder.ivCheck = (ImageView) convertView.findViewById(R.id.adapter_item_lol_server_iv_select);
            convertView.setTag(serverViewHolder);
        } else {
            serverViewHolder = (ServerViewHolder)convertView.getTag();
        }

        bindData(serverViewHolder, position);
        return convertView;
    }

    private void bindData(ServerViewHolder holder, int position) {
        holder.tvName.setText(listServer[position].fn);
        if(position == selectPosition){
            holder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            holder.ivCheck.setVisibility(View.INVISIBLE);
        }
    }

    class ServerViewHolder{
        TextView tvName;
        ImageView ivCheck;
    }
}
