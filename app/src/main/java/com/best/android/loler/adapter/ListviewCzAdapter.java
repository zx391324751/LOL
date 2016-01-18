package com.best.android.loler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.best.android.loler.R;
import com.best.android.loler.model.LOLczInfo;

import java.util.List;

/**
 * Created by BL06249 on 2016/1/7.
 */
public class ListviewCzAdapter extends BaseAdapter {

    private Context context;
    private List<LOLczInfo>listCzInfo;

    public ListviewCzAdapter(Context context, List<LOLczInfo>listCzInfo){
        this.context = context;
        this.listCzInfo = listCzInfo;
    }

    @Override
    public int getCount() {
        return listCzInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return listCzInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CzViewHolder czViewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_cz_item, parent, false);
            czViewHolder = new CzViewHolder();
            czViewHolder.tvAuthor = (TextView)convertView.findViewById(R.id.listview_cz_item_tv_author);
            czViewHolder.tvTime = (TextView)convertView.findViewById(R.id.listview_cz_item_tv_time);
            czViewHolder.tvTitle = (TextView)convertView.findViewById(R.id.listview_cz_item_tv_title);
            convertView.setTag(czViewHolder);
        } else {
            czViewHolder = (CzViewHolder)convertView.getTag();
        }

        bindData(czViewHolder, position);

        return convertView;
    }

    private void bindData(CzViewHolder holder, int position) {
        holder.tvAuthor.setText(listCzInfo.get(position).author);
        holder.tvTitle.setText(listCzInfo.get(position).title);
        holder.tvTime.setText(listCzInfo.get(position).time);
    }

    class CzViewHolder{
        public TextView tvAuthor;
        public TextView tvTime;
        public TextView tvTitle;
    }
}
