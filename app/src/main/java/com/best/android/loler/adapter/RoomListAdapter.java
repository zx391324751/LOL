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
import com.best.android.loler.activity.ZhiboVideoActivity;
import com.best.android.loler.manager.ImageLoadManager;
import com.best.android.loler.model.RoomInfo;

import java.util.List;

/**
 * Created by BL06249 on 2016/1/13.
 */
public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomViewHolder> implements OnItemClickListener {

    private Context context;
    private List<RoomInfo> listRooms;

    public RoomListAdapter(Context context, List<RoomInfo>listRooms){
        this.context = context;
        this.listRooms = listRooms;
    }

    @Override
    public RoomListAdapter.RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_room, parent, false);
        RoomViewHolder roomViewHolder = new RoomViewHolder(view);
        return roomViewHolder;
    }

    @Override
    public void onBindViewHolder(RoomListAdapter.RoomViewHolder holder, final int position) {
        holder.tvName.setText(listRooms.get(position).roomName);
        ImageLoadManager.loadImage(holder.imageView, listRooms.get(position).roomSrc, false);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listRooms.size();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(context, ZhiboVideoActivity.class);
        intent.putExtra("RoomInfo", listRooms.get(position));
        context.startActivity(intent);
    }

    class RoomViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout layout;
        public ImageView imageView;
        public TextView tvName;

        public RoomViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout)itemView.findViewById(R.id.adapter_item_room_layout);
            imageView = (ImageView) itemView.findViewById(R.id.adapter_item_room_iv);
            tvName = (TextView)itemView.findViewById(R.id.adapter_item_room_tv_name);
        }

    }
}
