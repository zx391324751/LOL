package com.best.android.loler.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.best.android.loler.R;
import com.best.android.loler.activity.RoomListActivity;
import com.best.android.loler.manager.ImageLoadManager;
import com.best.android.loler.model.GroupInfo;

import java.util.List;

/**
 * Created by BL06249 on 2016/1/11.
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.RoomViewHolder> implements OnItemClickListener{

    private Context context;
    private List<GroupInfo> listGroups;

    public GroupListAdapter(Context context, List<GroupInfo>listGroups){
        this.context = context;
        this.listGroups = listGroups;
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_group, parent, false);
        RoomViewHolder roomViewHolder = new RoomViewHolder(view);

        return roomViewHolder;
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, final int position) {
        ImageLoadManager.loadImage(holder.imageView, listGroups.get(position).gameSrc, false);
        holder.tvName.setText(listGroups.get(position).gameName);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGroups.size();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(context, RoomListActivity.class);
        intent.putExtra("GroupInfo", listGroups.get(position));
        context.startActivity(intent);
    }

    class RoomViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout layout;
        public ImageView imageView;
        public TextView tvName;

        public RoomViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout)itemView.findViewById(R.id.adapter_item_group_layout);
            imageView = (ImageView) itemView.findViewById(R.id.adapter_item_group_iv);
            tvName = (TextView)itemView.findViewById(R.id.adapter_item_group_tv_name);
        }

    }

}
